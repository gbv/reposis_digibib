package de.vzg.reposis.digibib.agreement.pdf.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstance;

import de.vzg.reposis.digibib.agreement.model.AgreementContent;
import de.vzg.reposis.digibib.agreement.pdf.formfiller.AgreementPdfFormFiller;

@MCRConfigurationProxy(proxyClass = AgreementPdfService.Factory.class)
public class AgreementPdfService {

    private final AgreementPdfTemplateLoader templateLoader;

    private final AgreementPdfFormFiller filler;

    public AgreementPdfService(AgreementPdfTemplateLoader templateLoader, AgreementPdfFormFiller filler) {
        this.templateLoader = templateLoader;
        this.filler = filler;
    }

    public void generatePdf(AgreementContent agreementContent, OutputStream outputStream) throws IOException {
        final byte[] templateBytes = templateLoader.loadTemplate();
        try (PDDocument document = Loader.loadPDF(templateBytes)) {
            final PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                throw new IllegalArgumentException("Invalid PDF form template: no AcroForm found.");
            }
            filler.fillForm(acroForm, agreementContent);
            acroForm.flatten();
            document.save(outputStream);
        }
    }

    public static final class Factory implements Supplier<AgreementPdfService> {

        @MCRInstance(name = "TemplateLoader", valueClass = AgreementPdfTemplateLoader.class)
        public AgreementPdfTemplateLoader templateLoader;

        @MCRInstance(name = "FormFiller", valueClass = AgreementPdfFormFiller.class)
        public AgreementPdfFormFiller formFiller;

        @Override
        public AgreementPdfService get() {
            return new AgreementPdfService(templateLoader, formFiller);
        }

    }
}
