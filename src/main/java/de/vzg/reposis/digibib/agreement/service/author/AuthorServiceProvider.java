package de.vzg.reposis.digibib.agreement.service.author;

import java.util.Map;
import java.util.function.Supplier;

import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstance;
import org.mycore.common.config.annotation.MCRInstanceMap;

import de.vzg.reposis.digibib.agreement.model.Author;

/**
 * Provider class that manages multiple {@link AuthorService} instances for different realms,
 * and delegates author resolution requests to the appropriate service based on the username.
 * <p>
 * The provider supports a default service and optionally realm-specific services keyed by realm name.
 * Usernames containing a realm (e.g., email-style "user@realm") are resolved using the service
 * registered for that realm, if available.
 */
@MCRConfigurationProxy(proxyClass = AuthorServiceProvider.Factory.class)
public class AuthorServiceProvider {

    private static final String PROVIDER_CLASS = "Digibib.Agreement.AuthorServiceProvider.Class";

    private final Map<String, AuthorService> servicesByRealm;

    private final AuthorService defaultService;

    /**
     * Constructs an {@code AuthorServiceProvider} with a default service and
     * an optional map of realm-specific services.
     *
     * @param defaultService the default {@link AuthorService} to use if no realm-specific service matches
     * @param servicesByRealm map of realm names to {@link AuthorService} instances
     */
    public AuthorServiceProvider(AuthorService defaultService, Map<String, AuthorService> servicesByRealm) {
        this.servicesByRealm = servicesByRealm;
        this.defaultService = defaultService;
    }

    /**
     * Returns the shared singleton instance of {@code AuthorServiceProvider}.
     *
     * @return the shared {@code AuthorServiceProvider} instance
     */
    public static AuthorServiceProvider obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    /**
     * Creates a new instance of {@code AuthorServiceProvider} using MyCoRe configuration.
     *
     * @return a new {@code AuthorServiceProvider} instance
     */
    public static AuthorServiceProvider createInstance() {
        return MCRConfiguration2.<AuthorServiceProvider>getSingleInstanceOf(PROVIDER_CLASS).orElseThrow();
    }

    /**
     * Retrieves the appropriate {@link AuthorService} for the given username.
     * <p>
     * If the username contains a realm (e.g., "user@realm") and a service is registered
     * for that realm, that service is returned. Otherwise, the default service is returned.
     *
     * @param username the username for which to find the corresponding {@link AuthorService}
     * @return the matching {@link AuthorService}, or the default if none matches
     */
    public AuthorService getServiceForUsername(String username) {
        if (servicesByRealm.size() > 0 && username.contains("@")) {
            final String realm = getRealm(username);
            if (servicesByRealm.containsKey(realm)) {
                servicesByRealm.get(realm);
            }
        }
        return defaultService;
    }

    /**
     * Resolves the {@link Author} information for the given username by delegating
     * to the appropriate {@link AuthorService}.
     *
     * @param username the username of the author to resolve
     * @return the resolved {@link Author}
     */
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
