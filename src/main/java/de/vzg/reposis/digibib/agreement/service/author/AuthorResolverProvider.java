package de.vzg.reposis.digibib.agreement.service.author;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstanceMap;

import de.vzg.reposis.digibib.agreement.model.Author;

/**
 * Provider class that manages multiple {@link AuthorResolver} instances for different realms,
 * and delegates author resolution requests to the appropriate service based on the username.
 * <p>
 * The provider supports a default service and optionally realm-specific services keyed by realm name.
 * Usernames containing a realm (e.g., email-style "user@realm") are resolved using the service
 * registered for that realm, if available.
 */
@MCRConfigurationProxy(proxyClass = AuthorResolverProvider.Factory.class)
public class AuthorResolverProvider {

    private final Map<String, AuthorResolver> servicesByRealm;

    /**
     * Constructs an {@code AuthorServiceProvider} with a default service and
     * an optional map of realm-specific services.
     *
     * @param servicesByRealm map of realm names to {@link AuthorResolver} instances
     */
    public AuthorResolverProvider(Map<String, AuthorResolver> servicesByRealm) {
        this.servicesByRealm = servicesByRealm;
    }

    /**
     * Resolves the {@link Author} information for the given username by delegating
     * to the appropriate {@link AuthorResolver}.
     *
     * @param username the username of the author to resolve
     * @return the resolved {@link Author}
     */
    public Optional<Author> resolveAuthor(String username) {
        final String[] parts = username.split("@");
        if (username.contains("@")) {
            final String userId = parts[0];
            final String realm = parts[1];
            if (realm != null && servicesByRealm.containsKey(realm)) {
                return servicesByRealm.get(realm).getAuthorByUsername(userId);
            }
        }
        return Optional.empty();
    }

    /**
     * Factory for creating {@link AuthorResolverProvider} instances with injected
     * dependencies from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<AuthorResolverProvider> {

        /**
         * Map of {@link AuthorResolverEntry} instances keyed by service name.
         * Each entry contains a realm and the corresponding {@link AuthorResolver}.
         * This map is optional and may be {@code null} or empty.
         */
        @MCRInstanceMap(name = "Services", valueClass = AuthorResolverEntry.class, required = false)
        public Map<String, AuthorResolverEntry> services;

        @Override
        public AuthorResolverProvider get() {
            final Map<String, AuthorResolver> servicesByRealm = services.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getValue().realm, entry -> entry.getValue().resolver));
            return new AuthorResolverProvider(servicesByRealm);
        }

    }
}
