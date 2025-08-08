package de.vzg.reposis.digibib.agreement.pdf.service;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstanceMap;

/**
 * Provider class for retrieving {@link AgreementPdfService} instances by agreement name.
 * <p>
 * This class acts as a registry for multiple PDF generation services, allowing
 * different agreement types to have their own PDF generation logic.
 */
@MCRConfigurationProxy(proxyClass = AgreementPdfServiceProvider.Factory.class)
public class AgreementPdfServiceProvider {

    private static final String CLASS_PROP = "Digibib.Agreement.AgreementPdfServiceProvider.Class";

    private Map<String, AgreementPdfService> pdfServices;

    /**
     * Creates a new {@code AgreementPdfServiceProvider} with the given map of PDF services.
     *
     * @param pdfServices a map of agreement names to their corresponding {@link AgreementPdfService}
     */
    public AgreementPdfServiceProvider(Map<String, AgreementPdfService> pdfServices) {
        this.pdfServices = pdfServices;
    }

    /**
     * Returns the shared singleton instance of {@code AgreementPdfServiceProvider}.
     *
     * @return the shared instance
     */
    public static AgreementPdfServiceProvider obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    /**
     * Creates a new instance of {@code AgreementPdfServiceProvider} using configuration.
     *
     * @return a new {@code AgreementPdfServiceProvider} instance
     */
    public static AgreementPdfServiceProvider createInstance() {
        return MCRConfiguration2.<AgreementPdfServiceProvider>getInstanceOf(CLASS_PROP).orElseThrow();
    }

    /**
     * Retrieves the {@link AgreementPdfService} for the given agreement name, if available.
     *
     * @param agreementName the name of the agreement type
     * @return an {@link Optional} containing the matching {@link AgreementPdfService}, or empty if not found
     */
    public Optional<AgreementPdfService> getPdfService(String agreementName) {
        return Optional.ofNullable(pdfServices.get(agreementName));
    }

    public static final class Factory implements Supplier<AgreementPdfServiceProvider> {

        @MCRInstanceMap(name = "Services", valueClass = AgreementPdfService.class, required = true)
        public Map<String, AgreementPdfService> services;

        @Override
        public AgreementPdfServiceProvider get() {
            return new AgreementPdfServiceProvider(services);
        }

    }

    private static final class InstanceHolder {
        private static final AgreementPdfServiceProvider SHARED_INSTANCE = createInstance();
    }
}
