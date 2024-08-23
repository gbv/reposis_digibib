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

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestException;
import de.vzg.reposis.digibib.email.exception.EmailException;

/**
 * Defines operations for managing and sending various types of emails related to contact requests.
 * <p>
 * This interface includes methods for sending confirmation emails, new request notifications,
 * forwarding emails, and handling bounce messages.
 * </p>
 */
public interface EmailService {

    /**
     * Sends a confirmation email to the contact request originator.
     *
     * @param contactRequestDto the DTO containing the contact request details
     * @throws EmailException if an error occurs while sending the email
     */
    void sendRequestConfirmation(ContactRequestDto contactRequestDto);

    /**
     * Sends a notification email about a new contact request to the staff.
     *
     * @param contactRequestDto the DTO containing the contact request details
     * @throws EmailException if an error occurs while sending the email
     */
    void sendNewRequestInfo(ContactRequestDto contactRequestDto);

    /**
     * Sends a forwarding email based on the contact request and attempt details.
     *
     * @param contactRequestDto the DTO containing the contact request details
     * @param contactAttemptDto the DTO containing the contact attempt details
     * @throws EmailException if an error occurs while sending the email
     */
    void sendRequestForwarding(ContactRequestDto contactRequestDto, ContactAttemptDto contactAttemptDto);

    /**
     * Discovers and processes bounced email messages.
     *
     * @throws ContactRequestException if an error occurs while retrieving or processing bounced messages
     */
    void handleBouncedMessages();
}
