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

package de.vzg.reposis.digibib.email;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.vzg.reposis.digibib.email.exception.EmailException;
import de.vzg.reposis.digibib.email.model.EmailAttachment;
import de.vzg.reposis.digibib.email.model.EmailMessage;
import de.vzg.reposis.digibib.email.model.EmailSendRequest;
import jakarta.activation.DataHandler;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

/**
 * A client for sending and receiving emails using the JavaMail API.
 * <p>
 * This class provides functionality to send emails with optional headers and to access the email store
 * from the mail session. It uses UTF-8 encoding for email content and headers.
 * </p>
 */
public class EmailClient implements EmailSender {

    private static final String ENCODING = "UTF-8";

    private final Session mailSession;

    /**
     * Constructs a new {@code EmailClient} with the specified mail session.
     *
     * @param session the session object used for email communication
     */
    public EmailClient(Session session) {
        this.mailSession = session;
    }

    /**
     * Returns the default {@link Store} associated with the mail session.
     * <p>
     * The store allows access to email messages and folders in the mail system.
     * </p>
     *
     * @return the store object
     * @throws EmailException if an error occurs while retrieving the store
     */
    public Store getStore() {
        try {
            return mailSession.getStore();
        } catch (MessagingException e) {
            throw new EmailException("Error while returning store", e);
        }
    }

    @Override
    public void sendEmail(EmailSendRequest emailSendRequest) {
        final MimeMessage message = new MimeMessage(mailSession);
        final EmailMessage emailMessage = emailSendRequest.getMessage();
        try {
            setFromAddress(message);
            setRecipients(message, emailMessage.getToRecipients());
            setSubject(message, emailMessage.getSubject());
            setSentDate(message, emailMessage.getSentDate());
            MimeBodyPart bodyPart = createBodyPart(emailMessage);
            setContentWithAttachments(message, bodyPart, emailSendRequest.getAttachments());
            setHeaders(message, emailMessage.getHeaders());
            message.saveChanges();
            Transport.send(message);
        } catch (AddressException e) {
            throw new EmailException("Invalid email address", e);
        } catch (MessagingException e) {
            throw new EmailException("Messaging error while sending email", e);
        } catch (Exception e) {
            throw new EmailException("Unexpected error while sending email", e);
        }
    }

    private void setFromAddress(MimeMessage message) throws MessagingException {
        message.setFrom();
    }

    private void setRecipients(MimeMessage message, List<String> toRecipients) throws MessagingException {
        final List<Address> recipients = toRecipients.stream()
            .flatMap(to -> {
                try {
                    return Arrays.stream(InternetAddress.parse(to));
                } catch (AddressException e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.toList());
        message.setRecipients(Message.RecipientType.TO, recipients.toArray(new Address[0]));
    }

    private void setSubject(MimeMessage message, String subject) throws MessagingException {
        if (subject != null) {
            message.setSubject(subject, ENCODING);
        }
    }

    private void setSentDate(MimeMessage message, Date sentDate) throws MessagingException {
        message.setSentDate(sentDate != null ? sentDate : new Date());
    }

    private MimeBodyPart createBodyPart(EmailMessage emailMessage) throws MessagingException {
        if (emailMessage.getTextBody() != null && emailMessage.getHtmlBody() != null) {
            final MimeMultipart alternativeMultipart = new MimeMultipart("alternative");

            final MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(emailMessage.getTextBody(), ENCODING);
            alternativeMultipart.addBodyPart(textPart);

            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(emailMessage.getHtmlBody(), "text/html; charset=" + ENCODING);
            alternativeMultipart.addBodyPart(htmlPart);

            final MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(alternativeMultipart);
            return bodyPart;

        } else if (emailMessage.getHtmlBody() != null) {
            final MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(emailMessage.getHtmlBody(), "text/html; charset=" + ENCODING);
            return bodyPart;

        } else if (emailMessage.getTextBody() != null) {
            final MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(emailMessage.getTextBody(), ENCODING);
            return bodyPart;

        } else {
            final MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText("", ENCODING);
            return bodyPart;
        }
    }

    private void setContentWithAttachments(MimeMessage message, MimeBodyPart bodyPart,
        List<EmailAttachment> attachments) throws MessagingException, IOException {
        if (attachments != null && !attachments.isEmpty()) {
            final MimeMultipart mixedMultipart = new MimeMultipart("mixed");
            mixedMultipart.addBodyPart(bodyPart);

            for (EmailAttachment attachment : attachments) {
                final MimeBodyPart attachmentPart = new MimeBodyPart();
                final ByteArrayDataSource dataSource
                    = new ByteArrayDataSource(attachment.getData(), attachment.getMimeType());
                attachmentPart.setDataHandler(new DataHandler(dataSource));
                attachmentPart.setFileName(attachment.getFilename());
                mixedMultipart.addBodyPart(attachmentPart);
            }
            message.setContent(mixedMultipart);
        } else {
            message.setContent(bodyPart.getContent(), bodyPart.getContentType());
        }
    }

    private void setHeaders(MimeMessage message, Map<String, String> headers) throws MessagingException {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                message.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

}
