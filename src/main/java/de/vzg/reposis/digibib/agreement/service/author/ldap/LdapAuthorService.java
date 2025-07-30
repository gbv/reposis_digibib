package de.vzg.reposis.digibib.agreement.service.author.ldap;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRInstance;
import org.mycore.common.config.annotation.MCRProperty;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.ldap.LdapClient;
import de.vzg.reposis.digibib.agreement.model.Author;
import de.vzg.reposis.digibib.agreement.service.author.AuthorService;

/**
 * Service for retrieving {@link Author} information from an LDAP directory.
 * <p>
 * Uses {@link LdapClient} to query LDAP by filter and map returned attributes
 * to the fields of an {@code Author} object according to a configured
 * {@link LdapAuthorMapping}.
 */
@MCRConfigurationProxy(proxyClass = LdapAuthorService.Factory.class)
public class LdapAuthorService implements AuthorService {

    private final LdapClient ldapClient;

    private final String baseDn;

    private final String uidFilter;

    private final LdapAuthorMapping mapping;

    /**
     * Creates a new {@code LdapAuthorService} with the specified LDAP client,
     * search configuration, and attribute mapping.
     *
     * @param ldapClient the {@link LdapClient} used to execute LDAP searches
     * @param baseDn the Base DN (distinguished name) to use as the starting point for searches
     * @param uidFilter the LDAP search filter pattern for the username, where {@code %s} is replaced by the actual username
     * @param mapping the mapping configuration that links LDAP attribute names to {@link Author} fields
     */
    public LdapAuthorService(LdapClient ldapClient, String baseDn, String uidFilter, LdapAuthorMapping mapping) {
        this.ldapClient = ldapClient;
        this.baseDn = baseDn;
        this.uidFilter = uidFilter;
        this.mapping = mapping;
    }

    @Override
    public Author getAuthorByUsername(String username) {
        final String filter = String.format(Locale.ROOT, uidFilter, username);
        final Map<String, Map<String, List<String>>> attributeMap
            = ldapClient.searchUserAttributes(baseDn, filter, mapping.keys());
        if (attributeMap.size() != 1) {
            throw new AgreementException("Username " + username + " does not exist or is not unique");
        }
        return mapFromAttributes(attributeMap.values().iterator().next());

    }

    private Author mapFromAttributes(Map<String, List<String>> attributes) {
        final Author author = new Author();
        author.setName(getFirstValue(attributes, mapping.nameId));
        author.setEmail(getFirstValue(attributes, mapping.emailId));
        author.setPhone(getFirstValue(attributes, mapping.phoneId));
        author.setInstitute(getFirstValue(attributes, mapping.institueId));
        return author;
    }

    private String getFirstValue(Map<String, List<String>> attributes, String key) {
        return Optional.ofNullable(attributes.get(key))
            .filter(list -> !list.isEmpty())
            .map(list -> list.get(0))
            .orElse(null);
    }

    /**
     * Factory for creating {@link LdapAuthorService} instances with injected
     * dependencies from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<LdapAuthorService> {

        /**
         * The LDAP client used to perform searches in the directory.
         */
        @MCRInstance(name = "LdapClient", valueClass = LdapClient.class)
        public LdapClient ldapClient;

        /**
         * Base distinguished name (DN) used as the starting point for LDAP searches.
         */
        @MCRProperty(name = "BaseDn")
        public String baseDn;

        /**
         * LDAP filter pattern for searching a user by username.
         * Use `%s` as a placeholder for the actual username.
         */
        @MCRProperty(name = "UidFilter")
        public String uidFilter;

        /**
         * Mapping configuration linking LDAP attribute names to {@link Author} fields.
         */
        @MCRInstance(name = "AuthorMapping", valueClass = LdapAuthorMapping.class)
        public LdapAuthorMapping mapping;

        @Override
        public LdapAuthorService get() {
            return new LdapAuthorService(ldapClient, baseDn, uidFilter, mapping);
        }

    }

}
