package de.vzg.reposis.digibib.agreement.pdf.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRProperty;

@MCRConfigurationProxy(proxyClass = AgreementPdfTemplateLoader.Factory.class)
public class AgreementPdfTemplateLoader {

    private final String resourcePath;

    public AgreementPdfTemplateLoader(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public byte[] loadTemplate() throws IOException {
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("Template resource not found: " + resourcePath);
            }
            return in.readAllBytes();
        }
    }

    public static final class Factory implements Supplier<AgreementPdfTemplateLoader> {

        @MCRProperty(name = "File")
        public String file;

        @Override
        public AgreementPdfTemplateLoader get() {
            return new AgreementPdfTemplateLoader(file);
        }

    }
}
