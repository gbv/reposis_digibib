package de.vzg.reposis.digibib.agreement.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstance;
import org.mycore.common.config.annotation.MCRProperty;

import de.vzg.reposis.digibib.agreement.model.Agreement;

@MCRConfigurationProxy(proxyClass = AgreementPdfCreator.Factory.class)
public class AgreementPdfCreator {

    private final String templateResource;

    private final AgreementPdfFormFiller filler;

    public AgreementPdfCreator(String templateResource, AgreementPdfFormFiller filler) {
        this.templateResource = templateResource;
        this.filler = filler;
    }

    public void createPdf(Agreement agreement, OutputStream outputStream) throws IOException {
        try (
            PDDocument document = Loader.loadPDF(getClass().getResourceAsStream(templateResource).readAllBytes())) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                throw new IllegalArgumentException("Invalid PDF form template: no AcroForm found.");
            }
            filler.fillForm(acroForm, agreement);
            acroForm.flatten();
            document.save(outputStream);
        }
    }

    public static final class Factory implements Supplier<AgreementPdfCreator> {

        @MCRProperty(name = "TemplateFilePath")
        public String templateFilePath;

        @MCRInstance(name = "FormFiller", valueClass = AgreementPdfFormFiller.class)
        public AgreementPdfFormFiller formFiller;

        @Override
        public AgreementPdfCreator get() {
            return new AgreementPdfCreator(templateFilePath, formFiller);
        }

    }
}
