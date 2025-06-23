package de.vzg.reposis.digibib.pdf.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstance;

import de.vzg.reposis.digibib.pdf.model.FormData;
import de.vzg.reposis.digibib.pdf.util.ResourceFileLoader;

/**
 * Service for processing PDFs based on a predefined PDF form template.
 * <p>
 * This class loads a PDF template via an {@link ResourceFileLoader},
 * fills its form fields using an {@link FormDataPdfFiller},
 * and writes the resulting PDF to an output stream.
 */
@MCRConfigurationProxy(proxyClass = PdfFormProcessor.Factory.class)
public class PdfFormProcessor {

    private final ResourceFileLoader loader;

    private final FormDataPdfFiller filler;

    /**
     * Creates a new {@code FormDataPdfFillerService} with the given template loader and form filler.
     *
     * @param loader the loader used to retrieve the PDF form template
     * @param filler the form filler used to insert form data into the PDF
     */
    public PdfFormProcessor(ResourceFileLoader loader, FormDataPdfFiller filler) {
        this.loader = loader;
        this.filler = filler;
    }

    /**
     * Generates a filled PDF for the {@code FormData} and writes it to the provided output stream.
     * <p>
     * This method loads the PDF template, fills in its fields with data from the
     * {@link FormData}, flattens the form fields, and saves the result.
     *
     * @param formData the content to insert into the PDF form
     * @param outputStream the output stream to which the generated PDF is written
     * @throws IOException if an I/O error occurs during PDF loading or saving
     * @throws IllegalArgumentException if the loaded PDF template does not contain an AcroForm
     */
    public void processPdf(FormData formData, OutputStream outputStream) throws IOException {
        final byte[] templateBytes = loader.load();
        try (PDDocument document = Loader.loadPDF(templateBytes)) {
            final PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                throw new PdfFormProcessorException("Invalid PDF form template: no AcroForm found.");
            }
            filler.fillForm(acroForm, formData);
            acroForm.flatten();
            document.save(outputStream);
        }
    }

    /**
     * Factory for creating {@link PdfFormProcessor} instances with injected
     * dependencies from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<PdfFormProcessor> {

        /**
         * The loader used by the {@link PdfFormProcessor} to load PDF templates.
         */
        @MCRInstance(name = "FileLoader", valueClass = ResourceFileLoader.class)
        public ResourceFileLoader fileLoader;

        /**
         * The form filler used by the {@link PdfFormProcessor} to populate PDF templates.
         */
        @MCRInstance(name = "Filler", valueClass = FormDataPdfFiller.class)
        public FormDataPdfFiller formFiller;

        @Override
        public PdfFormProcessor get() {
            return new PdfFormProcessor(fileLoader, formFiller);
        }

    }
}
