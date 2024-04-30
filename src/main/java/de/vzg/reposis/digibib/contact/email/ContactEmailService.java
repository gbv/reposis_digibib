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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.MCRMailer.EMail;

import com.sun.mail.dsn.MultipartReport;

import de.vzg.reposis.digibib.contact.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contact.exception.ContactEmailException;
import de.vzg.reposis.digibib.contact.exception.ContactException;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import de.vzg.reposis.digibib.contact.service.ContactAttemptService;
import de.vzg.reposis.digibib.contact.service.ContactAttemptServiceImpl;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.FlagTerm;

/**
 * This class implements a service to send mails via SMTP.
 */
public class ContactEmailService {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String REPORT_MIMETYPE = "multipart/report";

    private static final String CONTACT_ATTEMPT_HEADER_NAME = "X-Contact-Attempt-ID";

    /**
     * Creates confirmation email and sends it to from.
     *
     * @param contactTicket contact ticket
     * @throws ContactEmailException if an email error occurs
     */
    public static void sendRequestConfirmation(ContactTicket contactTicket) {
        final EMail requestConfirmationEmail = ContactEmailServiceHelper.createRequestConfirmationEmail(contactTicket);
        getInstance().sendEmail(requestConfirmationEmail, contactTicket.getContactRequest().getEmail());
    }

    /**
     * Creates new request info email and sends to email.
     *
     * @param contactTicket contact ticket
     * @param toEmail to email
     * @throws ContactEmailException if an email error occurs
     */
    public static void sendNewRequestInfo(ContactTicket contactTicket, String toEmail) {
        final EMail newRequestInfoEmail = ContactEmailServiceHelper.createNewRequestInfoEmail(contactTicket);
        getInstance().sendEmail(newRequestInfoEmail, toEmail);
    }

    /**
     * Creates completed confirmation email and sends to request applicant.
     *
     * @param contactTicket contact ticket
     * @throws ContactEmailException if an email error occurs
     */
    public static void sendRequestCompletedConfirmation(ContactTicket contactTicket) {
        final EMail requestCompletedConfirmationEmail
            = ContactEmailServiceHelper.createRequestCompletedConfirmationEmail(contactTicket);
        getInstance().sendEmail(requestCompletedConfirmationEmail, contactTicket.getContactRequest().getEmail());
    }

    /**
     * Creates forwarding email and sends to email via {@link ContactInfo}.
     *
     * @param contactTicket contact ticket
     * @param contactInfo contact info
     * @param contactAttemptId contact attempt id
     * @throws ContactEmailException if an email error occurs
     */
    public static void sendRequestForwarding(ContactTicket contactTicket, ContactInfo contactInfo,
        UUID contactAttemptId) {
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(CONTACT_ATTEMPT_HEADER_NAME, contactAttemptId.toString());
        final EMail requestForwardingEmail
            = ContactEmailServiceHelper.createRequestForwardingEmail(contactTicket, contactInfo);
        getInstance().sendEmail(requestForwardingEmail, contactInfo.getEmail(), headers);
    }

    /**
     * Discovers and handles bounce messages.
     *
     * @throws ContactException if cannot retrieve bounced message
     */
    public static void handleBouncedMessages() {
        final Store store = getInstance().getStore();
        boolean connected = false;
        try {
            store.connect();
            connected = true;
            final List<Message> reportMessages = fetchUnseenReportMessages(store);
            reportMessages.stream().forEach(m -> {
                try {
                    proccessReportMessage(m);
                } catch (IOException | MessagingException e) {
                    LOGGER.error("Error while proccessing report message");
                }
            });
        } catch (MessagingException | ContactException e) {
            LOGGER.error("Error while handling bounced messages", e);
        } finally {
            if (connected) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    LOGGER.error("Error while closing store", e);
                }
            }
        }
    }

    private static List<Message> fetchUnseenReportMessages(Store store) {
        try {
            final Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            final List<Message> unreadMessages
                = Arrays.asList(inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false)));
            final List<Message> reportMessages = new ArrayList<>();
            for (Message message : unreadMessages) {
                if (message.isMimeType(REPORT_MIMETYPE)) {
                    reportMessages.add(message);
                }
            }
            return reportMessages;
        } catch (MessagingException e) {
            throw new ContactEmailException("Error while fetching messages", e);
        }
    }

    private static void proccessReportMessage(Message reportMessage) throws IOException, MessagingException {
        final MultipartReport report = (MultipartReport) ((MimeMessage) reportMessage).getContent();
        final MimeMessage dsnMessage = report.getReturnedMessage();
        if (dsnMessage == null) {
            return;
        }
        final ContactAttemptService eventService = ContactAttemptServiceImpl.getInstance();
        Optional.ofNullable(dsnMessage.getHeader(CONTACT_ATTEMPT_HEADER_NAME, null)).map(UUID::fromString)
            .ifPresent(attemptId -> {
                try {
                    final Date date = reportMessage.getReceivedDate();
                    final ContactAttemptDto contactAttemptDto = new ContactAttemptDto();
                    contactAttemptDto.setId(attemptId);
                    contactAttemptDto.setErrorDate(date);
                    ContactAttemptServiceImpl.getInstance().partialUpdateContactAttempt(contactAttemptDto);
                } catch (MessagingException | ContactException e) {
                    LOGGER.error("Error while processing dsn message for attempt: {}", attemptId, e);
                } finally {
                    try {
                        reportMessage.setFlag(Flags.Flag.SEEN, true);
                    } catch (MessagingException e) {
                        LOGGER.error("Error while flagging message for {} as seen", attemptId, e);
                    }
                }
            });
    }

    private static ContactEmailClient getInstance() {
        return ContactEmailClientFactory.getInstance("default");
    }
}
