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
import de.vzg.reposis.digibib.contactrequest.exception.ContactAttemptValidationException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoAlreadyExistsException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoValidationException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestValidationException;

/**
 * This interface provides operations and actions to manage {@link ContactRequestDto}.
 * It allows creating, retrieving, updating, and deleting contact requests.
 */
public interface ContactRequestService {

    /**
     * Creates a new contact request based on the provided {@link ContactRequestDto}.
     *
     * @param contactRequestDto the DTO containing the details of the contact request to be created
     * @return the created contact request as a DTO
     * @throws ContactRequestValidationException if the contact request DTO is invalid
     */
    ContactRequestDto createContactRequest(ContactRequestDto contactRequestDto);

    /**
     * Retrieves a contact request by its ID.
     *
     * @param contactRequestId the ID of the contact request to retrieve
     * @return the corresponding contact request as DTO
     * @throws ContactRequestNotFoundException if no contact request with the specified ID is found
     */
    ContactRequestDto getContactRequestById(UUID contactRequestId);

    /**
     * Retrieves a list of all contact requests.
     *
     * @return a list of DTOs representing all contact requests
     */
    List<ContactRequestDto> getAllContactRequests();

    /**
     * Updates an existing contact request.
     *
     * @param contactRequestDto the DTO containing the updated contact request
     * @return the updated contact request as DTO after the changes have been saved
     * @throws ContactRequestValidationException if the contact request DTO is invalid
     * @throws ContactRequestNotFoundException if the contact request with the given ID is not found
     */
    ContactRequestDto updateContactRequest(ContactRequestDto contactRequestDto);

    /**
     * Partially updates an existing contact request.
     *
     * @param contactRequestId the ID of the contact request to update
     * @param contactRequestDto the DTO containing the partial updates
     * @return the updated DTO after applying the partial updates
     * @throws ContactRequestValidationException if the contact request DTO is invalid
     * @throws ContactRequestNotFoundException if the contact request with the given ID is not found
     */
    ContactRequestDto partialUpdateContactRequest(UUID contactRequestId,
        ContactRequestPartialUpdateDto contactRequestDto);

    /**
     * Deletes a contact request by its ID.
     *
     * @param contactRequestId the ID of the contact request to delete
     * @throws ContactRequestNotFoundException if the contact request with the given ID is not found
     */
    void deleteContactRequestById(UUID contactRequestId);

    /**
     * Creates a new contact info for a specific contact request.
     *
     * @param contactRequestId the ID of the contact request to which the contact info will be added
     * @param contactInfoDto the DTO containing the contact info to be created
     * @return the created contact info as DTO
     * @throws ContactInfoValidationException if contact info DTO is invalid
     * @throws ContactRequestNotFoundException if the contact request with the given ID is not found
     * @throws ContactInfoAlreadyExistsException if the contact info already exists for the given contact request
     */
    ContactInfoDto createContactInfo(UUID contactRequestId, ContactInfoDto contactInfoDto);

    /**
     * Retrieves a list of contact info entries associated with a specific contact request.
     *
     * @param contactRequestId the ID of the contact request for which the contact info is to be retrieved
     * @return a list of contact info DTOs associated with the given contact request
     * @throws ContactRequestNotFoundException if the contact request with the specified ID is not found
     */
    List<ContactInfoDto> getContactInfosById(UUID contactRequestId);

    /**
     * Creates a new contact attempt for a specific contact request.
     *
     * @param contactRequestId the ID of the contact request to which the contact info will be added
     * @param contactAttemptDto the DTO containing the contact attempt to be created
     * @return the created contact attempt as DTO
     * @throws ContactAttemptValidationException if contact attempt DTO is invalid
     * @throws ContactRequestNotFoundException if the contact request with the given ID is not found
     */
    ContactAttemptDto createContactAttempt(UUID contactRequestId, ContactAttemptDto contactAttemptDto);

    /**
     * Retrieves a list of contact attempts associated with a specific contact request.
     *
     * @param contactRequestId the ID of the contact request for which the contact attempts are to be retrieved
     * @return a list of contact attempt DTOs associated with the given contact request
     * @throws ContactRequestNotFoundException if the contact request with the specified ID is not found
     */
    List<ContactAttemptDto> getContactAttemptsById(UUID contactRequestId);

    /**
     * Retrieves a summary of the status for a specific contact request.
     *
     * @param contactRequestId the ID of the contact request for which the status summary is to be retrieved
     * @return a DTO containing the summarized status of the contact request
     * @throws ContactRequestNotFoundException if the contact request with the specified ID is not found
     */
    ContactRequestSummaryDto getStatusSummaryById(UUID contactRequestId);

}
