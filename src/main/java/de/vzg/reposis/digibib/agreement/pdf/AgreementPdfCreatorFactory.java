package de.vzg.reposis.digibib.agreement.pdf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mycore.common.config.MCRConfiguration2;

import de.vzg.reposis.digibib.agreement.model.Agreement;

public class AgreementPdfCreatorFactory {

    private static final String CREATOR_PROP_PREF = "Digibib.Agreement.PdfCreator.";

    private final Map<Agreement, AgreementPdfCreator> cache = new ConcurrentHashMap<>();

    public AgreementPdfCreator getCreatorFor(Agreement agreement) {
        return cache.computeIfAbsent(agreement, a -> createCreatorFor(a));
    }

    private static AgreementPdfCreator createCreatorFor(Agreement agreement) {
        final Map<String, String> properties = MCRConfiguration2.getSubPropertiesMap(CREATOR_PROP_PREF);
        final String filename = properties.get(".FormFillerClass");
        final AgreementPdfFormFiller mapper
            = MCRConfiguration2.<AgreementPdfFormFiller>getInstanceOf(properties.get(".TemplateFilePath"))
                .orElseThrow();
        return new AgreementPdfCreator(mapper, filename);
    }

}
