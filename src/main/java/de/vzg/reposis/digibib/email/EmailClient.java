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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.vzg.reposis.digibib.email.dto.SimpleEmailDto;
import de.vzg.reposis.digibib.email.exception.EmailException;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * A client for sending and receiving emails using the JavaMail API.
 * <p>
 * This class provides functionality to send emails with optional headers and to access the email store
 * from the mail session. It uses UTF-8 encoding for email content and headers.
 * </p>
 */
public class EmailClient implements SimpleEmailSender {

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
    public void sendEmail(SimpleEmailDto emailDto) {
        final MimeMessage message = new MimeMessage(mailSession);
        try {
            final List<Address> recipients = new ArrayList<>();
            for (String to : emailDto.getTo()) {
                recipients.addAll(Arrays.asList(InternetAddress.parse(to)));
            }
            message.setFrom();
            message.setRecipients(Message.RecipientType.TO, recipients.toArray(Address[]::new));
            message.setSubject(emailDto.getSubject(), ENCODING);
            if (emailDto.getSentDate() == null) {
                message.setSentDate(new Date());
            } else {
                message.setSentDate(emailDto.getSentDate());
            }
            message.setText(emailDto.getBody(), ENCODING);
            for (Map.Entry<String, String> entry : emailDto.getHeaders().entrySet()) {
                message.setHeader(entry.getKey(), entry.getValue());
            }
            Transport.send(message);
        } catch (AddressException e) {
            throw new EmailException("Address is invalid", e);
        } catch (MessagingException e) {
            throw new EmailException("Cannot send emai", e);
        }
    }

}
