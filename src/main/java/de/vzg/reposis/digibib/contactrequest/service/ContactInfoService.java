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
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestNotFoundException;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;

/**
 * This interface defines operations related to managing {@link ContactInfo}.
 * It allows adding, retrieving, updating, and deleting contact information.
 */
public interface ContactInfoService {

    /**
     * Returns {@link ContactInfo} by the given id.
     *
     * @param contactInfoId the id of the contact info
     * @return the contact info
     * @throws ContactInfoNotFoundException if the contact info does not exist
     */
    ContactInfoDto getContactInfoById(UUID contactInfoId);

    /**
     * Updates {@link ContactInfo}.
     *
     * @param contactInfoDto the updated contact info
     * @return the updated contact info
     * @throws IllegalArgumentException if contact id is null
     * @throws ContactInfoValidationException if the contact info is invalid
     * @throws ContactInfoAlreadyExistsException if the contact info already exists
     */
    ContactInfoDto updateContactInfo(ContactInfoDto contactInfoDto);

    /**
     * Partially updates an existing {@link ContactInfo}.
     *
     * @param contactInfoId the id of the contact info
     * @param contactInfoDto the DTO representing the contact info to be updated
     * @return the updated contact info
     * @throws ContactRequestNotFoundException if the contact info does not exist
     */
    ContactInfoDto partialUpdateContactInfo(UUID contactInfoId, ContactInfoPartialUpdateDto contactInfoDto);

    /**
     * Deletes {@link ContactInfo} by id.
     *
     * @param contactInfoId the id of the contact info to be deleted
     * @throws ContactInfoNotFoundException if the contact info cannot be found
     */
    void deleteContactInfoById(UUID contactInfoId);

}
