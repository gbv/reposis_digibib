package de.vzg.reposis.digibib.agreement.pdf.service;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstanceMap;

/**
 * Provider class for retrieving {@link AgreementPdfService} instances by agreement id.
 * <p>
 * This class acts as a registry for multiple PDF generation services, allowing
 * different agreement types to have their own PDF generation logic.
 */
@MCRConfigurationProxy(proxyClass = AgreementPdfServiceProvider.Factory.class)
public class AgreementPdfServiceProvider {

    private Map<String, AgreementPdfService> pdfServices;

    /**
     * Creates a new {@code AgreementPdfServiceProvider} with the given map of PDF services.
     *
     * @param pdfServices a map of agreement ids to their corresponding {@link AgreementPdfService}
     */
    public AgreementPdfServiceProvider(Map<String, AgreementPdfService> pdfServices) {
        this.pdfServices = pdfServices;
    }

    /**
     * Retrieves the {@link AgreementPdfService} for the given agreement id, if available.
     *
     * @param agreementId the id of the agreement
     * @return an {@link Optional} containing the matching {@link AgreementPdfService}, or empty if not found
     */
    public Optional<AgreementPdfService> getPdfService(String agreementId) {
        return Optional.ofNullable(pdfServices.get(agreementId));
    }

    /**
     * Factory for creating {@link AgreementPdfServiceProvider} instances with
     * injected dependencies from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<AgreementPdfServiceProvider> {

        /**
         * A map of named {@link AgreementPdfService} instances to be provided
         * by the {@link AgreementPdfServiceProvider}.
         */
        @MCRInstanceMap(name = "Services", valueClass = AgreementPdfService.class, required = true)
        public Map<String, AgreementPdfService> services;

        @Override
        public AgreementPdfServiceProvider get() {
            return new AgreementPdfServiceProvider(services);
        }

    }

}
