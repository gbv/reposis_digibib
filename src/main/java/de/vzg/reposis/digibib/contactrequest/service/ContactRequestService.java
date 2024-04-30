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

import java.util.List;
import java.util.UUID;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestSummaryDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactAttemptNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoAlreadyExistsException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoValidationException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestValidationException;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactRequest;

/**
 * This interface provides operations and actions to manage {@link ContactRequest}.
 * It allows creating, retrieving, updating, and deleting contact requests.
 */
public interface ContactRequestService {

    /**
     * Creates and adds a new {@link ContactRequest}.
     *
     * @param contactRequestDto the DTO representing the contact request to be created
     * @return the created contact request
     * @throws ContactRequestValidationException if the contact request validation fails
     */
    ContactRequestDto createContactRequest(ContactRequestDto contactRequestDto);

    /**
     * Retrieves a {@link ContactRequest} by its id.
     *
     * @param contactRequestId the id of the contact request
     * @return the contact request
     * @throws ContactRequestNotFoundException if the contact request does not exist
     */
    ContactRequestDto getContactRequestById(UUID contactRequestId);

    /**
     * Retrieves a collection of all {@link ContactRequest} elements.
     *
     * @return a collection of contact request elements
     */
    List<ContactRequestDto> getAllContactRequests();

    /**
     * Updates an existing {@link ContactRequest}.
     *
     * @param contactRequestDto the DTO representing the contact request to be updated
     * @return the updated contact request DTO
     * @throws IllegalArgumentException if contact request id is null
     * @throws ContactRequestValidationException if the contact request validation fails
     * @throws ContactRequestNotFoundException if the contact request does not exist
     */
    ContactRequestDto updateContactRequest(ContactRequestDto contactRequestDto);

    /**
     * Partially updates an existing {@link ContactRequest}.
     *
     * @param contactRequestId the contact request id
     * @param contactRequestDto the DTO representing the contact request to be updated
     * @return the updated contact request
     * @throws ContactRequestValidationException if the contact request validation fails
     * @throws ContactRequestNotFoundException if the contact request does not exist
     */
    ContactRequestDto partialUpdateContactRequest(UUID contactRequestId,
        ContactRequestPartialUpdateDto contactRequestDto);

    /**
     * Deletes a {@link ContactRequest} by its id.
     *
     * @param contactRequestId the id of the contact request to be deleted
     * @throws ContactRequestNotFoundException if the contact request does not exist
     */
    void deleteContactRequestById(UUID contactRequestId);

    /**
     * Creates and adds {@link ContactInfo} to a {@link ContactRequest} by its id.
     *
     * @param contactRequestId the contact request id
     * @param contactInfoDto the contact info to be added
     * @return the created contact info
     * @throws ContactInfoValidationException if contact info validation fails
     * @throws ContactRequestNotFoundException if the contact request does not exist
     * @throws ContactInfoAlreadyExistsException if the contact info already exists
     */
    ContactInfoDto createContactInfo(UUID contactRequestId, ContactInfoDto contactInfoDto);

    /**
     * Retrieves a collection of all {@link ContactInfo} elements by id.
     *
     * @param contactRequestId the contact request id
     * @return list over contact info elements
     * @throws ContactRequestNotFoundException if the contact request does not exist
     */
    List<ContactInfoDto> getContactInfosById(UUID contactRequestId);

    /**
     * Creates and adds {@link ContactAttempt} to a {@link ContactRequest} by its id.
     *
     * @param contactRequestId the contact request id
     * @param contactInfoId the contact info id
     * @param contactAttemptDto the contact attempt DTO
     * @return the created contact attempt
     * @throws ContactRequestNotFoundException if the contact request does not exist
     * @throws ContactInfoNotFoundException if contact info cannot be found
     * @throws ContactAttemptNotFoundException if the contact attempt does not exist
     */
    ContactAttemptDto createContactAttempt(UUID contactRequestId, UUID contactInfoId,
        ContactAttemptDto contactAttemptDto);

    /**
     * Retrieves all linked {@link ContactAttempt} from {@link ContactRequest} by its id.
     *
     * @param contactRequestId the contact request id
    * @throws ContactRequestNotFoundException if the contact request does not exist
     * @return the list over contact attempt elements
     */
    List<ContactAttemptDto> getContactAttemptsById(UUID contactRequestId);

    /**
     * Forwards a contact request to a specified {@link ContactInfo} and logs this action as a {@link ContactAttempt}.
     *
     * @param contactRequestId the id of the contact id to be forwarded
     * @param contactInfoId the id of the contact info to which the request is forwarded
     * @return the created ContactAttempt entity
     * @throws ContactRequestNotFoundException if the contact request does not exist
     * @throws ContactInfoNotFoundException if contact info cannot be found
     */
    ContactAttemptDto forwardContactRequestById(UUID contactRequestId, UUID contactInfoId);

    /**
     * Returns {@link ContactRequestSummaryDto} for request by id.
     *
     * @param contactRequestId the contact request id
     * @return summary DTO
     */
    ContactRequestSummaryDto getStatusSummaryById(UUID contactRequestId);

}
