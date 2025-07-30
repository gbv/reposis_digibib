package de.vzg.reposis.digibib.agreement.service.author;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    private static final String CONFIG_PREFIX = "Digibib.Agreement.";

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
        return MCRConfiguration2
            .<AuthorServiceProvider>getInstanceOf(CONFIG_PREFIX + "AuthorServiceProvider.Class")
            .orElseThrow();
    }

    /**
     * Resolves the {@link Author} information for the given username by delegating
     * to the appropriate {@link AuthorService}.
     *
     * @param username the username of the author to resolve
     * @return the resolved {@link Author}
     */
    public Author resolveAuthor(String username) {
        if (servicesByRealm.size() > 0 && username.contains("@")) {
            final String[] parts = username.split("@");
            final String userId = parts[0];
            final String realm = parts[1];
            if (servicesByRealm.containsKey(realm)) {
                return servicesByRealm.get(realm).getAuthorByUsername(userId);
            }
        }
        return defaultService.getAuthorByUsername(username);
    }

    /**
     * Factory for creating {@link AuthorServiceProvider} instances with injected
     * dependencies from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<AuthorServiceProvider> {

        /**
         * Map of {@link AuthorServiceEntry} instances keyed by service name.
         * Each entry contains a realm and the corresponding {@link AuthorService}.
         * This map is optional and may be {@code null} or empty.
         */
        @MCRInstanceMap(name = "Services", valueClass = AuthorServiceEntry.class, required = false)
        public Map<String, AuthorServiceEntry> services;

        /**
         * The default {@link AuthorService} to use when no specific realm match is found.
         */
        @MCRInstance(name = "DefaultService", valueClass = AuthorService.class)
        public AuthorService defaultService;

        @Override
        public AuthorServiceProvider get() {
            final Map<String, AuthorService> servicesByRealm = services.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getValue().realm, entry -> entry.getValue().service));
            return new AuthorServiceProvider(defaultService, servicesByRealm);
        }

    }

    private static final class InstanceHolder {
        private static final AuthorServiceProvider SHARED_INSTANCE = createInstance();
    }
}
