package de.vzg.reposis.digibib;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.junit.Test;
import org.mycore.common.MCRException;

public class AgreementTest {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final SubmitterInfo submitterInfo = new SubmitterInfo("Test", "Test", "test@test.de", "0000");

    private static final ObjectInfo objectInfo = new ObjectInfo("Title", null, "license", "access", "comment");

    @Test
    public void testAgreement() throws IOException {
        final OutputStream test = Files.newOutputStream(Path.of("/tmp/test.pdf"));
        final String pathname = getPathname();

        try (PDDocument document = Loader.loadPDF(getClass().getResourceAsStream(pathname).readAllBytes())) {

            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm != null) {
                acroForm.getDefaultResources().put(COSName.getPDFName("Helvetica"),
                    new PDType1Font(FontName.HELVETICA));

                final PDTextField nameField = (PDTextField) acroForm.getField("Name Vorname");
                if (nameField != null) {
                    nameField.setDefaultAppearance("/Helvetica 10 Tf 0 g");
                    nameField.setValue(submitterInfo.name());
                }

                final PDTextField instituteField = (PDTextField) acroForm.getField("Institut");
                if (instituteField != null) {
                    instituteField.setDefaultAppearance("/Helvetica 10 Tf 0 g");
                    instituteField.setValue(submitterInfo.institute());
                }

                final PDTextField emailField = (PDTextField) acroForm.getField("E-Mail");
                if (emailField != null) {
                    emailField.setDefaultAppearance("/Helvetica 10 Tf 0 g");
                    emailField.setValue(submitterInfo.email());
                }

                final PDTextField phoneField = (PDTextField) acroForm.getField("Telefon");
                if (phoneField != null) {
                    phoneField.setDefaultAppearance("/Helvetica 10 Tf 0 g");
                    phoneField.setValue(submitterInfo.phone());
                }

                final PDTextField titleField = (PDTextField) acroForm.getField("Titel Datensatz");
                if (titleField != null) {
                    titleField.setDefaultAppearance("/Helvetica 10 Tf 0 g");
                    titleField.setValue(objectInfo.title());
                }

                final PDTextField commentField = (PDTextField) acroForm.getField("Anmerkungen");
                if (commentField != null) {
                    commentField.setDefaultAppearance("/Helvetica 10 Tf 0 g");
                    commentField.setValue(objectInfo.comment());
                }

                // License
                // TODO use direct license value instead of check box?
                final PDButton btn = (PDButton) acroForm.getField("Group1");
                btn.setValue(6);

                // Embago
                // TODO use Date or -?
                final PDButton btn2 = (PDButton) acroForm.getField("Group2");
                btn2.setValue(1);

                // Access
                // TODO use direct value?
                final PDButton btn3 = (PDButton) acroForm.getField("Group3");
                btn3.setValue(1);

                // TODO use revision?
                // final MCRObject object = MCRMetadataManager.retrieveMCRObject(objectId);
                // TODO fill form
                acroForm.flatten();
                document.save(test);
            } else {
                throw new MCRException("Invalid form");
            }
        }
    }

    private String getPathname() {
        // TODO move to mycore.properties
        return "/META-INF/resources/content/publish/deposit-rd.pdf";
    }

    private record SubmitterInfo(String name, String institute, String email, String phone) {

    }

    private record ObjectInfo(String title, Date embago, String license, String access, String comment) {

    }
}
