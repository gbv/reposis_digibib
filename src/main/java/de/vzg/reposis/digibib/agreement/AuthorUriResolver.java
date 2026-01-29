package de.vzg.reposis.digibib.agreement;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.transform.JDOMSource;

import de.vzg.reposis.digibib.agreement.exceptions.AgreementException;
import de.vzg.reposis.digibib.agreement.model.Author;
import de.vzg.reposis.digibib.agreement.serialization.AuthorXmlMapper;
import de.vzg.reposis.digibib.agreement.service.author.AuthorResolverProvider;
import de.vzg.reposis.digibib.agreement.service.author.AuthorService;

/**
 * URIResolver implementation to resolve author references in XML transformations.
 */
public class AuthorUriResolver implements URIResolver {

    private static final Logger LOGGER = LogManager.getLogger();

    private final AuthorService authorService;

    /**
     * Constructs an instance using the default {@link AuthorResolverProvider}.
     */
    public AuthorUriResolver() {
        authorService = AuthorService.obtainInstance();
    }

    /**
     * Constructs an instance using the given {@link AuthorResolverProvider}.
     *
     * @param authorService the author service to use
     */
    public AuthorUriResolver(AuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * Resolves the given href URI to a {@link Source} representing the referenced author.
     * <p>
     * Syntax: <code>agreementauthor:{username}</code>
     *
     * @param href the URI to resolve, containing the author identifier
     * @param base the base URI against which the href is resolved (ignored)
     * @return a {@link Source} containing the XML representation of the resolved author
     * @throws TransformerException if an error occurs during transformation or author resolution
     * @see javax.xml.transform.URIResolver
     */
    @Override
    public Source resolve(String href, String base) throws TransformerException {
        final String username = href.substring(href.indexOf(':') + 1);
        LOGGER.debug("Resolving username '{}'", username);
        if (username.isBlank()) {
            throw new IllegalArgumentException("An username is required.");
        }
        final Author author = authorService.resolveAuthor(username);
        try {
            return new JDOMSource(AuthorXmlMapper.toDocument(author).detachRootElement());
        } catch (IOException e) {
            throw new AgreementException("Error while transforming author.");
        }
    }

}
