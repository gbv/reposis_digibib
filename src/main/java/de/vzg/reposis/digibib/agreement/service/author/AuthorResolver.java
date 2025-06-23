package de.vzg.reposis.digibib.agreement.service.author;

import java.util.Optional;

import de.vzg.reposis.digibib.agreement.model.Author;

/**
 * Service interface for retrieving author information based on username.
 * <p>
 * Implementations of this interface are responsible for fetching {@link Author} details,
 * for example from an LDAP directory, a database, or any other data source.
 */
public interface AuthorResolver {

    /**
     * Retrieves the {@link Author} information for the given username.
     *
     * @param username the username identifying the author
     * @return the {@link Author} associated with the username
     */
    Optional<Author> getAuthorByUsername(String username);

}
