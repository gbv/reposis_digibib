package de.vzg.reposis.digibib.agreement.job;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobAction;

import de.vzg.reposis.digibib.agreement.service.AgreementService;

public class DeliverAgreementJobAction extends MCRJobAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String OBJECT_ID_PROP = "objectId";

    private static final String AGREEMENT_NAME_PROP = "agreement";

    public DeliverAgreementJobAction(MCRJob job) {
        super(job);
    }

    public static MCRJob createJob(MCRObjectID objectId, String agreementName) {
        final MCRJob job = new MCRJob(DeliverAgreementJobAction.class);
        job.setParameter(OBJECT_ID_PROP, objectId.toString());
        job.setParameter(AGREEMENT_NAME_PROP, agreementName);
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
        try {
            AgreementService.obtainInstance().transferAgreement(getObjectId(), getAgreementName());
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public void rollback() {
        // no rollback required
    }

    private String getAgreementName() {
        return Optional.ofNullable(job.getParameter(AGREEMENT_NAME_PROP)).orElseThrow();
    }

    private MCRObjectID getObjectId() {
        return Optional.ofNullable(job.getParameter(OBJECT_ID_PROP)).map(MCRObjectID::getInstance).orElseThrow();
    }
}
