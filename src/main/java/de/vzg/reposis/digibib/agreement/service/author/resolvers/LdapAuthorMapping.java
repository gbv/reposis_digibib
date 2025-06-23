package de.vzg.reposis.digibib.agreement.service.author.resolvers;

import java.util.List;

import org.mycore.common.config.annotation.MCRProperty;

/**
 * Mapping configuration for LDAP attributes to author fields.
 */
public class LdapAuthorMapping {

    /**
     * LDAP attribute name for the author's full name.
     */
    @MCRProperty(name = "Name")
    public String nameId;

    /**
     * LDAP attribute name for the author's email address.
     */
    @MCRProperty(name = "Email")
    public String emailId;

    /**
     * LDAP attribute name for the author's phone number.
     */
    @MCRProperty(name = "Phone")
    public String phoneId;

    /**
     * LDAP attribute name for the author's affiliated institute.
     */
    @MCRProperty(name = "Institute")
    public String institueId;

    /**
     * Default constructor.
     */
    public LdapAuthorMapping() {
    }

    /**
     * Returns a list of all LDAP attribute keys used in this mapping.
     *
     * @return a list of LDAP attribute names
     */
    public List<String> keys() {
        return List.of(nameId, emailId, phoneId, institueId);
    }
}
