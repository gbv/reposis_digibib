package de.vzg.reposis.digibib.agreement.service.author;

import java.util.Map;

import de.vzg.reposis.digibib.agreement.model.Author;

public class AuthorServiceProvider {

    private final Map<String, AuthorService> servicesByRealm;

    private final AuthorService defaultService;

    //Digibib.Agreement.Author.Resolver.bla.Class=Blub
    //Digibib.Agreement.Author.Resolver.bla.Realm=@dm
    public AuthorServiceProvider(AuthorService defaultService, Map<String, AuthorService> servicesByRealm) {
        this.servicesByRealm = servicesByRealm;
        this.defaultService = defaultService;
    }

    public AuthorService getServiceForUsername(String username) {
        if (username.contains("@")) {
            final String realm = getRealm(username);
            if (servicesByRealm.containsKey(realm)) {
                servicesByRealm.get(realm);
            }
        }
        return defaultService;
    }

    public Author resolveAuthor(String username) {
        return getServiceForUsername(username).getAuthorByUsername(username);
    }

    private String getRealm(String username) {
        final String[] parts = username.split("@");
        return parts[1];
    }
}
