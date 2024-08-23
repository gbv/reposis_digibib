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

import de.vzg.reposis.digibib.email.dto.SimpleEmailDto;
import de.vzg.reposis.digibib.email.exception.EmailException;

/**
 * Interface for sending emails. This interface defines methods for sending emails with various options such
 * as recipients, subject, body, and attachments.
 */
public interface SimpleEmailSender {

    /**
     * Sends an email to the specified recipient.
     * <p>
     * This method uses the provided {@link SimpleEmailDto} object to construct the email message and send it
     * to the recipient address.
     * </p>
     *
     * @param email the email DTO containing email details
     * @throws EmailException if an error occurs while sending the email
     */
    void sendEmail(SimpleEmailDto email);
}
