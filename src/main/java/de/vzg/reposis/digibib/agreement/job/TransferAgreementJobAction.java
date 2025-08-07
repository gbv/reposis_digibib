package de.vzg.reposis.digibib.agreement.job;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobAction;

import de.vzg.reposis.digibib.agreement.model.Agreement;
import de.vzg.reposis.digibib.agreement.model.AgreementContent;
import de.vzg.reposis.digibib.agreement.model.Author;
import de.vzg.reposis.digibib.agreement.service.AgreementTransferService;

public class TransferAgreementJobAction extends MCRJobAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String NAME_PARAM = "aN";

    private static final String CONTENT_TITLE_PARAM = "aCT";
    private static final String CONTENT_DOI_PARAM = "acD";
    private static final String CONTENT_COMMENT_PARAM = "acC";
    private static final String CONTENT_LICENSE_PARAM = "aCL";
    private static final String CONTENT_EMBARGO_PARAM = "aCE";

    private static final String CONTENT_AUTHOR_NAME_PARAM = "aCAN";
    private static final String CONTENT_AUTHOR_EMAIL_PARAM = "aCAE";
    private static final String CONTENT_AUTHOR_INSTITUTE_PARAM = "aCAI";
    private static final String CONTENT_AUTHOR_PHONE_PARAM = "aCAP";

    private final AgreementTransferService agreementService;

    public TransferAgreementJobAction(MCRJob job) {
        this(job, AgreementTransferService.obtainInstance());
    }

    public TransferAgreementJobAction(MCRJob job, AgreementTransferService agreementService) {
        super(job);
        this.agreementService = agreementService;
    }

    public static MCRJob createJob(Agreement agreement) {
        final MCRJob job = new MCRJob(TransferAgreementJobAction.class);
        job.setParameter(NAME_PARAM, agreement.getAgreementName());

        AgreementContent content = agreement.getContent();
        job.setParameter(CONTENT_TITLE_PARAM, content.getTitle());
        job.setParameter(CONTENT_DOI_PARAM, content.getDoi());
        job.setParameter(CONTENT_COMMENT_PARAM, content.getComment());
        job.setParameter(CONTENT_LICENSE_PARAM, content.getLicense());

        Optional.ofNullable(content.getEmbargo())
            .map(LocalDate::toString)
            .ifPresent(dateStr -> job.setParameter(CONTENT_EMBARGO_PARAM, dateStr));

        Author author = content.getAuthor();
        job.setParameter(CONTENT_AUTHOR_NAME_PARAM, author.getName());
        job.setParameter(CONTENT_AUTHOR_EMAIL_PARAM, author.getEmail());
        job.setParameter(CONTENT_AUTHOR_INSTITUTE_PARAM, author.getInstitute());
        job.setParameter(CONTENT_AUTHOR_PHONE_PARAM, author.getPhone());

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
            final Agreement agreement = getAgreement();
            LOGGER.info("Transferring agreement: {}", agreement.getAgreementName());
            agreementService.transferAgreement(agreement);
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public void rollback() {
        // no rollback required
    }

    protected Agreement getAgreement() {
        final Author author = new Author.Builder()
            .name(job.getParameter(CONTENT_AUTHOR_NAME_PARAM))
            .email(job.getParameter(CONTENT_AUTHOR_EMAIL_PARAM))
            .institute(job.getParameter(CONTENT_AUTHOR_INSTITUTE_PARAM))
            .phone(job.getParameter(CONTENT_AUTHOR_PHONE_PARAM))
            .build();

        final LocalDate embargo
            = Optional.ofNullable(job.getParameter(CONTENT_EMBARGO_PARAM)).map(LocalDate::parse).orElse(null);

        final AgreementContent content = new AgreementContent.Builder()
            .author(author)
            .comment(job.getParameter(CONTENT_COMMENT_PARAM))
            .doi(job.getParameter(CONTENT_DOI_PARAM))
            .embargo(embargo)
            .license(job.getParameter(CONTENT_LICENSE_PARAM))
            .title(job.getParameter(CONTENT_TITLE_PARAM))
            .build();

        return new Agreement(job.getParameter(NAME_PARAM), content);
    }
}
