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

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.transform.JDOMSource;
import org.mycore.common.MCRException;
import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.content.MCRJAXBContent;
import org.mycore.common.content.MCRJDOMContent;
import org.mycore.common.content.transformer.MCRXSL2XMLTransformer;
import org.mycore.common.xsl.MCRParameterCollector;

import de.vzg.reposis.digibib.contactrequest.ContactRequestConstants;
import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.email.dto.ContactRequestEmailDto;
import de.vzg.reposis.digibib.email.exception.EmailException;
import de.vzg.reposis.digibib.email.model.EmailMessage;
import de.vzg.reposis.digibib.email.model.EmailSendRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Utility class for creating and managing email templates related to contact requests.
 * <p>
 * This class provides static methods to generate different types of emails, including request
 * confirmations, request information for staff, and request forwarding emails, using predefined
 * XSLT stylesheets for transformation.
 * </p>
 */
public class EmailServiceHelper {
    private static final String CONF_PREFIX = ContactRequestConstants.CONF_PREFIX + "EmailService.";

    private static final String REQUEST_CONFIRMATION_EMAIL_STYLESHEET
        = MCRConfiguration2.getStringOrThrow(CONF_PREFIX + "RequestConfirmationEmail.Stylesheet");

    private static final String NEW_REQUEST_INFO_EMAIL_STYLESHEET
        = MCRConfiguration2.getStringOrThrow(CONF_PREFIX + "NewRequestInfoEmail.Stylesheet");

    private static final String REQUEST_FORWARDING_EMAIL_STYLESHEET
        = MCRConfiguration2.getStringOrThrow(CONF_PREFIX + "RequestForwardingEmail.Stylesheet");

    private static final JAXBContext JAXB_CONTEXT = initContext();

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
     * Creates a {@link ContactRequestEmailDto} for confirming a contact request,
     * based on the provided {@link ContactRequestDto}.
     *
     * @param contactRequestDto the DTO containing details about the contact request
     * @return a DTO representing the constructed request confirmation email
     */
    public static ContactRequestEmailDto createRequestConfirmationEmail(ContactRequestDto contactRequestDto) {
        final ContactRequestEmailDto baseEmail = new ContactRequestEmailDto();
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PARAM_REQUEST_ID, contactRequestDto.getId().toString());
        properties.put(PARAM_OBJECT_ID, contactRequestDto.getObjectId().toString());
        properties.put(PARAM_REQUEST_NAME, contactRequestDto.getBody().getName());
        Optional.ofNullable(contactRequestDto.getBody().getOrcid())
            .ifPresent(o -> properties.put(PARAM_REQUEST_ORCID, o));
        properties.put(PARAM_REQUEST_MESSAGE, contactRequestDto.getBody().getMessage());
        final Document emailDocument = convertToDocument(baseEmail);
        final Element emailElement
            = transform(emailDocument, REQUEST_CONFIRMATION_EMAIL_STYLESHEET, properties).getRootElement();
        return convertToEmailMessage(emailElement);
    }

    /**
     * Creates a {@link ContactRequestEmailDto} for forwarding a contact request, based on the
     * provided {@link ContactRequestDto} and {@link ContactAttemptDto}.
     *
     * @param contactRequestDto the DTO containing details about the contact request
     * @param contactAttemptDto the DTO containing details about the contact attempt
     * @return a DTO representing the constructed request forwarding email
     */
    public static ContactRequestEmailDto createRequestForwardingEmail(ContactRequestDto contactRequestDto,
        ContactAttemptDto contactAttemptDto) {
        final ContactRequestEmailDto baseEmail = new ContactRequestEmailDto();
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PARAM_REQUEST_ID, contactRequestDto.getId().toString());
        properties.put(PARAM_OBJECT_ID, contactRequestDto.getObjectId().toString());
        properties.put(PARAM_ATTEMPT_ID, contactAttemptDto.getId().toString());
        properties.put(PARAM_REQUEST_EMAIL, contactRequestDto.getBody().getEmail());
        properties.put(PARAM_REQUEST_MESSAGE, contactRequestDto.getBody().getMessage());
        properties.put(PARAM_REQUEST_NAME, contactRequestDto.getBody().getName());
        Optional.ofNullable(contactRequestDto.getBody().getOrcid())
            .ifPresent(o -> properties.put(PARAM_REQUEST_ORCID, o));
        Optional.ofNullable(contactAttemptDto.getComment()).ifPresent(c -> properties.put(PARAM_COMMENT, c));
        properties.put(TO_NAME, contactAttemptDto.getRecipientName());
        final Document emailDocument = convertToDocument(baseEmail);
        final Element emailElement
            = transform(emailDocument, REQUEST_FORWARDING_EMAIL_STYLESHEET, properties).getRootElement();
        return convertToEmailMessage(emailElement);
    }

    /**
     * Creates a {@link ContactRequestEmailDto} containing new request information, based on the provided {@link ContactRequestDto}.
     *
     * @param contactRequestDto the DTO containing details about the contact request
     * @return a DTO representing the constructed new request information email
     */
    public static ContactRequestEmailDto createNewRequestInfoEmail(ContactRequestDto contactRequestDto) {
        final ContactRequestEmailDto baseEmail = new ContactRequestEmailDto();
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put(PARAM_OBJECT_ID, contactRequestDto.getObjectId().toString());
        final Document emailDocument = convertToDocument(baseEmail);
        final Element emailElement
            = transform(emailDocument, NEW_REQUEST_INFO_EMAIL_STYLESHEET, properties).getRootElement();
        return convertToEmailMessage(emailElement);
    }

    /**
     * Constructs an {@link EmailSendRequest} from a {@link ContactRequestEmailDto}.
     * <p>
     * This method translates the data in the DTO (from, to, cc, bcc, subject, body, headers, etc.)
     * into an {@link EmailMessage}, and wraps it in an {@link EmailSendRequest}.
     * </p>
     *
     * @param emailDto the DTO containing the email data
     * @return an {@link EmailSendRequest} built from the DTO
     */
    public static EmailSendRequest getSendRequest(ContactRequestEmailDto emailDto) {
        final EmailMessage message = new EmailMessage.Builder(emailDto.getTo())
            .ccRecipients(emailDto.getCc())
            .bccRecipients(emailDto.getBcc())
            .sentDate(emailDto.getSentDate())
            .subject(emailDto.getSubject())
            .headers(emailDto.getHeaders())
            .textBody(emailDto.getBody())
            .build();
        return new EmailSendRequest(message);
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

    private static Document convertToDocument(ContactRequestEmailDto simpleEmailDto) {
        final MCRJAXBContent<ContactRequestEmailDto> content = new MCRJAXBContent<>(JAXB_CONTEXT, simpleEmailDto);
        try {
            return content.asXML();
        } catch (IOException e) {
            throw new MCRException("Exception while transforming SimpleEmailDto to Document", e);
        }
    }

    private static ContactRequestEmailDto convertToEmailMessage(Element element) {
        try {
            final Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            return (ContactRequestEmailDto) unmarshaller.unmarshal(new JDOMSource(element));
        } catch (final JAXBException e) {
            throw new MCRException("Exception while transforming Element to SimpleEmailDto", e);
        }
    }

    private static JAXBContext initContext() {
        try {
            return JAXBContext.newInstance(ContactRequestEmailDto.class);
        } catch (final JAXBException e) {
            throw new MCRException("Could not instantiate JAXBContext.", e);
        }
    }
}
