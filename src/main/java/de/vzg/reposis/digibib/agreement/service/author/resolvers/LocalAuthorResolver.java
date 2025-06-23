package de.vzg.reposis.digibib.agreement.service.author.resolvers;

import java.util.Optional;

import org.mycore.user2.MCRUser;
import org.mycore.user2.MCRUserManager;

import de.vzg.reposis.digibib.agreement.exceptions.AgreementException;
import de.vzg.reposis.digibib.agreement.model.Author;
import de.vzg.reposis.digibib.agreement.service.author.AuthorResolver;

/**
 * Local implementation of {@link AuthorResolver} that retrieves author information
 * from the local MyCoRe user management system.
 * <p>
 * This service queries {@link MCRUserManager} for the given username, and if found,
 * maps the user's real name and email address to an {@link Author} object.
 * <p>
 * If the user cannot be found, an {@link AgreementException} is thrown.
 */
public class LocalAuthorResolver implements AuthorResolver {

    @Override
    public Optional<Author> getAuthorByUsername(String username) {
        final MCRUser user = MCRUserManager.getUser(username);
        if (user != null) {
            final Author author = new Author.Builder()
                .name(user.getRealName())
                .email(user.getEMailAddress())
                .build();
            return Optional.of(author);
        }
        return Optional.empty();
    }
}
