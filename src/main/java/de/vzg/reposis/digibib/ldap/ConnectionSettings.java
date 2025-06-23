package de.vzg.reposis.digibib.ldap;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;

/**
 * LDAP connection configuration including provider URL, authentication, and timeouts.
 *
 * @param providerUrl the LDAP server URL (e.g. ldap://host:389)
 * @param securitySettings authentication configuration
 * @param connectTimeoutMillis connection timeout in milliseconds
 * @param readTimeoutMillis read timeout in milliseconds
 */
public record ConnectionSettings(
    String providerUrl,
    SecuritySettings securitySettings,
    Integer connectTimeoutMillis,
    Integer readTimeoutMillis) {

    public Map<String, String> toLdapSettings() {
        Map<String, String> ldapSettings = new HashMap<>();
        ldapSettings.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapSettings.put(Context.PROVIDER_URL, providerUrl);
        ldapSettings.putAll(securitySettings.toLdapSettings());
        ldapSettings.put("com.sun.jndi.ldap.connect.timeout", readTimeoutMillis.toString());
        ldapSettings.put("com.sun.jndi.ldap.read.timeout", readTimeoutMillis.toString());
        return ldapSettings;
    }

}
