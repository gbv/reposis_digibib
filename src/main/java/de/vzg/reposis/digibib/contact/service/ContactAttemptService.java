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

package de.vzg.reposis.digibib.contact.service;

import java.util.List;
import java.util.UUID;

import de.vzg.reposis.digibib.contact.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contact.exception.ContactAttemptNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketNotFoundException;
import de.vzg.reposis.digibib.contact.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;

/**
 * This interface defines operations related to {@link ContactAttempt}.
 * It allows adding new contact attempt to a {@link ContactInfo}.
 */
public interface ContactAttemptService {

    /**
     * Creates and adds {@link ContactAttempt} to a {@link ContactTicket} by its id.
     *
     * @param contactTicketId the contact ticket id
     * @param contactAttemptDto the contact attempt DTO
     * @return the created contact attempt
     * @throws ContactTicketNotFoundException if the contact ticket does not exist
     * @throws ContactInfoNotFoundException if contact info cannot be found
     * @throws ContactAttemptNotFoundException if the contact attempt does not exist
     */
    ContactAttempt createContactAttempt(UUID contactTicketId, ContactAttemptDto contactAttemptDto);

    /**
     * Retrieves all linked {@link ContactAttempt} from {@link ContactTicket} by its id.
     *
     * @param contactTicketId the contact ticket id
    * @throws ContactTicketNotFoundException if the contact ticket does not exist
     * @return the list over contact attempt elements
     */
    List<ContactAttempt> getContactAttemptsForContactTicket(UUID contactTicketId);

    /**
     * Retrieves all linked {@link ContactAttempt} from {@link ContactTicket} by its id.
     *
     * @param contactTicketId the contact ticket id
     * @param contactInfoId the contact info id
     * @throws ContactTicketNotFoundException if the contact ticket does not exist
     * @return the list over contact attempt elements
     */
    List<ContactAttempt> getContactAttemptsForContactTicketAndContactInfo(UUID contactTicketId, UUID contactInfoId);

    /**
     * Partially updates an existing {@link ContactAttempt}.
     *
     * @param contactAttemptDto the DTO representing the contact attempt to be updated
     * @return the updated contact attempt
     * @throws ContactAttemptNotFoundException if the contact attempt does not exist
     */
    ContactAttempt partialUpdateContactAttempt(ContactAttemptDto contactAttemptDto);
}
