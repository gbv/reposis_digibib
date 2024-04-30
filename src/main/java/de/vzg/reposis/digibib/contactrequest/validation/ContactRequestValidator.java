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

import java.util.Set;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestBodyDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestBodyValidationException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * This class provides methods to validate contact request.
 */
public class ContactRequestValidator {

    private final Validator validator;

    /**
     * Constructs a new {@code ContactRequestValidator} instance.
     */
    public ContactRequestValidator() {
        validator = Validation.byDefaultProvider().configure().messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory().getValidator();
    }

    /**
     * Validates {@link ContactRequestBodyDto} against model.
     *
     * @param contactRequestBodyDto the contact request body DTO
     * @throws ContactRequestBodyValidationException if contact request body DTO is not valid
     */
    public void validate(ContactRequestBodyDto contactRequestBodyDto) {
        Set<ConstraintViolation<ContactRequestBodyDto>> violations = validator.validate(contactRequestBodyDto);
        if (!violations.isEmpty()) {
            throw new ContactRequestBodyValidationException(violations.iterator().next().getMessage());
        }
    }

    /**
     * Validates {@link ContactRequestDto} against model.
     *
     * @param contactRequestDto the contact request DTO
     * @throws ContactRequestValidationException if contact request DTO is not valid
     */
    public void validate(ContactRequestDto contactRequestDto) {
        Set<ConstraintViolation<ContactRequestDto>> violations = validator.validate(contactRequestDto);
        if (!violations.isEmpty()) {
            throw new ContactRequestValidationException(violations.iterator().next().getMessage());
        }
        try {
            validate(contactRequestDto.getBody());
        } catch (ContactRequestBodyValidationException e) {
            throw new ContactRequestValidationException(e);
        }
    }

    /**
     * Validates {@link ContactRequestPartialUpdateDto} against model.
     *
     * @param contactRequestDto the contact request DTO
     * @throws ContactRequestValidationException if contact request DTO is not valid
     */
    public void validate(ContactRequestPartialUpdateDto contactRequestDto) {
        Set<ConstraintViolation<ContactRequestPartialUpdateDto>> violations = validator.validate(contactRequestDto);
        if (!violations.isEmpty()) {
            throw new ContactRequestValidationException(violations.iterator().next().getMessage());
        }
        if (contactRequestDto.getBody().isPresent()) {
            try {
                validate(contactRequestDto.getBody().get());
            } catch (ContactRequestBodyValidationException e) {
                throw new ContactRequestValidationException(e);
            }
        }

    }

}
