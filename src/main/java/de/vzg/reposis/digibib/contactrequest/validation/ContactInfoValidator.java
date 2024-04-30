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

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * This class provides methods to validate contact info.
 */
public class ContactInfoValidator {

    private final Validator validator;

    /**
     * Constructs a new {@code ContactInfoValidator} instance.
     */
    public ContactInfoValidator() {
        validator = Validation.byDefaultProvider().configure().messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory().getValidator();
    }

    /**
     * Validates {@link ContactInfoDto} against model.
     *
     * @param contactInfoDto the contact info DTO
     * @throws ContactInfoValidationException if contact info DTO is not valid
     */
    public void validate(ContactInfoDto contactInfoDto) {
        Set<ConstraintViolation<ContactInfoDto>> violations = validator.validate(contactInfoDto);
        if (!violations.isEmpty()) {
            throw new ContactInfoValidationException(violations.iterator().next().getMessage());
        }
    }

    /**
     * Validates {@link ContactInfoPartialUpdateDto} against model.
     *
     * @param contactInfoDto the contact info DTO
     * @throws ContactInfoValidationException if contact info DTO is not valid
     */
    public void validate(ContactInfoPartialUpdateDto contactInfoDto) {
        Set<ConstraintViolation<ContactInfoPartialUpdateDto>> violations = validator.validate(contactInfoDto);
        if (!violations.isEmpty()) {
            throw new ContactInfoValidationException(violations.iterator().next().getMessage());
        }
    }

}
