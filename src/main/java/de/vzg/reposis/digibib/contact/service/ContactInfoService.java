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

import de.vzg.reposis.digibib.contact.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contact.exception.ContactInfoAlreadyExistsException;
import de.vzg.reposis.digibib.contact.exception.ContactInfoInvalidException;
import de.vzg.reposis.digibib.contact.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketNotFoundException;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;

/**
 * This interface defines operations related to managing {@link ContactInfo}.
 * It allows adding, retrieving, updating, and deleting contact information associated with contact tickets.
 */
public interface ContactInfoService {

    /**
     * Creates and adds {@link ContactInfo} to a {@link ContactTicket} by its id.
     *
     * @param contactTicketId the contact ticket id
     * @param contactInfoDto the contact info to be added
     * @return the created contact info
     * @throws ContactInfoInvalidException if contact info is invalid
     * @throws ContactTicketNotFoundException if the contact ticket does not exist
     * @throws ContactInfoAlreadyExistsException if the contact info already exists
     */
    ContactInfo createContactInfo(UUID contactTicketId, ContactInfoDto contactInfoDto);

    /**
     * Returns {@link ContactInfo} by the given id.
     *
     * @param contactInfoId the id of the contact info
     * @return the contact info
     * @throws ContactInfoNotFoundException if the contact info does not exist
     */
    ContactInfo getContactInfoById(UUID contactInfoId);

    /**
     * Returns a list over all {@link ContactInfo} associated by {@link ContactTicket}.
     *
     * @param contactTicketId the id of the contact ticket
     * @return the list over contact info elements
     * @throws ContactTicketNotFoundException if the contact ticket does not exist
     */
    List<ContactInfo> getContactInfosWithChildrenForContactTicket(UUID contactTicketId);

    /**
     * Updates {@link ContactInfo}.
     *
     * @param contactInfoDto the updated contact info
     * @return the updated contact info
     * @throws ContactInfoInvalidException if the contact info is invalid
     * @throws ContactInfoAlreadyExistsException if the contact info already exists
     */
    ContactInfo updateContactInfo(ContactInfoDto contactInfoDto);

    /**
     * Partially updates an existing {@link ContactInfo}.
     *
     * @param contactInfoDto the DTO representing the contact info to be updated
     * @return the updated contact info
     * @throws ContactTicketNotFoundException if the contact info does not exist
     */
    ContactInfo partialUpdateContactInfo(ContactInfoDto contactInfoDto);

    /**
     * Deletes {@link ContactInfo} by id.
     *
     * @param contactInfoId the id of the contact info to be deleted
     * @throws ContactInfoNotFoundException if the contact info cannot be found
     */
    void deleteContactInfo(UUID contactInfoId);

}
