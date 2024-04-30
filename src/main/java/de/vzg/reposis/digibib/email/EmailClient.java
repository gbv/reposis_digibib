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

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.mycore.common.MCRMailer.EMail;
import org.mycore.common.MCRMailer.EMail.MessagePart;

import de.vzg.reposis.digibib.email.exception.EmailException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Provides email functionalities.
 */
public class EmailClient {

    private static final String ENCODING = "UTF-8";

    private final Session mailSession;

    /**
     * Constructs new email client with session.
     *
     * @param session session
     */
    public EmailClient(Session session) {
        this.mailSession = session;
    }

    /**
     * Returns client default store.
     *
     * @return store
     * @throws EmailException if an error occurs
     */
    public Store getStore() {
        try {
            return mailSession.getStore();
        } catch (MessagingException e) {
            throw new EmailException("Error while returning store", e);
        }
    }

    /**
     * Sends email to address.
     *
     * @param email email
     * @param to email address
     * @throws EmailException if cannot send mail
     */
    public void sendEmail(EMail email, String to) {
        sendEmail(email, to, Collections.emptyMap());
    }

    /**
     * Sends email to address with list over header elements.
     *
     * @param email email
     * @param to email address
     * @param headers list over header elements
     * @throws EmailException if cannot send mail
     */
    public void sendEmail(EMail email, String to, Map<String, String> headers) {
        final MimeMessage msg = new MimeMessage(mailSession);
        try {
            msg.setFrom();
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSentDate(new Date());
            msg.setSubject(email.subject, ENCODING);
            final Optional<MessagePart> plainMsg = email.getTextMessage();
            if (plainMsg.isPresent()) {
                msg.setText(plainMsg.get().message, ENCODING);
            }
            for (var entry : headers.entrySet()) {
                msg.setHeader(entry.getKey(), entry.getValue());
            }
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new EmailException("Cannot send mail", e);
        }
    }

}
