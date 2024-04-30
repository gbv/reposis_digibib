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

package de.vzg.reposis.digibib.contactrequest.validation;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestBodyDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoValidationException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestBodyValidationException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestValidationException;

/**
 * This interface provides a validation methods.
 */
public interface ContactRequestValidator {

    /**
     * Validates {@link ContactRequestBodyDto} against model.
     *
     * @param contactRequestBodyDto contact request body DTO
     * @throws ContactRequestBodyValidationException if contact request body DTO is not valid
     */
    void validateContactRequestBodyDto(ContactRequestBodyDto contactRequestBodyDto);

    /**
     * Validates {@link ContactRequestDto} against model.
     *
     * @param contactRequestDto contact request DTO
     * @throws ContactRequestValidationException if contact request DTO is not valid
     */
    void validateContactRequestDto(ContactRequestDto contactRequestDto);

    /**
     * Validates {@link ContactRequestPartialUpdateDto} against model.
     *
     * @param contactRequestDto contact request DTO
     * @throws ContactRequestValidationException if contact request DTO is not valid
     */
    void validateContactRequestDto(ContactRequestPartialUpdateDto contactRequestDto);

    /**
     * Validates {@link ContactInfoDto} against model.
     *
     * @param contactInfoDto contact info DTO
     * @throws ContactInfoValidationException if contact info DTO is not valid
     */
    void validateContactInfoDto(ContactInfoDto contactInfoDto);

    /**
     * Validates {@link ContactInfoPartialUpdateDto} against model.
     *
     * @param contactInfoDto contact info DTO
     * @throws ContactInfoValidationException if contact info DTO is not valid
     */
    void validateContactInfoDto(ContactInfoPartialUpdateDto contactInfoDto);

    /**
     * Validates {@link ContactAttemptDto} against model.
     *
     * @param contactAttemptDto contact info DTO
     * @throws ContactInfoValidationException if contact attempt DTO is not valid
     */
    void validateContactAttemptDto(ContactAttemptDto contactAttemptDto);

    /**
     * Validates {@link ContactAttemptPartialUpdateDto} against model.
     *
     * @param contactAttemptDto contact info DTO
     * @throws ContactInfoValidationException if contact attempt DTO is not valid
     */
    void validateContactAttemptDto(ContactAttemptPartialUpdateDto contactAttemptDto);

}
