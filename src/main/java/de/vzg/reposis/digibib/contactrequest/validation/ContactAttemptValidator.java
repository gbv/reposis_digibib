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

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactAttemptValidationException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * This class provides methods to validate contact attempt.
 */
public class ContactAttemptValidator {

    private final Validator validator;

    /**
     * Constructs a new {@code ContactAttemptValidator} instance.
     */
    public ContactAttemptValidator() {
        validator = Validation.byDefaultProvider().configure().messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory().getValidator();
    }

    /**
     * Validates {@link ContactAttemptDto} against model.
     *
     * @param contactAttemptDto the contact info DTO
     * @throws ContactInfoValidationException if contact attempt DTO is not valid
     */
    public void validate(ContactAttemptDto contactAttemptDto) {
        Set<ConstraintViolation<ContactAttemptDto>> violations = validator.validate(contactAttemptDto);
        if (!violations.isEmpty()) {
            throw new ContactAttemptValidationException(violations.iterator().next().getMessage());
        }
    }

    /**
     * Validates {@link ContactAttemptPartialUpdateDto} against model.
     *
     * @param contactAttemptDto the contact info DTO
     * @throws ContactInfoValidationException if contact attempt DTO is not valid
     */
    public void validate(ContactAttemptPartialUpdateDto contactAttemptDto) {
        Set<ConstraintViolation<ContactAttemptPartialUpdateDto>> violations = validator.validate(contactAttemptDto);
        if (!violations.isEmpty()) {
            throw new ContactAttemptValidationException(violations.iterator().next().getMessage());
        }
    }
}
