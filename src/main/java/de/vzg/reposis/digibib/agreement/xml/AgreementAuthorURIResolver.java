package de.vzg.reposis.digibib.agreement.xml;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import org.jdom2.transform.JDOMSource;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.model.Author;
import de.vzg.reposis.digibib.agreement.service.author.AuthorServiceProvider;

/**
 * URIResolver implementation to resolve author references in XML transformations.
 */
public class AgreementAuthorURIResolver implements URIResolver {

    private final AuthorServiceProvider authorServiceProvider;

    /**
     * Constructs an instance using the default {@link AuthorServiceProvider}.
     */
    public AgreementAuthorURIResolver() {
        authorServiceProvider = AuthorServiceProvider.obtainInstance();
    }

    /**
     * Constructs an instance using the given {@link AuthorServiceProvider}.
     *
     * @param authorServiceProvider the author service provider to use
     */
    public AgreementAuthorURIResolver(AuthorServiceProvider authorServiceProvider) {
        this.authorServiceProvider = authorServiceProvider;
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
        final Author author = authorServiceProvider.resolveAuthor(username);
        try {
            return new JDOMSource(AgreementXmlMapper.toDocument(author));
        } catch (IOException e) {
            throw new AgreementException("Error while transforming author");
        }
    }

}
