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

import java.util.Optional;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactAttempt;

/**
 * The ContactEventMapper class provides methods to convert between
 * {@link ContactAttempt} and {@link ContactAttemptDto}.
 */
public class ContactAttemptMapper {

    /**
     * Converts a {@link ContactAttemptDto} to a {@link ContactAttempt} entity.
     *
     * @param contactAttemptDto the DTO to be converted
     * @return the converted ContactAttempt entity
     */
    public static ContactAttempt toEntity(ContactAttemptDto contactAttemptDto) {
        final ContactAttempt contactAttempt = new ContactAttempt();
        contactAttempt.setId(contactAttemptDto.getId());
        Optional.ofNullable(contactAttemptDto.getSendDate()).map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactAttempt::setSendDate);
        Optional.ofNullable(contactAttemptDto.getErrorDate()).map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactAttempt::setErrorDate);
        Optional.ofNullable(contactAttemptDto.getSuccessDate()).map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactAttempt::setSuccessDate);
        Optional.ofNullable(contactAttemptDto.getContactInfo()).map(ContactInfoMapper::toEntity)
            .ifPresent(contactAttempt::setContactInfo);
        return contactAttempt;
    }

    /**
     * Converts a {@link ContactAttempt} entity to a {@link ContactAttemptDto}.
     *
     * @param contactAttempt the entity to be converted
     * @return the converted ContactAttemptDto
     */
    public static ContactAttemptDto toDto(ContactAttempt contactAttempt) {
        final ContactAttemptDto contactAttemptDto = new ContactAttemptDto();
        contactAttemptDto.setId(contactAttempt.getId());
        contactAttemptDto.setContactInfo(ContactInfoMapper.toDto(contactAttempt.getContactInfo()));
        Optional.ofNullable(contactAttempt.getSendDate()).map(ContactMapperUtil::localDateToDate)
            .ifPresent(contactAttemptDto::setSendDate);
        Optional.ofNullable(contactAttempt.getErrorDate()).map(ContactMapperUtil::localDateToDate)
            .ifPresent(contactAttemptDto::setErrorDate);
        Optional.ofNullable(contactAttempt.getSuccessDate()).map(ContactMapperUtil::localDateToDate)
            .ifPresent(contactAttemptDto::setSuccessDate);
        return contactAttemptDto;
    }
}
