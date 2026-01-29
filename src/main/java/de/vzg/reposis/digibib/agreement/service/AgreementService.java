package de.vzg.reposis.digibib.agreement.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
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

import de.vzg.reposis.digibib.agreement.exceptions.AgreementException;
import de.vzg.reposis.digibib.agreement.job.TransferAgreementJobAction;
import de.vzg.reposis.digibib.agreement.mapping.AgreementFormDataMapper;
import de.vzg.reposis.digibib.agreement.transport.AgreementTransmitter;
import de.vzg.reposis.digibib.pdf.model.FormData;
import de.vzg.reposis.digibib.pdf.processor.PdfFormProcessor;

/**
 * Service class responsible for managing agreements associated with {@link MCRObject} instances.
 * <p>
 * This service validates objects before creating agreements, determines the correct
 * agreement id based on genre or configuration defaults, and manages agreement
 * transfer scheduling and execution.
 */
@MCRConfigurationProxy(proxyClass = AgreementService.Factory.class)
public class AgreementService {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CONFIG_PREFIX = "Digibib.Agreement.";
    private static final Optional<String> DEFAULT_AGREEMENT_ID
        = MCRConfiguration2.getString(CONFIG_PREFIX + "Default.Id");

    private final AgreementFormDataMapper formDataFactory;
    private final MCRJobQueue jobQueue;
    private final ObjectAgreementService objectService;
    private final AgreementTransmitter transmitter;
    private final AgreementMetadataManager metadataManager;
    private final AgreementPdfService pdfService;

    /**
     * Creates a new {@code AgreementService} instance with the provided dependencies.
     *
     * @param jobQueue the job queue for scheduling agreement transfer jobs
     * @param formDataFactory factory for generating agreement form data from {@link MCRObject}s
     * @param objectService service for accessing and modifying agreement-related object data
     * @param transmitter transmitter for performing agreement transfers
     * @param metadataManager manager for retrieving and updating metadata of {@link MCRObject}s
     * @param pdfService the service for PDF generation
     */
    public AgreementService(MCRJobQueue jobQueue, AgreementFormDataMapper formDataFactory,
        ObjectAgreementService objectService, AgreementTransmitter transmitter,
        AgreementMetadataManager metadataManager, AgreementPdfService pdfService) {
        this.formDataFactory = formDataFactory;
        this.jobQueue = jobQueue;
        this.objectService = objectService;
        this.transmitter = transmitter;
        this.metadataManager = metadataManager;
        this.pdfService = pdfService;
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

    /**
     * Creates a new {@link FormData} for the given {@link MCRObject} if all requirements are met.
     *
     * @param obj the {@link MCRObject} for which the agreement id is created
     * @param requiredAgreementId the required agreement id
     * @return the created {@link FormData}
     * @throws AgreementException if the object is not in a valid state or the agreement is not applicable
     */
    public FormData createAgreementFormData(MCRObject obj, String requiredAgreementId) {
        LOGGER.debug("Creating agreement {} for object {}...", requiredAgreementId, obj.getId());
        validateObjectStateForAgreement(obj);
        if (!objectService.hasConfirmedAgreement(obj, requiredAgreementId)) {
            throw new AgreementException(
                "Object " + obj.getId() + " already has no required agreement " + requiredAgreementId);
        }
        return formDataFactory.fromObject(obj, requiredAgreementId);
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
     * Resolves the required agreement id for the given {@link MCRObject} based on its genre.
     * Falls back to the default agreement id if no genre-specific agreement is configured.
     *
     * @param obj the object for which to resolve the id
     * @return an {@link Optional} containing the resolved agreement id
     * @throws java.util.NoSuchElementException if the object has no genre
     */
    public Optional<String> resolveRequiredAgreementId(MCRObject obj) {
        LOGGER.debug("Resolving required agreement id for object {}...", obj.getId());
        final String genre = objectService.extractGenre(obj).orElseThrow(
            () -> new AgreementException("Cannot resolve agreement for object " + obj.getId() + ": Genre is missing."));
        LOGGER.debug("Genre {} resolved for object {}.", genre, obj.getId());
        return getAgreementIdByGenre(genre).or(() -> DEFAULT_AGREEMENT_ID);
    }

    public void transferAgreement(MCRObjectID objectId, String agreementId, FormData formData) {
        LOGGER.info("Starting transfer of agreement {} for object {}...", agreementId, objectId);
        if (!metadataManager.exists(objectId)) {
            throw new AgreementException(
                "Cannot transfer agreement, " + objectId.toString() + " does not exist (anymore).");
        }
        final MCRObject object = metadataManager.retrieveMCRObject(objectId);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            pdfService.generatePdf(agreementId, formData, baos);
            final byte[] agreementBytes = baos.toByteArray();
            transmitter.send(getAgreementFilename(object) + ".pdf", agreementBytes);
        } catch (IOException e) {
            throw new AgreementException(e);
        }
        LOGGER.debug("Agreement {} successfully transferred for object {}.", agreementId, objectId);
        LOGGER.debug("Updating agreement metadata for object {}...", objectId);
        objectService.setAgreementTransmissionInfoNow(object, agreementId);
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
     * Schedules an agreement transfer job for the given agreement.
     *
     * @param objectId the object id
     * @param requiredAgreement the required agreement
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
     * Retrieves the configured agreement id for the given genre.
     *
     * @param genre the genre key
     * @return an {@link Optional} containing the configured agreement id, if present
     */
    public Optional<String> getAgreementIdByGenre(String genre) {
        return MCRConfiguration2.getString(CONFIG_PREFIX + "Genres." + genre + ".Id");
    }

    /**
     * Retrieves the filename for the agreement associated with the given {@link MCRObject}.
     * <p>
     * The filename is derived from the DOI of the object via {@link ObjectAgreementService}.
     * If no DOI is available, an exception will be thrown.
     *
     * @param obj the {@link MCRObject} for which the agreement filename should be determined
     * @return the agreement filename, based on the object's DOI
     * @throws NoSuchElementException if the object has no DOI assigned
     */
    protected String getAgreementFilename(MCRObject obj) {
        return Optional.ofNullable(objectService.getDoi(obj)).orElseThrow();
    }

    /**
     * Provides access to the underlying {@link ObjectAgreementService}.
     *
     * @return the injected {@link ObjectAgreementService} instance
     */
    protected ObjectAgreementService getObjectService() {
        return objectService;
    }

    private static final class InstanceHolder {
        private static final AgreementService SHARED_INSTANCE = createInstance();
    }

    /**
     * Factory for creating {@link PdfFormProcessor} instances with injected
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
        @MCRInstance(name = "PdfService", valueClass = AgreementPdfService.class)
        public AgreementPdfService pdfService;

        @Override
        public AgreementService get() {
            return new AgreementService(MCRJobQueueManager.getInstance().getJobQueue(TransferAgreementJobAction.class),
                AgreementFormDataMapper.obtainInstance(), ObjectAgreementService.obtainInstance(),
                transmitter, AgreementMetadataManager.obtainInstance(), pdfService);
        }

    }
}
