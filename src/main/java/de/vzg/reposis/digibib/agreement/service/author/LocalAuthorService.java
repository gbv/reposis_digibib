package de.vzg.reposis.digibib.agreement.service.author;

import org.mycore.user2.MCRUser;
import org.mycore.user2.MCRUserManager;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.model.Author;

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
