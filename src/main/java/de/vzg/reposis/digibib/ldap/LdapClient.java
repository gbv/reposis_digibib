package de.vzg.reposis.digibib.ldap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.MCRUsageException;
import org.mycore.common.config.MCRConfigurationException;
import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRProperty;

/**
 * LDAP client for searching and retrieving user attributes from an LDAP server.
 * <p>
 * Encapsulates connection, authentication, and search settings, and provides
 * convenience methods to perform user searches and return results as a map of
 * attribute IDs to values.
 */
@MCRConfigurationProxy(proxyClass = LdapClient.Factory.class)
public class LdapClient {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Hashtable<String, String> ldapSettings;

    /**
     * Creates a new {@code LdapClient} from typed connection and search settings.
     *
     * @param connectionSettings LDAP connection configuration (URL, authentication, timeouts, etc.)
     */
    public LdapClient(ConnectionSettings connectionSettings) {
        this(connectionSettings.toLdapSettings());
    }

    /**
     * Creates a new {@code LdapClient} from raw connection settings map and search settings.
     *
     * @param ldapSettings LDAP connection settings map (JNDI environment keys and values)
     */
    public LdapClient(Map<String, String> ldapSettings) {
        this.ldapSettings = new Hashtable<>(ldapSettings);
    }

    /**
     * Searches LDAP using the specified base DN and filter, returning all attributes of matching entries.
     *
     * @param baseDn the base distinguished name (DN) to start the search from
     * @param filter the LDAP search filter string
     * @return a map of attribute IDs to lists of their corresponding values
     */
    public Map<String, Map<String, List<String>>> searchUserAttributesPerUser(String baseDn, String filter) {
        return searchUserAttributes(baseDn, filter, Collections.emptyList());
    }

    /**
     * Searches LDAP using the specified base DN and filter, returning only the requested attributes of matching entries.
     *
     * @param baseDn the base distinguished name (DN) to start the search from
     * @param filter the LDAP search filter string
     * @param wantedAttributes a list of attribute IDs to retrieve; if empty, all attributes are returned
     * @return a map of attribute IDs to lists of their corresponding values
     * @throws MCRConfigurationException if the specified LDAP base DN does not exist
     * @throws MCRUsageException if an error occurs while accessing the LDAP server
     */
    public Map<String, Map<String, List<String>>> searchUserAttributes(
        String baseDn, String filter, List<String> wantedAttributes) {

        final Map<String, Map<String, List<String>>> userAttributeMap = new HashMap<>();
        DirContext ldapContext = null;
        try {
            ldapContext = new InitialDirContext(ldapSettings);
            final SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            if (!wantedAttributes.isEmpty()) {
                controls.setReturningAttributes(wantedAttributes.toArray(new String[0]));
            }

            final NamingEnumeration<SearchResult> results = ldapContext.search(baseDn, filter, controls);
            while (results.hasMore()) {
                final SearchResult result = results.next();
                final Attributes attrs = result.getAttributes();
                final Map<String, List<String>> attributeMap = new HashMap<>();

                final NamingEnumeration<String> ids = attrs.getIDs();
                while (ids.hasMore()) {
                    final String id = ids.next();
                    final Attribute attr = attrs.get(id);

                    final List<String> values = new ArrayList<>();
                    final NamingEnumeration<?> all = attr.getAll();
                    while (all.hasMore()) {
                        values.add(all.next().toString());
                    }
                    attributeMap.put(id, values);
                }

                userAttributeMap.put(result.getNameInNamespace(), attributeMap);
            }
        } catch (NameNotFoundException e) {
            throw new MCRConfigurationException("LDAP base name not found: "
                + e.getMessage() + " " + e.getExplanation(), e);
        } catch (NamingException e) {
            throw new MCRUsageException("Exception accessing LDAP server", e);
        } finally {
            try {
                if (ldapContext != null) {
                    ldapContext.close();
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to close LDAP context", e);
            }
        }

        return userAttributeMap;
    }

    /**
     * Factory for creating {@link LdapClient} instances from configuration properties.
     */
    public static final class Factory implements Supplier<LdapClient> {

        /**
         * The URL of the LDAP provider.
         */
        @MCRProperty(name = "ProviderUrl")
        public String providerUrl;

        /**
         * The authentication type for the connection.
         */
        @MCRProperty(name = "SecurityAuthentication")
        public String securityAuthentication;

        /**
         * The security protocol to use.
         */
        @MCRProperty(name = "SecurityProtocol", required = false)
        public String securityProtocol;

        /**
         * The security principal (username) for authentication.
         */
        @MCRProperty(name = "SecurityPrincipal", required = false)
        public String securityPrincipal;

        /**
         * The security credentials (password) for authentication.
         */
        @MCRProperty(name = "SecurityCredentials", required = false)
        public String securityCredentials;

        /**
         * Connection timeout in milliseconds.
         */
        @MCRProperty(name = "ConnectTimeout")
        public String connectTimeoutMillis;

        /**
         * Read timeout in milliseconds.
         */
        @MCRProperty(name = "ReadTimeout")
        public String readTimeoutMillis;

        @Override
        public LdapClient get() {

            final ConnectionSettings connectionSettings = new ConnectionSettings(
                providerUrl,
                switch (getAuthentication()) {
                    case NONE -> new SecuritySettings.None(getProtocol());
                    case EXTERNAL -> new SecuritySettings.External();
                    case SIMPLE -> new SecuritySettings.Simple(getProtocol(), securityPrincipal, securityCredentials);
                },
                Integer.parseInt(connectTimeoutMillis),
                Integer.parseInt(readTimeoutMillis));
            return new LdapClient(connectionSettings);
        }

        private SecuritySettings.Authentication getAuthentication() {
            return SecuritySettings.Authentication.valueOf(securityAuthentication.toUpperCase(Locale.ROOT));
        }

        private SecuritySettings.Protocol getProtocol() {
            return SecuritySettings.Protocol.valueOf(securityProtocol);
        }
    }
}
