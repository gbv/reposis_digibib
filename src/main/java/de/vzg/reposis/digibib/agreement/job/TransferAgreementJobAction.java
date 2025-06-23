package de.vzg.reposis.digibib.agreement.job;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.MCRSessionMgr;
import org.mycore.common.MCRSystemUserInformation;
import org.mycore.common.MCRUserInformation;
import org.mycore.datamodel.metadata.MCRObject;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobAction;

import de.vzg.reposis.digibib.agreement.service.AgreementMetadataManager;
import de.vzg.reposis.digibib.agreement.service.AgreementService;
import de.vzg.reposis.digibib.pdf.model.FormData;

/**
 * A queued job action that transfers an agreement to a remote service.
 * <p>
 * This job is designed to be executed asynchronously by the MyCoRe queued job system.
 */
public class TransferAgreementJobAction extends MCRJobAction {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Job parameter name for the object ID.
     */
    public static final String OBJECT_ID_PARAM = "objectId";
    private static final String AGREEMENT_ID_PARAM = "agreementId";

    private final AgreementService agreementService;
    private final AgreementMetadataManager metadataManager;

    /**
     * Creates a new {@code TransferAgreementJobAction} for the given job using
     * the default {@link AgreementService} and {@link AgreementMetadataManager}instance.
     *
     * @param job the queued job
     */
    public TransferAgreementJobAction(MCRJob job) {
        this(job, AgreementService.obtainInstance(), AgreementMetadataManager.obtainInstance());
    }

    /**
     * Creates a new {@code TransferAgreementJobAction} for the given job
     * and a custom {@link AgreementService} instance.
     *
     * @param job the queued job
     * @param agreementService the service used to transfer agreements
     */
    public TransferAgreementJobAction(MCRJob job, AgreementService agreementService,
        AgreementMetadataManager metadataManager) {
        super(job);
        this.agreementService = agreementService;
        this.metadataManager = metadataManager;
    }

    /**
     * Creates a new {@link MCRJob} instance containing all parameters
     * necessary to reconstruct and transfer the given agreement.
     *
     * @param objectId the object id
     * @param agreementId the id of the agreement
     * @return a configured job ready for queuing
     */
    public static MCRJob createJob(MCRObjectID objectId, String agreementId) {
        final MCRJob job = new MCRJob(TransferAgreementJobAction.class);
        job.setParameter(AGREEMENT_ID_PARAM, agreementId);
        job.setParameter(OBJECT_ID_PARAM, objectId.toString());

        return job;
    }

    @Override
    public boolean isActivated() {
        return true;
    }

    @Override
    public String name() {
        return getClass().getName();
    }

    @Override
    public void execute() throws ExecutionException {
        LOGGER.debug(() -> "Executing...");
        final MCRUserInformation prevUserInformation = MCRSessionMgr.getCurrentSession().getUserInformation();
        try {
            MCRSessionMgr.getCurrentSession().setUserInformation(MCRSystemUserInformation.getGuestInstance());
            MCRSessionMgr.getCurrentSession().setUserInformation(MCRSystemUserInformation.getJanitorInstance());
            final String agreementId = getAgreementId();
            final MCRObjectID objectId = getObjectId();

            if (!metadataManager.exists(objectId)) {
                LOGGER.info("Cannot transfer agreement, {} does not exist (anymore).", objectId);
                return;
            }
            final MCRObject object = metadataManager.retrieveMCRObject(objectId);
            final FormData formData = agreementService.createAgreementFormData(object, agreementId);
            LOGGER.info("Transferring agreement: '{}' for {}", agreementId, objectId.toString());
            agreementService.transferAgreement(objectId, agreementId, formData);
        } catch (Exception e) {
            throw new ExecutionException(e);
        } finally {
            MCRSessionMgr.getCurrentSession().setUserInformation(MCRSystemUserInformation.getGuestInstance());
            MCRSessionMgr.getCurrentSession().setUserInformation(prevUserInformation);
        }
    }

    @Override
    public void rollback() {
        // no rollback required
    }

    /**
     * Returns the {@link MCRObjectID} of the agreement associated with this job.
     *
     * @return the {@link MCRObjectID} of the agreement
     */
    public MCRObjectID getObjectId() {
        return Optional.ofNullable(job.getParameter(OBJECT_ID_PARAM)).map(MCRObjectID::getInstance).orElseThrow();
    }

    /**
     * Returns the id of the agreement associated with this job.
     *
     * @return the id of the agreement
     */
    public String getAgreementId() {
        return job.getParameter(AGREEMENT_ID_PARAM);
    }

}
