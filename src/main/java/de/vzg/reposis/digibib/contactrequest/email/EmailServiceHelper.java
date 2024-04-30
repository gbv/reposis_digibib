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

package de.vzg.reposis.digibib.contactrequest.email;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.mycore.common.MCRMailer.EMail;
import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.content.MCRJDOMContent;
import org.mycore.common.content.transformer.MCRXSL2XMLTransformer;
import org.mycore.common.xsl.MCRParameterCollector;

import de.vzg.reposis.digibib.contactrequest.ContactRequestConstants;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactRequest;
import de.vzg.reposis.digibib.email.exception.EmailException;

/**
 * Provides helper methods for {@link EmailServiceImpl}.
 */
public class EmailServiceHelper {
    private static final String CONF_PREFIX = ContactRequestConstants.CONF_PREFIX + "EmailService.";

    private static final String REQUEST_CONFIRMATION_EMAIL_STYLESHEET
        = MCRConfiguration2.getStringOrThrow(CONF_PREFIX + "RequestConfirmationEmail.Stylesheet");

    private static final String NEW_REQUEST_INFO_EMAIL_STYLESHEET
        = MCRConfiguration2.getStringOrThrow(CONF_PREFIX + "NewRequestInfoEmail.Stylesheet");

    private static final String REQUEST_FORWARDING_EMAIL_STYLESHEET
        = MCRConfiguration2.getStringOrThrow(CONF_PREFIX + "RequestForwardingEmail.Stylesheet");

    private static final String PARAM_REQUEST_ID = "requestId";

    private static final String PARAM_OBJECT_ID = "objectId";

    private static final String PARAM_ATTEMPT_ID = "attemptId";

    private static final String PARAM_REQUEST_NAME = "rname";

    private static final String PARAM_REQUEST_EMAIL = "remail";

    private static final String PARAM_REQUEST_ORCID = "rorcid";

    private static final String PARAM_REQUEST_MESSAGE = "rmessage";

    private static final String PARAM_COMMENT = "comment";

    private static final String TO_NAME = "tname";

    /**
     * Creates and returns request forwarding email for {@link ContactRequest} and {@link ContactInfo}.
     *
     * @param contactRequestDto the contact request DTO
     * @param toContactInfoDto to contact info DTO
     * @param contactAttemptId contact attempt id
     * @return email
     */
    protected static EMail createRequestForwardingEmail(ContactRequestDto contactRequestDto,
        ContactInfoDto toContactInfoDto, UUID contactAttemptId) {
        final EMail baseEmail = new EMail();
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PARAM_REQUEST_ID, contactRequestDto.getId().toString());
        properties.put(PARAM_OBJECT_ID, contactRequestDto.getObjectId().toString());
        properties.put(PARAM_ATTEMPT_ID, contactAttemptId.toString());
        properties.put(PARAM_REQUEST_EMAIL, contactRequestDto.getBody().getEmail());
        properties.put(PARAM_REQUEST_MESSAGE, contactRequestDto.getBody().getMessage());
        properties.put(PARAM_REQUEST_NAME, contactRequestDto.getBody().getName());
        Optional.ofNullable(contactRequestDto.getBody().getOrcid())
            .ifPresent(o -> properties.put(PARAM_REQUEST_ORCID, o));
        Optional.ofNullable(contactRequestDto.getComment()).ifPresent(c -> properties.put(PARAM_COMMENT, c));
        properties.put(TO_NAME, toContactInfoDto.getName());
        final Element emailElement
            = transform(baseEmail.toXML(), REQUEST_FORWARDING_EMAIL_STYLESHEET, properties).getRootElement();
        return EMail.parseXML(emailElement);
    }

    /**
     * Creates and returns request confirmation email for {@link ContactRequest}.
     *
     * @param contactRequestDto the contact request DTO
     * @return email
     */
    protected static EMail createRequestConfirmationEmail(ContactRequestDto contactRequestDto) {
        final EMail baseEmail = new EMail();
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PARAM_REQUEST_ID, contactRequestDto.getId().toString());
        properties.put(PARAM_OBJECT_ID, contactRequestDto.getObjectId().toString());
        properties.put(PARAM_REQUEST_NAME, contactRequestDto.getBody().getName());
        Optional.ofNullable(contactRequestDto.getBody().getOrcid())
            .ifPresent(o -> properties.put(PARAM_REQUEST_ORCID, o));
        properties.put(PARAM_REQUEST_MESSAGE, contactRequestDto.getBody().getMessage());
        final Element emailElement
            = transform(baseEmail.toXML(), REQUEST_CONFIRMATION_EMAIL_STYLESHEET, properties).getRootElement();
        return EMail.parseXML(emailElement);
    }

    /**
     * Creates and returns new request info email for {@link ContactRequest}.
     *
     * @param contactRequestDto the contact request DTO
     * @return email
     */
    protected static EMail createNewRequestInfoEmail(ContactRequestDto contactRequestDto) {
        final EMail baseEmail = new EMail();
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PARAM_OBJECT_ID, contactRequestDto.getObjectId().toString());
        final Element emailElement
            = transform(baseEmail.toXML(), NEW_REQUEST_INFO_EMAIL_STYLESHEET, properties).getRootElement();
        return EMail.parseXML(emailElement);
    }

    private static Document transform(Document input, String stylesheet, Map<String, String> parameters) {
        MCRJDOMContent source = new MCRJDOMContent(input);
        MCRXSL2XMLTransformer transformer = MCRXSL2XMLTransformer.getInstance(stylesheet);
        MCRParameterCollector parameterCollector = MCRParameterCollector.getInstanceFromUserSession();
        parameterCollector.setParameters(parameters);
        try {
            return transformer.transform(source, parameterCollector).asXML();
        } catch (IOException | JDOMException e) {
            throw new EmailException("Cannot transform document", e);
        }
    }
}
