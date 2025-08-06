package de.vzg.reposis.digibib.agreement.pdf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mycore.common.config.MCRConfiguration2;

public class AgreementPdfCreatorFactory {

    private static final String CREATOR_PROP_PREF = "Digibib.Agreement.PdfCreator.";

    private final Map<String, AgreementPdfCreator> cache = new ConcurrentHashMap<>();

    public AgreementPdfCreator getCreatorFor(String agreement) {
        return cache.computeIfAbsent(agreement, a -> createCreatorFor(a));
    }

    private static AgreementPdfCreator createCreatorFor(String agreement) {
        return MCRConfiguration2.<AgreementPdfCreator>getInstanceOf(CREATOR_PROP_PREF + agreement + ".Class")
            .orElseThrow();
    }

}
