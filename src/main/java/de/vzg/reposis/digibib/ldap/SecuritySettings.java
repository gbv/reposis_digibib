package de.vzg.reposis.digibib.ldap;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;

/**
 * Defines different LDAP security (authentication) configurations.
 * Implementations return a map of JNDI environment properties for authentication.
 */
public interface SecuritySettings {

    /**
     * Converts the security settings into a map of LDAP context properties.
     *
     * @return a map of LDAP context properties
     */
    Map<String, String> toLdapSettings();

    /**
     * Enumeration of supported LDAP authentication types.
     */
    enum Authentication {
        /**
         * No authentication (anonymous bind).
         */
        NONE,
        /**
         * External SASL authentication.
         */
        EXTERNAL,
        /**
         * Simple authentication (username/password).
         */
        SIMPLE;
    }

    /**
     * Enumeration of supported security protocols.
     */
    enum Protocol {
        /**
         * Clear text (plain) protocol.
         */
        PLAIN,
        /**
         * Secure SSL protocol.
         */
        SSL;
    }

    /**
     * No authentication (anonymous)
     *
     * @param protocol the protocol to use for the connection
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4513#section-5.1.1">RFC 4513 / 5.1.1</a>
     */
    record None(Protocol protocol) implements SecuritySettings {

        @Override
        public Map<String, String> toLdapSettings() {
            Map<String, String> ldapSettings = new HashMap<>();
            ldapSettings.put(Context.SECURITY_AUTHENTICATION, "none");
            ldapSettings.put(Context.SECURITY_PROTOCOL, protocol.name().toLowerCase(Locale.ROOT));
            return ldapSettings;
        }

    }

    /**
     * External SASL authentication
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4513#section-5.2.3">RFC 4513 / 5.2.3</a>
     */
    record External() implements SecuritySettings {

        @Override
        public Map<String, String> toLdapSettings() {
            Map<String, String> ldapSettings = new HashMap<>();
            ldapSettings.put(Context.SECURITY_AUTHENTICATION, "EXTERNAL");
            ldapSettings.put(Context.SECURITY_PROTOCOL, "ssl");
            return ldapSettings;
        }

    }

    /**
     * Weak authentication (clear-text password).
     *
     * @param protocol the security protocol to use (PLAIN or SSL)
     * @param principal the username or distinguished name (DN)
     * @param credentials the password
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4513#section-5.1.3">RFC 4513 / 5.1.3</a>
     */
    record Simple(Protocol protocol, String principal, String credentials) implements SecuritySettings {

        @Override
        public Map<String, String> toLdapSettings() {
            Map<String, String> ldapSettings = new HashMap<>();
            ldapSettings.put(Context.SECURITY_AUTHENTICATION, "simple");
            ldapSettings.put(Context.SECURITY_PROTOCOL, protocol.name().toLowerCase(Locale.ROOT));
            ldapSettings.put(Context.SECURITY_PRINCIPAL, principal);
            ldapSettings.put(Context.SECURITY_CREDENTIALS, credentials);
            return ldapSettings;
        }

    }

}
