package de.vzg.reposis.digibib.agreement.service.author;

import org.mycore.common.config.annotation.MCRInstance;
import org.mycore.common.config.annotation.MCRProperty;

/**
 * Represents a configuration entry for an {@link AuthorResolver} within a
 * specific realm.
 */
public class AuthorResolverEntry {

    /**
     * The realm or context for which this {@link AuthorResolver} is applicable.
     */
    @MCRProperty(name = "Realm")
    public String realm;

    /**
     * The {@link AuthorResolver} instance associated with the specified realm.
     */
    @MCRInstance(name = "Resolver", valueClass = AuthorResolver.class)
    public AuthorResolver resolver;

    /**
     * Default constructor.
     */
    public AuthorResolverEntry() {
    }
}
