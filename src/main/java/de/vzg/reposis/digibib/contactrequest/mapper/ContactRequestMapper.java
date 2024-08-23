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

package de.vzg.reposis.digibib.contactrequest.mapper;

import java.util.List;
import java.util.Optional;

import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestSummaryDto;
import de.vzg.reposis.digibib.contactrequest.model.ContactAttempt;
import de.vzg.reposis.digibib.contactrequest.model.ContactRequest;

/**
 * The ContactRequestMapper class provides methods to convert between
 * {@link ContactRequest} and {@link ContactRequestDto}.
 */
public class ContactRequestMapper {

    /**
     * Converts a {@link ContactRequestDto} to a {@link ContactRequest} entity.
     *
     * @param contactRequestDto the DTO to be converted
     * @return the converted entity
     */
    public static ContactRequest toEntity(ContactRequestDto contactRequestDto) {
        final ContactRequest contactRequest = new ContactRequest();
        contactRequest.setObjectId(contactRequestDto.getObjectId());
        contactRequest.setComment(contactRequestDto.getComment());
        contactRequest.setId(contactRequestDto.getId());
        Optional.ofNullable(contactRequestDto.getStatus()).map(ContactRequest.Status::valueOf)
            .ifPresent(contactRequest::setStatus);
        Optional.ofNullable(contactRequestDto.getCreated()).map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactRequest::setCreated);
        contactRequest.setCreatedBy(contactRequestDto.getCreatedBy());
        Optional.ofNullable(contactRequestDto.getBody()).map(ContactRequestBodyMapper::toEntity)
            .ifPresent(contactRequest::setBody);
        return contactRequest;
    }

    /**
     * Converts a {@link ContactRequest} entity to a {@link ContactRequestDto}.
     *
     * @param contactRequest the entity to be converted
     * @return the converted DTO
     */
    public static ContactRequestDto toDto(ContactRequest contactRequest) {
        final ContactRequestDto contactRequestDto = new ContactRequestDto();
        contactRequestDto.setId(contactRequest.getId());
        contactRequestDto.setObjectId(contactRequest.getObjectId());
        contactRequestDto.setComment(contactRequest.getComment());
        Optional.ofNullable(contactRequest.getBody()).map(ContactRequestBodyMapper::toDto)
            .ifPresent(contactRequestDto::setBody);
        contactRequestDto.setStatus(contactRequest.getStatus().toString());
        Optional.ofNullable(contactRequest.getCreated()).map(ContactMapperUtil::localDateToDate)
            .ifPresent(contactRequestDto::setCreated);
        contactRequestDto.setCreatedBy(contactRequest.getCreatedBy());
        return contactRequestDto;
    }

    /**
     * Converts a {@link ContactRequest} entity to a {@link ContactRequestSummaryDto}.
     *
     * @param contactRequest the entity to be converted
     * @return the converted DTO
     */
    public static ContactRequestSummaryDto toSummaryDto(ContactRequest contactRequest) {
        final List<String> emails = contactRequest.getEmailContactAttempts().stream()
            .map(ContactAttempt::getRecipientReference).distinct().toList();
        final String statusString = contactRequest.getStatus().toString().toLowerCase();
        final ContactRequestSummaryDto contactRequestSummaryDto = new ContactRequestSummaryDto();
        contactRequestSummaryDto.setStatusString(statusString);
        contactRequestSummaryDto.setEmails(emails);
        return contactRequestSummaryDto;
    }

}
