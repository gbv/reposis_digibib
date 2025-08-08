package de.vzg.reposis.digibib.agreement.xml;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import org.jdom2.transform.JDOMSource;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.model.Author;
import de.vzg.reposis.digibib.agreement.service.author.AuthorServiceProvider;

public class AgreementAuthorURIResolver implements URIResolver {

    public final AuthorServiceProvider authorServiceProvider;

    public AgreementAuthorURIResolver() {
        authorServiceProvider = AuthorServiceProvider.obtainInstance();
    }

    public AgreementAuthorURIResolver(AuthorServiceProvider authorServiceProvider) {
        this.authorServiceProvider = authorServiceProvider;
    }

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
