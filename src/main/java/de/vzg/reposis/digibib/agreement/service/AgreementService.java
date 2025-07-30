package de.vzg.reposis.digibib.agreement.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstance;
import org.mycore.datamodel.metadata.MCRObject;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobQueue;
import org.mycore.services.queuedjob.MCRJobQueueManager;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.factory.AgreementFormDataFactory;
import de.vzg.reposis.digibib.agreement.job.TransferAgreementJobAction;
import de.vzg.reposis.digibib.agreement.model.AgreementFormData;
import de.vzg.reposis.digibib.agreement.pdf.service.AgreementPdfService;
import de.vzg.reposis.digibib.agreement.pdf.service.AgreementPdfServiceProvider;
import de.vzg.reposis.digibib.agreement.transport.AgreementTransmitter;

/**
 * Service class responsible for managing agreements associated with {@link MCRObject} instances.
 * <p>
 * This service validates objects before creating agreements, determines the correct
 * agreement name based on genre or configuration defaults, and manages agreement
 * transfer scheduling and execution.
 */
@MCRConfigurationProxy(proxyClass = AgreementService.Factory.class)
public class AgreementService {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CONFIG_PREFIX = "Digibib.Agreement.";
    private static final String AGREEMENT_DEFAULT_NAME_CONF = "Default";

    private final AgreementFormDataFactory formDataFactory;
    private final MCRJobQueue jobQueue;
    private final ObjectAgreementService objectService;
    private final AgreementTransmitter transmitter;
    private final AgreementMetadataManager metadataManager;
    private AgreementPdfServiceProvider pdfServiceProvider;

    /**
     * Creates a new {@code AgreementService} instance with the provided dependencies.
     *
     * @param jobQueue the job queue for scheduling agreement transfer jobs
     * @param formDataFactory factory for generating agreement form data from {@link MCRObject}s
     * @param objectService service for accessing and modifying agreement-related object data
     * @param transmitter transmitter for performing agreement transfers
     * @param metadataManager manager for retrieving and updating metadata of {@link MCRObject}s
     * @param pdfServiceProvider the provider for PDF generation services
     */
    public AgreementService(MCRJobQueue jobQueue, AgreementFormDataFactory formDataFactory,
        ObjectAgreementService objectService, AgreementTransmitter transmitter,
        AgreementMetadataManager metadataManager, AgreementPdfServiceProvider pdfServiceProvider) {
        this.formDataFactory = formDataFactory;
        this.jobQueue = jobQueue;
        this.objectService = objectService;
        this.transmitter = transmitter;
        this.metadataManager = metadataManager;
        this.pdfServiceProvider = pdfServiceProvider;
    }

    /**
     * Returns the shared singleton instance of {@code AgreementService}.
     *
     * @return the singleton instance
     */
    public static AgreementService obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    /**
     * Creates a new independent instance of {@code AgreementService} using default dependencies.
     *
     * @return a new service instance
     */
    public static AgreementService createInstance() {
        return MCRConfiguration2.<AgreementService>getInstanceOf(CONFIG_PREFIX + "Service.Class").orElseThrow();
    }

    // ContentTransformer.mycoreobject-agreement-rd-form-data

    /**
     * Creates a new {@link AgreementFormData} for the given {@link MCRObject} if all requirements are met.
     *
     * @param obj the {@link MCRObject} for which the agreement is created
     * @param requiredAgreement the required agreement name
     * @return the created {@link AgreementFormData}
     * @throws AgreementException if the object is not in a valid state or the agreement is not applicable
     */
    public AgreementFormData createAgreementFormData(MCRObject obj, String requiredAgreement) {
        LOGGER.debug("Creating agreement {} for object {}...", requiredAgreement, obj.getId());
        validateObjectStateForAgreement(obj);
        if (!objectService.hasConfirmedAgreement(obj, requiredAgreement)) {
            throw new AgreementException(
                "Object " + obj.getId() + " already has no required agreement " + requiredAgreement);
        }
        return formDataFactory.fromObject(obj, requiredAgreement);
    }

    private void validateObjectStateForAgreement(MCRObject obj) {
        if (!objectService.isPublished(obj)) {
            throw new AgreementException(
                "Object " + obj.getId() + "is not published. Agreement creation not possible.");
        }
        if (objectService.extractGenre(obj).isEmpty()) {
            throw new AgreementException(
                "Object " + obj.getId() + " has no genre assigned. Agreement creation not possible.");
        }
        if (!objectService.hasDoi(obj)) {
            throw new AgreementException(
                "Object " + obj.getId() + " has no DOI assigned. Agreement creation not possible.");
        }
    }

    /**
     * Resolves the required agreement name for the given {@link MCRObject} based on its genre.
     * Falls back to the default agreement name if no genre-specific agreement is configured.
     *
     * @param obj the object for which to resolve the agreement
     * @return an {@link Optional} containing the resolved agreement name
     * @throws java.util.NoSuchElementException if the object has no genre
     */
    public Optional<String> resolveRequiredAgreement(MCRObject obj) {
        LOGGER.debug("Resolving required agreement for object {}...", obj.getId());
        final String genre = objectService.extractGenre(obj).orElseThrow(
            () -> new AgreementException("Cannot resolve agreement for object " + obj.getId() + ": Genre is missing."));
        LOGGER.debug("Genre {} resolved for object {}.", genre, obj.getId());
        return getAgreementName(genre).or(() -> getDefaultAgreementName());
    }

    public void transferAgreement(MCRObjectID objectId, String agreementName, AgreementFormData formData) {
        LOGGER.info("Starting transfer of agreement {} for object {}...", agreementName, objectId);
        if (!metadataManager.exists(objectId)) {
            throw new AgreementException(
                "Cannot transfer agreement, " + objectId.toString() + " does not exist (anymore).");
        }
        final MCRObject object = metadataManager.retrieveMCRObject(objectId);
        final AgreementPdfService serivce = pdfServiceProvider.getPdfService(agreementName).orElseThrow();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            serivce.generatePdf(formData, baos);
            final byte[] agreementBytes = baos.toByteArray();
            transmitter.send(getAgreementFilename(object) + ".pdf", agreementBytes);
        } catch (IOException e) {
            throw new AgreementException(e);
        }
        LOGGER.debug("Agreement {} successfully transferred for object {}.", agreementName, objectId);
        LOGGER.debug("Updating agreement metadata for object {}...", objectId);
        objectService.setAgreementTransmissionInfoNow(object, agreementName);
        objectService.removeConfirmedAgreements(object);
        metadataManager.update(object);
        LOGGER.debug("Agreement metadata updated after transfer for object {}.", objectId);
    }

    /**
     * Checks whether an agreement transfer job for the given object is already scheduled.
     *
     * @param objectId the ID of the object to check
     * @return {@code true} if a job is scheduled, otherwise {@code false}
     */
    public boolean isAgreementTransferScheduled(MCRObjectID objectId) {
        LOGGER.debug("Checking if an agreement transfer job is already scheduled for object {}...", objectId);
        final Iterator<MCRJob> it = jobQueue.iterator(null);
        while (it.hasNext()) {
            final MCRJob job = it.next();
            if (TransferAgreementJobAction.class.equals(job.getAction())) {
                final String targetId = job.getParameters().get(TransferAgreementJobAction.OBJECT_ID_PARAM);
                if (objectId.toString().equals(targetId)) {
                    LOGGER.debug("Agreement transfer job found for object {}.", targetId);
                    return true;
                }
            }
        }
        LOGGER.debug("No scheduled agreement transfer job found for object {}.", objectId);
        return false;
    }

    /**
     * Schedules an agreement transfer job for the given {@link Agreement}.
     *
     * @param agreement the agreement to transfer
     * @throws AgreementException if a transfer for this object is already scheduled
     */
    public void scheduleAgreementTransfer(MCRObjectID objectId, String requiredAgreement) {
        LOGGER.info("Scheduling agreement transfer for object {}...", objectId);
        if (isAgreementTransferScheduled(objectId)) {
            throw new AgreementException(
                "Agreement transfer for object " + objectId + " is already scheduled.");
        }
        jobQueue.add(TransferAgreementJobAction.createJob(objectId, requiredAgreement));
        LOGGER.debug("Agreement transfer job successfully scheduled for object {}.", objectId);
    }

    /**
     * Retrieves the default agreement name from configuration.
     *
     * @return an {@link Optional} containing the default agreement name, if configured
     */
    public Optional<String> getDefaultAgreementName() {
        return getAgreementName(AGREEMENT_DEFAULT_NAME_CONF);
    }

    /**
     * Retrieves the configured agreement name for the given genre.
     *
     * @param genre the genre key
     * @return an {@link Optional} containing the configured agreement name, if present
     */
    public Optional<String> getAgreementName(String genre) {
        return MCRConfiguration2.getString(CONFIG_PREFIX + genre + ".Name");
    }

    protected String getAgreementFilename(MCRObject obj) {
        return Optional.ofNullable(objectService.getDoi(obj)).orElseThrow();
    }

    protected ObjectAgreementService getObjectService() {
        return objectService;
    }

    private static final class InstanceHolder {
        private static final AgreementService SHARED_INSTANCE = createInstance();
    }

    /**
     * Factory for creating {@link AgreementPdfService} instances with injected
     * dependencies from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<AgreementService> {

        /**
         * The {AgreementTransferService} to transfer agreement.
         */
        @MCRInstance(name = "Transmitter", valueClass = AgreementTransmitter.class)
        public AgreementTransmitter transmitter;

        /**
         * The {AgreementTransferService} to process pdf agreements.
         */
        @MCRInstance(name = "PdfServiceProvider", valueClass = AgreementPdfServiceProvider.class)
        public AgreementPdfServiceProvider pdfServiceProvider;

        @Override
        public AgreementService get() {
            return new AgreementService(MCRJobQueueManager.getInstance().getJobQueue(TransferAgreementJobAction.class),
                AgreementFormDataFactory.obtainInstance(), ObjectAgreementService.obtainInstance(),
                transmitter, AgreementMetadataManager.obtainInstance(),
                pdfServiceProvider);
        }

    }
}
