package de.vzg.reposis.digibib.agreement.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstance;

import de.vzg.reposis.digibib.agreement.exceptions.AgreementException;
import de.vzg.reposis.digibib.pdf.model.FormData;
import de.vzg.reposis.digibib.pdf.processor.PdfFormProcessor;
import de.vzg.reposis.digibib.pdf.processor.PdfFormProcessorProvider;

/**
 * Service class for generating PDF documents based on {@link FormData}
 * using a configured {@link PdfFormProcessor}.
 * <p>
 * This class encapsulates the selection of the appropriate PDF processor
 * for a given agreement ID and delegates the processing task to it.
 */
@MCRConfigurationProxy(proxyClass = AgreementPdfService.Factory.class)
public class AgreementPdfService {

    private final PdfFormProcessorProvider provider;

    /**
     * Creates a new {@link AgreementPdfService} with the given
     * {@link PdfFormProcessorProvider}.
     *
     * @param provider the provider that supplies {@link PdfFormProcessor} instances, must not be {@code null}
     */
    public AgreementPdfService(PdfFormProcessorProvider provider) {
        this.provider = provider;
    }

    /**
     * Generates a PDF document based on the provided form data
     * and writes the result to the specified {@link OutputStream}.
     *
     * @param agreementId the unique agreement ID used to select the matching processor
     * @param formData the form data to be filled into the PDF
     * @param outputStream the output stream to which the generated PDF will be written
     * @throws IOException if an error occurs while writing the PDF
     * @throws AgreementException if no processor is defined for the given {@code agreementId}
     */
    public void generatePdf(String agreementId, FormData formData, OutputStream outputStream) throws IOException {
        final PdfFormProcessor processor
            = provider.getPdfProcessor(agreementId)
                .orElseThrow(() -> new AgreementException("No processor for '" + agreementId + "' definied."));
        processor.processPdf(formData, outputStream);
    }

    /**
     * Factory class for creating {@link AgreementPdfService} instances,
     * with dependencies injected automatically from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<AgreementPdfService> {

        /**
         * Provider for {@link PdfFormProcessor} instances, injected
         * via MyCoRe configuration.
         */
        @MCRInstance(name = "ProcessorProvider", valueClass = PdfFormProcessorProvider.class, required = true)
        public PdfFormProcessorProvider provider;

        /**
         * Creates a new {@link AgreementPdfService} instance
         * using the configured {@link PdfFormProcessorProvider}.
         *
         * @return a new {@link AgreementPdfService} instance
         */
        @Override
        public AgreementPdfService get() {
            return new AgreementPdfService(provider);
        }

    }
}
