package de.vzg.reposis.digibib.agreement.pdf;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

import de.vzg.reposis.digibib.agreement.model.Agreement;

public class AgreementPdfCreator {

    private final AgreementPdfFormFiller filler;

    private final String templateResource;

    public AgreementPdfCreator(AgreementPdfFormFiller filler, String templateResource) {
        this.filler = filler;
        this.templateResource = templateResource;
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
}
