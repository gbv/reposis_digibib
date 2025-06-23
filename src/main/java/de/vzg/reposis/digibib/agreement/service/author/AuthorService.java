package de.vzg.reposis.digibib.agreement.service.author;

import java.util.function.Supplier;

import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstance;

import de.vzg.reposis.digibib.agreement.exceptions.AgreementException;
import de.vzg.reposis.digibib.agreement.model.Author;

/**
 * Service for resolving {@link Author} information based on a given username.
 */
@MCRConfigurationProxy(proxyClass = AuthorService.Factory.class)
public class AuthorService {

    private static final String CONFIG_PREFIX = "Digibib.Agreement.";

    private final AuthorResolver defaultService;
    private final AuthorResolverProvider resolverProvider;

    /**
     * Constructs a new {@link AuthorService}.
     *
     * @param defaultService   the default resolver for usernames without realms (non-email identifiers)
     * @param resolverProvider the provider for resolving authors by e-mail or realm-specific logic
     */
    public AuthorService(AuthorResolver defaultService, AuthorResolverProvider resolverProvider) {
        this.defaultService = defaultService;
        this.resolverProvider = resolverProvider;
    }

    /**
     * Returns the shared singleton instance of {@code AuthorService}.
     *
     * @return the shared {@code AuthorServiceProvider} instance
     */
    public static AuthorService obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    /**
     * Creates a new instance of {@code AuthorService} using MyCoRe configuration.
     *
     * @return a new {@code AuthorServiceProvider} instance
     */
    public static AuthorService createInstance() {
        return MCRConfiguration2.<AuthorService>getInstanceOf(CONFIG_PREFIX + "AuthorService.Class").orElseThrow();
    }

    /**
     * Resolves the {@link Author} information for the given username by delegating
     * to the appropriate {@link AuthorResolver}.
     *
     * @param username the username of the author to resolve
     * @return the resolved {@link Author}
     */
    public Author resolveAuthor(String username) {
        return (username.contains("@")
            ? resolverProvider.resolveAuthor(username)
            : defaultService.getAuthorByUsername(username))
            .orElseThrow(() -> new AgreementException(username));
    }

    /**
     * Factory for creating {@link AuthorService} instances with injected
     * dependencies from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<AuthorService> {

        /**
         * The default {@link AuthorResolver} to use when no specific realm match is found.
         */
        @MCRInstance(name = "DefaultResolver", valueClass = AuthorResolver.class)
        public AuthorResolver defaultResolver;

        /**
         * The {@link AuthorResolver} instance associated with the specified realm.
         */
        @MCRInstance(name = "ResolverProvider", valueClass = AuthorResolverProvider.class)
        public AuthorResolverProvider resolverProvider;

        @Override
        public AuthorService get() {
            return new AuthorService(defaultResolver, resolverProvider);
        }

    }

    private static final class InstanceHolder {
        private static final AuthorService SHARED_INSTANCE = createInstance();
    }
}
