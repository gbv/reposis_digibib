package de.vzg.reposis.digibib.agreement.pdf.service;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstanceMap;

@MCRConfigurationProxy(proxyClass = AgreementPdfServiceProvider.Factory.class)
public class AgreementPdfServiceProvider {

    private static final String CLASS_PROP = "Digibib.Agreement.AgreementPdfServiceProvider.Class";

    private Map<String, AgreementPdfService> pdfServices;

    public AgreementPdfServiceProvider(Map<String, AgreementPdfService> pdfServices) {
        this.pdfServices = pdfServices;
    }

    public static AgreementPdfServiceProvider obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    public static AgreementPdfServiceProvider createInstance() {
        return MCRConfiguration2.<AgreementPdfServiceProvider>getInstanceOf(CLASS_PROP).orElseThrow();
    }

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
