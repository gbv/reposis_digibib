package de.vzg.reposis.digibib.pdf.processor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstanceMap;

/**
 * Provider class for retrieving {@link PdfFormProcessor} instances by agreement id.
 * <p>
 * This class acts as a registry for multiple PDF generation services, allowing
 * different agreement types to have their own PDF generation logic.
 */
@MCRConfigurationProxy(proxyClass = PdfFormProcessorProvider.Factory.class)
public class PdfFormProcessorProvider {

    private Map<String, PdfFormProcessor> pdfServices;

    /**
     * Creates a new {@code AgreementPdfServiceProvider} with the given map of PDF services.
     *
     * @param pdfServices a map of agreement ids to their corresponding {@link PdfFormProcessor}
     */
    public PdfFormProcessorProvider(Map<String, PdfFormProcessor> pdfServices) {
        this.pdfServices = pdfServices;
    }

    /**
     * Retrieves the {@link PdfFormProcessor} for the given agreement id, if available.
     *
     * @param agreementId the id of the agreement
     * @return an {@link Optional} containing the matching {@link PdfFormProcessor}, or empty if not found
     */
    public Optional<PdfFormProcessor> getPdfProcessor(String agreementId) {
        return Optional.ofNullable(pdfServices.get(agreementId));
    }

    /**
     * Factory for creating {@link PdfFormProcessorProvider} instances with
     * injected dependencies from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<PdfFormProcessorProvider> {

        /**
         * A map of named {@link PdfFormProcessor} instances to be provided
         * by the {@link PdfFormProcessorProvider}.
         */
        @MCRInstanceMap(name = "Processors", valueClass = PdfFormProcessor.class, required = true)
        public Map<String, PdfFormProcessor> processors;

        @Override
        public PdfFormProcessorProvider get() {
            return new PdfFormProcessorProvider(processors);
        }

    }

}
