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

import de.vzg.reposis.digibib.contact.dto.ContactTicketDto;
import de.vzg.reposis.digibib.contact.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketInvalidException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketStatusException;
import de.vzg.reposis.digibib.contact.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;

/**
 * This interface provides operations and actions to manage {@link ContactTicket}.
 * It allows creating, retrieving, updating, and deleting contact tickets,
 * as well as closing tickets and performing actions on them.
 */
public interface ContactTicketService {

    /**
     * Creates and adds a new {@link ContactTicket}.
     *
     * @param contactTicket the DTO representing the contact ticket to be created
     * @return the created contact ticket
     * @throws ContactTicketInvalidException if the contact ticket is invalid
     */
    ContactTicket createContactTicket(ContactTicketDto contactTicket);

    /**
     * Retrieves a {@link ContactTicket} by its id.
     *
     * @param contactTicketId the id of the contact ticket
     * @return the contact ticket
     * @throws ContactTicketNotFoundException if the contact ticket does not exist
     */
    ContactTicket getContactTicketById(UUID contactTicketId);

    /**
     * Retrieves a collection of all {@link ContactTicket} elements.
     *
     * @return a collection of contact ticket elements
     */
    List<ContactTicket> getAllContactTickets();

    /**
     * Updates an existing {@link ContactTicket}.
     *
     * @param contactTicketDto the DTO representing the contact ticket to be updated
     * @return the updated contact ticket
     * @throws ContactTicketInvalidException if the contact ticket is invalid
     * @throws ContactTicketNotFoundException if the contact ticket does not exist
     */
    ContactTicket updateContactTicket(ContactTicketDto contactTicketDto);

    /**
     * Partially updates an existing {@link ContactTicket}.
     *
     * @param contactTicketDto the DTO representing the contact ticket to be updated
     * @return the updated contact ticket
     * @throws ContactTicketInvalidException if the contact ticket is invalid
     * @throws ContactTicketNotFoundException if the contact ticket does not exist
     */
    ContactTicket partialUpdateContactTicket(ContactTicketDto contactTicketDto);

    /**
     * Deletes a {@link ContactTicket} by its id.
     *
     * @param contactTicketId the Id of the contact ticket to be deleted
     * @throws ContactTicketNotFoundException if the contact ticket does not exist
     */
    void deleteTicket(UUID contactTicketId);

    /**
     * Closes a {@link ContactTicket} and sets its status to closed, optionally sending a notification message.
     *
     * @param contactTicketId the Id of the contact ticket to be closed
     * @throws ContactTicketNotFoundException if the contact ticket does not exist
     * @throws ContactTicketStatusException if the contact ticket is already closed
     */
    void closeTicket(UUID contactTicketId);

    /**
     * Forwards a contact tickets request to a specified {@link ContactInfo} and logs this action as a {@link ContactAttempt}.
     *
     * @param contactTicketId the id of the contact ticket to be forwarded
     * @param contactInfoId the id of the contact info to which the ticket is forwarded
     * @return the created ContactAttempt entity
     * @throws ContactTicketNotFoundException if the contact ticket does not exist
     * @throws ContactInfoNotFoundException if contact info cannot be found
     */
    ContactAttempt forwardContactRequest(UUID contactTicketId, UUID contactInfoId);

}
