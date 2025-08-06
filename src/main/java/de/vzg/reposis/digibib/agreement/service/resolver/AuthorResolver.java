package de.vzg.reposis.digibib.agreement.service.resolver;

import java.util.Optional;

import de.vzg.reposis.digibib.agreement.model.Author;

public interface AuthorResolver {

    Optional<Author> resolveAuthor(String username);
}
