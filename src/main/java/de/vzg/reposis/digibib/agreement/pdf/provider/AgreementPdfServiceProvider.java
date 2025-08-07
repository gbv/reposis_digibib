package de.vzg.reposis.digibib.agreement.pdf.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mycore.common.config.MCRConfiguration2;

import de.vzg.reposis.digibib.agreement.pdf.service.AgreementPdfService;

public class AgreementPdfServiceProvider {

    private static final String CREATOR_PROP_PREF = "Digibib.Agreement.PdfCreator.";

    private final Map<String, AgreementPdfService> cache = new ConcurrentHashMap<>();

    public AgreementPdfService getPdfService(String agreementName) {
        return cache.computeIfAbsent(agreementName, a -> createCreatorFor(a));
    }

    private static AgreementPdfService createCreatorFor(String agreementName) {
        return MCRConfiguration2.<AgreementPdfService>getInstanceOf(CREATOR_PROP_PREF + agreementName + ".Class")
            .orElseThrow();
    }

}
