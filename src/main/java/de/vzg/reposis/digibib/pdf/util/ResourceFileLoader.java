package de.vzg.reposis.digibib.pdf.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRProperty;

/**
 * Loader for retrieving a PDF form template from the application's resources.
 * <p>
 * The template is read as a byte array from the classpath using the specified resource path.
 */
@MCRConfigurationProxy(proxyClass = ResourceFileLoader.Factory.class)
public class ResourceFileLoader {

    private final String resourcePath;

    /**
     * Creates a new {@code ResourceFileLoader} with the given resource path.
     *
     * @param resourcePath the classpath resource path to the PDF template
     */
    public ResourceFileLoader(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * Loads the PDF template from the configured resource path.
     *
     * @return the template file as a byte array
     * @throws IOException if an I/O error occurs while reading the template
     */
    public byte[] load() throws IOException {
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return in.readAllBytes();
        }
    }

    /**
     * Factory for creating {@link ResourceFileLoader} instances with
     * injected dependencies from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<ResourceFileLoader> {

        /**
         * The path to the PDF template file used by the loader.
         */
        @MCRProperty(name = "File")
        public String file;

        @Override
        public ResourceFileLoader get() {
            return new ResourceFileLoader(file);
        }

    }
}
