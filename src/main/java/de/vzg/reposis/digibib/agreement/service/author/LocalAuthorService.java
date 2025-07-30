package de.vzg.reposis.digibib.agreement.service.author;

import org.mycore.user2.MCRUser;
import org.mycore.user2.MCRUserManager;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.model.Author;

/**
 * Local implementation of {@link AuthorService} that retrieves author information
 * from the local MyCoRe user management system.
 * <p>
 * This service queries {@link MCRUserManager} for the given username, and if found,
 * maps the user's real name and email address to an {@link Author} object.
 * <p>
 * If the user cannot be found, an {@link AgreementException} is thrown.
 */
public class LocalAuthorService implements AuthorService {

    @Override
    public Author getAuthorByUsername(String username) {
        final MCRUser user = MCRUserManager.getUser(username);
        if (user == null) {
            throw new AgreementException("User '" + username + "' does not exist");
        }
        return new Author.Builder()
            .name(user.getRealName())
            .email(user.getEMailAddress())
            .build();
    }
}
