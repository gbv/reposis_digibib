/*
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * MyCoRe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyCoRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyCoRe.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.vzg.reposis.digibib.contactrequest.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;
import org.mycore.datamodel.metadata.MCRObject;
import org.mycore.mods.MCRMODSWrapper;
import org.mycore.orcid2.client.MCRORCIDCredential;
import org.mycore.orcid2.user.MCRORCIDUser;
import org.mycore.orcid2.user.MCRORCIDUserUtils;
import org.mycore.orcid2.v3.client.MCRORCIDClientHelper;
import org.mycore.orcid2.v3.client.MCRORCIDSectionImpl;
import org.orcid.jaxb.model.v3.release.record.Email;
import org.orcid.jaxb.model.v3.release.record.Emails;
import org.orcid.jaxb.model.v3.release.record.Name;
import org.orcid.jaxb.model.v3.release.record.Person;

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;

/**
 * Collector for retrieving contact information from ORCID records.
 * <p>
 * This class implements the {@link ContactInfoCollector} interface and is responsible
 * for seeking contact information related to ORCID identifiers.
 * It processes ORCID records to gather emails.
 * </p>
 */
public class ContactInfoOrcidCollector implements ContactInfoCollector {

    private static final Logger LOGGER = LogManager.getLogger();

    private static String ORIGIN = "orcid";

    @Override
    public List<ContactInfoDto> collect(MCRObject object) {
        LOGGER.info("Collecting contacts for {} from orcid", object.getId());
        final List<ContactInfoDto> contacts = new ArrayList<>();
        final List<String> orcids = getOrcids(object);
        LOGGER.info("Detected orcids: {}", orcids);
        for (String orcid : orcids) {
            contacts.addAll(processOrcid(orcid));
        }
        return contacts;
    }

    /**
     * Retrieves a list of ORCID identifiers associated with the given {@link MCRObject}.
     *
     * @param object the object from which ORCID identifiers are extracted
     * @return a list of ORCID identifiers associated with the provided object
     */
    protected List<String> getOrcids(MCRObject object) {
        return new MCRMODSWrapper(object)
            .getElements("mods:name[mods:role/mods:roleTerm"
                + "/@valueURI='https://www.mycore.de/classifications/author_roles#corresponding_author']"
                + "/mods:nameIdentifier[@type='orcid']")
            .stream().map(Element::getTextTrim).distinct().collect(Collectors.toList());
    }

    /**
     * Processes a given ORCID identifier to retrieve and map contact information.
     *
     * @param orcid the ORCID identifier to process
     * @return a list of DTOs representing the contact information associated with the given ORCID
     */
    protected List<ContactInfoDto> processOrcid(String orcid) {
        final String name = fetchNameFromPublicApi(orcid);
        final MCRORCIDUser orcidUser = MCRORCIDUserUtils.getORCIDUserByORCID(orcid);
        if (orcidUser != null) {
            final Set<String> emails = new HashSet<String>();
            // TODO may check all credentials of user
            final MCRORCIDCredential credential = orcidUser.getCredentialByORCID(orcid);
            final Set<String> otherOrcids = orcidUser.getORCIDs();
            if (credential != null) {
                try {
                    emails.addAll(fetchEmailsFromMemberApi(orcid, credential));
                    otherOrcids.remove(orcid);
                } catch (Exception e) {
                    LOGGER.warn(e);
                }
            }
            for (String otherOrcid : otherOrcids) {
                try {
                    emails.addAll(fetchEmailsFromPublicApi(otherOrcid));
                } catch (Exception e) {
                    LOGGER.warn(e);
                }
            }
            return emails.stream().map(m -> createOrcidContactInfoDto(name, m, orcid)).toList();
        } else {
            try {
                return fetchEmailsFromPublicApi(orcid).stream().map(m -> createOrcidContactInfoDto(name, m, orcid))
                    .toList();
            } catch (Exception e) {
                LOGGER.warn(e);
            }
        }
        return Collections.emptyList();
    }

    private List<String> fetchEmailsFromMemberApi(String orcid, MCRORCIDCredential credential) {
        return extractEmails(MCRORCIDClientHelper.getClientFactory().createUserClient(orcid, credential)
            .fetch(MCRORCIDSectionImpl.EMAIL, Emails.class));
    }

    private List<String> fetchEmailsFromPublicApi(String orcid) {
        return extractEmails(MCRORCIDClientHelper.getClientFactory().createReadClient().fetch(orcid,
            MCRORCIDSectionImpl.EMAIL, Emails.class));
    }

    private List<String> extractEmails(Emails emails) {
        return emails.getEmails().stream().map(Email::getEmail).distinct().toList();
    }

    private String fetchNameFromPublicApi(String orcid) {
        try {
            final Name name = MCRORCIDClientHelper.getClientFactory().createReadClient()
                .fetch(orcid, MCRORCIDSectionImpl.PERSON, Person.class).getName();
            return name.getGivenNames().getContent() + " " + name.getFamilyName().getContent();
        } catch (Exception e) {
            return orcid;
        }
    }

    private static ContactInfoDto createOrcidContactInfoDto(String name, String email, String orcid) {
        final ContactInfoDto contactInfoDto = new ContactInfoDto();
        contactInfoDto.setName(name);
        contactInfoDto.setEmail(email);
        contactInfoDto.setOrigin(ORIGIN);
        contactInfoDto.setReference(orcid);
        return contactInfoDto;
    }

}
