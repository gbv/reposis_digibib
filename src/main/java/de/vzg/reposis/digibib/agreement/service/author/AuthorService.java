package de.vzg.reposis.digibib.agreement.service.author;

import de.vzg.reposis.digibib.agreement.model.Author;

public interface AuthorService {

    Author getAuthorByUsername(String username);
}
