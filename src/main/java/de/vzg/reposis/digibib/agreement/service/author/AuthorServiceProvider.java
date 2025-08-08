package de.vzg.reposis.digibib.agreement.service.author;

import java.util.Map;
import java.util.function.Supplier;

import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstance;
import org.mycore.common.config.annotation.MCRInstanceMap;

import de.vzg.reposis.digibib.agreement.model.Author;

@MCRConfigurationProxy(proxyClass = AuthorServiceProvider.Factory.class)
public class AuthorServiceProvider {

    private static final String PROVIDER_CLASS = "Digibib.Agreement.AuthorServiceProvider.Class";

    private final Map<String, AuthorService> servicesByRealm;

    private final AuthorService defaultService;

    public AuthorServiceProvider(AuthorService defaultService, Map<String, AuthorService> servicesByRealm) {
        this.servicesByRealm = servicesByRealm;
        this.defaultService = defaultService;
    }

    public static AuthorServiceProvider obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    public static AuthorServiceProvider createInstance() {
        return MCRConfiguration2.<AuthorServiceProvider>getSingleInstanceOf(PROVIDER_CLASS).orElseThrow();
    }

    public AuthorService getServiceForUsername(String username) {
        if (servicesByRealm.size() > 0 && username.contains("@")) {
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

    public static final class Factory implements Supplier<AuthorServiceProvider> {

        @MCRInstanceMap(name = "Services", valueClass = AuthorService.class, required = false)
        public Map<String, AuthorService> servicesByRealm;

        @MCRInstance(name = "DefaultService", valueClass = AuthorService.class)
        public AuthorService defaultService;

        @Override
        public AuthorServiceProvider get() {
            return new AuthorServiceProvider(defaultService, servicesByRealm);
        }

    }

    private static final class InstanceHolder {
        private static final AuthorServiceProvider SHARED_INSTANCE = createInstance();
    }
}
