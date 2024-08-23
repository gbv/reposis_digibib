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

import java.util.UUID;

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoAlreadyExistsException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoValidationException;

/**
 * Defines operations for managing contact information.
 * <p>
 * This service provides methods to add, retrieve, update, and delete contact info objects. Each method handles
 * the corresponding CRUD operation and ensures that appropriate exceptions are thrown for invalid or non-existent data.
 * </p>
 */
public interface ContactInfoService {

    /**
     * Retrieves a contact info by its ID.
     *
     * @param contactInfoId the ID of the contact info to retrieve
     * @return a DTOrepresenting the contact info with the specified ID
     * @throws ContactInfoNotFoundException if no contact info with the specified ID exists
     */
    ContactInfoDto getContactInfoById(UUID contactInfoId);

    /**
     * Updates an existing contact info with the provided details.
     *
     * @param contactInfoDto the contact info DTO containing the updated contact info
     * @return the updated contact info DTO reflecting the changes made to the contact info
     * @throws ContactInfoValidationException if the contact info is invalid
     * @throws ContactInfoNotFoundException if no contact info with the provided ID exists
     * @throws ContactInfoAlreadyExistsException if the updated email already exists in the contact info
     */
    ContactInfoDto updateContactInfo(ContactInfoDto contactInfoDto);

    /**
     * Partially updates an existing contact info based on the provided partial update DTO.
     *
     * @param contactInfoId the ID of the contact info to be updated
     * @param contactInfoDto the DTO containing the fields to be updated
     * @return the updated contact info DTO reflecting the changes made to the contact info
     * @throws ContactInfoValidationException if the partial update DTO is invalid
     * @throws ContactInfoNotFoundException if no contact information with the provided ID exists
     */
    ContactInfoDto partialUpdateContactInfo(UUID contactInfoId, ContactInfoPartialUpdateDto contactInfoDto);

    /**
     * Deletes the contact info by the given ID.
     *
     * @param contactInfoId the ID of the contact info to be deleted
     * @throws ContactInfoNotFoundException if no contact info with the provided ID exists
     */
    void deleteContactInfoById(UUID contactInfoId);

}
