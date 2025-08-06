package de.vzg.reposis.digibib.agreement.xml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import org.jdom2.transform.JDOMSource;
import org.mycore.user2.MCRUser;
import org.mycore.user2.MCRUserManager;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.model.Author;
import de.vzg.reposis.digibib.agreement.service.resolver.AuthorResolver;

public class AgreementAuthorURIResolver implements URIResolver {

    public final Map<String, AuthorResolver> resolverMap;

    public AgreementAuthorURIResolver() {
        resolverMap = new HashMap<>();
    }

    //Digibib.Agreement.Author.Resolver.bla.Class=Blub
    //Digibib.Agreement.Author.Resolver.bla.Realm=@dm
    public AgreementAuthorURIResolver(Map<String, AuthorResolver> resolverMap) {
        this.resolverMap = resolverMap;
    }

    // TODO
    @Override
    public Source resolve(String href, String base) throws TransformerException {
        final String username = href.substring(href.indexOf(':') + 1);
        Author author;
        if (!resolverMap.isEmpty() && username.contains("@")) {
            final String realm = getRealm(username);
            if (resolverMap.containsKey(realm)) {
                author = resolverMap.get(realm).resolveAuthor(username).orElseThrow();
            }
            throw new IllegalArgumentException();
        } else {
            author = resolveLocalAuthor(username);
        }
        try {
            return new JDOMSource(AgreementXmlMapper.toDocument(author));
        } catch (IOException e) {
            throw new AgreementException("Error while transforming author");
        }
    }

    private String getRealm(String username) {
        final String[] parts = username.split("@");
        return parts[1];
    }

    protected Author resolveLocalAuthor(String username) {
        final MCRUser user = MCRUserManager.getUser(username);
        if (user == null) {
            throw new AgreementException("User '" + username + "' does not exist");
        }
        return new Author.Builder()
            .name(user.getRealName())
            .email(user.getEMailAddress())
            .build();
    }

}
