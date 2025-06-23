package de.vzg.reposis.digibib.agreement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.mycore.common.MCRException;
import org.mycore.datamodel.metadata.MCRMetadataManager;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobAction;

import de.vzg.reposis.digibib.email.EmailClientFactory;
import de.vzg.reposis.digibib.email.model.EmailAttachment;
import de.vzg.reposis.digibib.email.model.EmailMessage;
import de.vzg.reposis.digibib.email.model.EmailSendRequest;

public class DeliverAgreementJobAction extends MCRJobAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String OBJECT_ID_PARAM = "object_id";

    public DeliverAgreementJobAction(MCRJob job) {
        super(job);
    }

    public static MCRJob createJob(MCRObjectID objectId) {
        final MCRJob job = new MCRJob(DeliverAgreementJobAction.class);
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
        final MCRObjectID objectId = getObjectId();
        if (objectId == null) {
            throw new MCRException("Job requires a corresponding object id");
        }
        if (!MCRMetadataManager.exists(objectId)) {
            LOGGER.warn(() -> objectId.toString() + " does not exist");
            return;
        }

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            createPdf(objectId, outputStream);
            // TODO recipient mail
            final EmailMessage message = new EmailMessage.Builder("l.golsch@tu-braunschweig.de").build();
            // TODO filename
            final EmailAttachment attachment
                = new EmailAttachment("doi", "application/pdf", outputStream.toByteArray());
            EmailClientFactory.getInstance("default").sendEmail(new EmailSendRequest(message, List.of(attachment)));
        } catch (IOException e) {
            throw new ExecutionException(e);
        }
    }

    private MCRObjectID getObjectId() {
        return MCRObjectID.getInstance(job.getParameter(OBJECT_ID_PARAM));
    }

    private void createPdf(MCRObjectID objectId, ByteArrayOutputStream outputStream)
        throws IOException {
        LOGGER.debug(() -> "Creating pdf...");
        final String pathname = getPathname();
        try (PDDocument document = Loader.loadPDF(getClass().getResourceAsStream(pathname).readAllBytes())) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm != null) {
                LOGGER.debug(() -> acroForm.getFields().size());
                LOGGER.debug(() -> acroForm.getFields());
                // TODO use revision?
                // final MCRObject object = MCRMetadataManager.retrieveMCRObject(objectId);
                // TODO fill form

                acroForm.flatten();
                document.save(outputStream);
            } else {
                throw new MCRException("Invalid form");
            }
        }
    }

    private String getPathname() {
        // TODO move to mycore.properties
        return "/META-INF/resources/content/publish/deposit-rd.pdf";
    }

    @Override
    public void rollback() {
        // no rollback required
    }
}
