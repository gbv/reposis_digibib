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

import java.util.UUID;

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestException;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.email.exception.EmailException;

/**
 * Interface for a service to send mails via SMTP.
 */
public interface EmailService {

    /**
     * Creates confirmation email and sends it to from.
     *
     * @param contactRequestDto the contact request DTO
     * @throws EmailException if an email error occurs
     */
    void sendRequestConfirmation(ContactRequestDto contactRequestDto);

    /**
     * Creates new request info email and sends to staff.
     *
     * @param contactRequestDto the contact request DTO
     * @throws EmailException if an email error occurs
     */
    void sendNewRequestInfo(ContactRequestDto contactRequestDto);

    /**
     * Creates forwarding email and sends to email via {@link ContactInfo}.
     *
     * @param contactRequestDto the contact request DTO
     * @param contactInfoDto the contact info DTO
     * @param contactAttemptId the contact attempt id
     * @throws EmailException if an email error occurs
     */
    void sendRequestForwarding(ContactRequestDto contactRequestDto, ContactInfoDto contactInfoDto,
        UUID contactAttemptId);

    /**
     * Discovers and handles bounce messages.
     *
     * @throws ContactRequestException if cannot retrieve bounced message
     */
    void handleBouncedMessages();
}
