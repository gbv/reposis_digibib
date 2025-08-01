package de.vzg.reposis.digibib.agreement.job;

import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobAction;

import de.vzg.reposis.digibib.agreement.model.Agreement;
import de.vzg.reposis.digibib.agreement.service.AgreementService;

public class DeliverAgreementJobAction extends MCRJobAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String LICENSE_PROP = "license";

    private static final String DOI_PROP = "doi";

    public DeliverAgreementJobAction(MCRJob job) {
        super(job);
    }

    public static MCRJob createJob(Agreement agreement) {
        final MCRJob job = new MCRJob(DeliverAgreementJobAction.class);
        job.setParameter(LICENSE_PROP, agreement.license());
        job.setParameter(DOI_PROP, agreement.doi());
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
            AgreementService.obtainInstance().transferAgreement(getAgreement());
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public void rollback() {
        // no rollback required
    }

    private Agreement getAgreement() {
        return new Agreement.Builder()
            .license(job.getParameter(LICENSE_PROP))
            .doi(job.getParameter(DOI_PROP))
            .build();
    }
}
