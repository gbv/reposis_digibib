package de.vzg.reposis.digibib.agreement.pdf.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstance;

import de.vzg.reposis.digibib.agreement.model.AgreementFormData;

/**
 * Service for generating agreement PDFs based on a predefined PDF form template.
 * <p>
 * This class loads a PDF template via an {@link AgreementPdfTemplateLoader},
 * fills its form fields with agreement content using an {@link AgreementPdfProcessor},
 * and writes the resulting PDF to an output stream.
 */
@MCRConfigurationProxy(proxyClass = AgreementPdfService.Factory.class)
public class AgreementPdfService {

    private final AgreementPdfTemplateLoader templateLoader;

    private final AgreementPdfProcessor pdfProcessor;

    /**
     * Creates a new {@code AgreementPdfService} with the given template loader and form filler.
     *
     * @param templateLoader the loader used to retrieve the PDF form template
     * @param filler the form filler used to insert agreement data into the PDF
     */
    public AgreementPdfService(AgreementPdfTemplateLoader templateLoader, AgreementPdfProcessor pdfProcessor) {
        this.templateLoader = templateLoader;
        this.pdfProcessor = pdfProcessor;
    }

    /**
     * Generates a filled PDF for the given agreement content and writes it to the provided output stream.
     * <p>
     * This method loads the PDF template, fills in its fields with data from the
     * {@link AgreementFormData}, flattens the form fields, and saves the result.
     *
     * @param agreementFormData the content to insert into the PDF form
     * @param outputStream the output stream to which the generated PDF is written
     * @throws IOException if an I/O error occurs during PDF loading or saving
     * @throws IllegalArgumentException if the loaded PDF template does not contain an AcroForm
     */
    public void generatePdf(AgreementFormData agreementFormData, OutputStream outputStream) throws IOException {
        final byte[] templateBytes = templateLoader.loadTemplate();
        try (PDDocument document = Loader.loadPDF(templateBytes)) {
            final PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                throw new IllegalArgumentException("Invalid PDF form template: no AcroForm found.");
            }
            pdfProcessor.fillForm(acroForm, agreementFormData);
            acroForm.flatten();
            document.save(outputStream);
        }
    }

    /**
     * Factory for creating {@link AgreementPdfService} instances with injected
     * dependencies from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<AgreementPdfService> {

        /**
         * The template loader used by the {@link AgreementPdfService} to load PDF templates.
         */
        @MCRInstance(name = "TemplateLoader", valueClass = AgreementPdfTemplateLoader.class)
        public AgreementPdfTemplateLoader templateLoader;

        /**
         * The form filler used by the {@link AgreementPdfService} to populate PDF templates.
         */
        @MCRInstance(name = "PdfProcessor", valueClass = AgreementPdfProcessor.class)
        public AgreementPdfProcessor formFiller;

        @Override
        public AgreementPdfService get() {
            return new AgreementPdfService(templateLoader, formFiller);
        }

    }
}
