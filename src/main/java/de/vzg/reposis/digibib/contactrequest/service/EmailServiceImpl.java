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

package de.vzg.reposis.digibib.contactrequest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.config.MCRConfiguration2;

import com.sun.mail.dsn.MultipartReport;

import de.vzg.reposis.digibib.contactrequest.ContactRequestConstants;
import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.util.Nullable;
import de.vzg.reposis.digibib.contactrequest.email.EmailServiceHelper;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestException;
import de.vzg.reposis.digibib.email.EmailClient;
import de.vzg.reposis.digibib.email.dto.SimpleEmailDto;
import de.vzg.reposis.digibib.email.exception.EmailException;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.FlagTerm;

/**
 * Implementation of the {@link EmailService} interface for managing and sending various types of emails
 * related to contact requests.
 */
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String STAFF_EMAIL
        = MCRConfiguration2.getStringOrThrow(ContactRequestConstants.CONF_PREFIX + "StaffEmail");

    private static final String REPORT_MIMETYPE = "multipart/report";

    private static final String CONTACT_ATTEMPT_HEADER_NAME = "X-Contact-Attempt-ID";

    private final EmailClient client;

    /**
     * Constructs a new {@code EmailServiceImpl} with the specified {@link EmailClient} instance.
     *
     * @param client the email client
     */
    public EmailServiceImpl(EmailClient client) {
        this.client = client;
    }

    @Override
    public void sendNewRequestInfo(ContactRequestDto contactRequestDto) {
        final SimpleEmailDto newRequestInfoEmail = EmailServiceHelper.createNewRequestInfoEmail(contactRequestDto);
        newRequestInfoEmail.getTo().add(STAFF_EMAIL);
        client.sendEmail(newRequestInfoEmail);
    }

    @Override
    public void sendRequestConfirmation(ContactRequestDto contactRequestDto) {
        final SimpleEmailDto requestConfirmationEmail
            = EmailServiceHelper.createRequestConfirmationEmail(contactRequestDto);
        requestConfirmationEmail.getTo().add(contactRequestDto.getBody().getEmail());
        client.sendEmail(requestConfirmationEmail);
    }

    @Override
    public void sendRequestForwarding(ContactRequestDto contactRequestDto, ContactAttemptDto contactAttemptDto) {
        final SimpleEmailDto requestForwardingEmail
            = EmailServiceHelper.createRequestForwardingEmail(contactRequestDto, contactAttemptDto);
        requestForwardingEmail.getHeaders().put(CONTACT_ATTEMPT_HEADER_NAME, contactAttemptDto.getId().toString());
        requestForwardingEmail.getTo().add(contactAttemptDto.getRecipientReference());
        client.sendEmail(requestForwardingEmail);
    }

    @Override
    public void handleBouncedMessages() {
        final Store store = client.getStore();
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
        } catch (MessagingException | ContactRequestException e) {
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
            throw new EmailException("Error while fetching messages", e);
        }
    }

    private static void proccessReportMessage(Message reportMessage) throws IOException, MessagingException {
        final MultipartReport report = (MultipartReport) ((MimeMessage) reportMessage).getContent();
        final MimeMessage dsnMessage = report.getReturnedMessage();
        if (dsnMessage == null) {
            return;
        }
        Optional.ofNullable(dsnMessage.getHeader(CONTACT_ATTEMPT_HEADER_NAME, null)).map(UUID::fromString)
            .ifPresent(attemptId -> {
                try {
                    final Date date = reportMessage.getReceivedDate();
                    final ContactAttemptPartialUpdateDto contactAttemptDto = new ContactAttemptPartialUpdateDto();
                    contactAttemptDto.setErrorDate(new Nullable<Date>(date));
                    ContactRequestFactory.getContactAttemptService().partialUpdateContactAttempt(attemptId,
                        contactAttemptDto);
                } catch (MessagingException | ContactRequestException e) {
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
}
