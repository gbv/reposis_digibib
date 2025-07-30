package de.vzg.reposis.digibib.agreement.service.author;

import org.mycore.common.config.annotation.MCRInstance;
import org.mycore.common.config.annotation.MCRProperty;

/**
 * Represents a configuration entry for an {@link AuthorService} within a
 * specific realm.
 */
public class AuthorServiceEntry {

    /**
     * The realm or context for which this {@link AuthorService} is applicable.
     */
    @MCRProperty(name = "Realm")
    public String realm;

    /**
     * The {@link AuthorService} instance associated with the specified realm.
     */
    @MCRInstance(name = "Service", valueClass = AuthorService.class)
    public AuthorService service;

    /**
     * Default constructor.
     */
    public AuthorServiceEntry() {
    }
}
