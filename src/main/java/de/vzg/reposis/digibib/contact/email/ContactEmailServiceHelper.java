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

package de.vzg.reposis.digibib.contact.email;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.mycore.common.MCRMailer.EMail;
import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.content.MCRJDOMContent;
import org.mycore.common.content.transformer.MCRXSL2XMLTransformer;
import org.mycore.common.xsl.MCRParameterCollector;

import de.vzg.reposis.digibib.contact.ContactConstants;
import de.vzg.reposis.digibib.contact.exception.ContactEmailException;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;

/**
 * Provides helper methods for {@link ContactEmailService}.
 */
public class ContactEmailServiceHelper {
    private static final String CONF_PREFIX = ContactConstants.CONF_PREFIX + "EmailService.";

    private static final String REQUEST_CONFIRMATION_EMAIL_STYLESHEET
        = MCRConfiguration2.getStringOrThrow(CONF_PREFIX + "RequestConfirmationEmail.Stylesheet");

    private static final String NEW_REQUEST_INFO_EMAIL_STYLESHEET
        = MCRConfiguration2.getStringOrThrow(CONF_PREFIX + "NewRequestInfoEmail.Stylesheet");

    private static final String REQUEST_FORWARDING_EMAIL_STYLESHEET
        = MCRConfiguration2.getStringOrThrow(CONF_PREFIX + "RequestForwardingEmail.Stylesheet");

    private static final String REQUEST_COMPLETED_CONFIRMATION_EMAIL_STYLESHEET
        = MCRConfiguration2.getStringOrThrow(CONF_PREFIX + "RequestCompletedConfirmationEmail.Stylesheet");

    private static final String PARAM_TICKET_ID = "ticketId";

    private static final String PARAM_OBJECT_ID = "objectId";

    private static final String PARAM_REQUEST_NAME = "rname";

    private static final String PARAM_REQUEST_EMAIL = "remail";

    private static final String PARAM_REQUEST_ORCID = "rorcid";

    private static final String PARAM_REQUEST_MESSAGE = "rmessage";

    private static final String PARAM_COMMENT = "comment";

    private static final String TO_NAME = "tname";

    private static final String TO_EMAIL = "temail";

    /**
     * Creates and returns request forwarding email for {@link ContactTicket} and {@link ContactInfo}.
     *
     * @param ticket ticket
     * @param toContactInfo to contact info
     * @return email
     */
    protected static EMail createRequestForwardingEmail(ContactTicket ticket, ContactInfo toContactInfo) {
        final EMail baseEmail = new EMail();
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PARAM_TICKET_ID, ticket.getId().toString());
        properties.put(PARAM_OBJECT_ID, ticket.getObjectId().toString());
        properties.put(PARAM_REQUEST_EMAIL, ticket.getContactRequest().getEmail());
        properties.put(PARAM_REQUEST_MESSAGE, ticket.getContactRequest().getMessage());
        properties.put(PARAM_REQUEST_NAME, ticket.getContactRequest().getName());
        Optional.ofNullable(ticket.getContactRequest().getOrcid()).ifPresent(o -> properties.put(PARAM_REQUEST_ORCID, o));
        Optional.ofNullable(ticket.getComment()).ifPresent(c -> properties.put(PARAM_COMMENT, c));
        properties.put(TO_NAME, toContactInfo.getName());
        properties.put(TO_EMAIL, toContactInfo.getEmail());
        final Element emailElement
            = transform(baseEmail.toXML(), REQUEST_FORWARDING_EMAIL_STYLESHEET, properties).getRootElement();
        return EMail.parseXML(emailElement);
    }

    /**
     * Creates and returns request confirmation email for {@link ContactTicket}.
     *
     * @param ticket ticket
     * @return email
     */
    protected static EMail createRequestConfirmationEmail(ContactTicket ticket) {
        final EMail baseEmail = new EMail();
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PARAM_TICKET_ID, ticket.getId().toString());
        properties.put(PARAM_OBJECT_ID, ticket.getObjectId().toString());
        properties.put(PARAM_REQUEST_NAME, ticket.getContactRequest().getName());
        Optional.ofNullable(ticket.getContactRequest().getOrcid()).ifPresent(o -> properties.put(PARAM_REQUEST_ORCID, o));
        properties.put(PARAM_REQUEST_MESSAGE, ticket.getContactRequest().getMessage());
        final Element emailElement
            = transform(baseEmail.toXML(), REQUEST_CONFIRMATION_EMAIL_STYLESHEET, properties).getRootElement();
        return EMail.parseXML(emailElement);
    }

    /**
     * Creates and returns new request info email for {@link ContactTicket}.
     *
     * @param ticket ticket
     * @return email
     */
    protected static EMail createNewRequestInfoEmail(ContactTicket ticket) {
        final EMail baseEmail = new EMail();
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PARAM_OBJECT_ID, ticket.getObjectId().toString());
        final Element emailElement
            = transform(baseEmail.toXML(), NEW_REQUEST_INFO_EMAIL_STYLESHEET, properties).getRootElement();
        return EMail.parseXML(emailElement);
    }

    /**
     * Creates and returns request completed confirmation email.
     *
     * @param ticket ticket
     * @return email
     */
    protected static EMail createRequestCompletedConfirmationEmail(ContactTicket ticket) {
        final EMail baseEmail = new EMail();
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PARAM_OBJECT_ID, ticket.getObjectId().toString());
        properties.put(PARAM_REQUEST_NAME, ticket.getContactRequest().getName());
        final Element emailElement
            = transform(baseEmail.toXML(), REQUEST_COMPLETED_CONFIRMATION_EMAIL_STYLESHEET, properties)
                .getRootElement();
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
            throw new ContactEmailException("Cannot transform document", e);
        }
    }
}
