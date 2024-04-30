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
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestBodyDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactAttemptValidationException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoValidationException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestBodyValidationException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * This class implements a {@link ContactRequestValidator}.
 */
public class ContactRequestValidatorImpl implements ContactRequestValidator {

    private final Validator validator;

    private ContactRequestValidatorImpl() {
        validator = Validation.byDefaultProvider().configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory().getValidator();
    }

    /**
     * Returns singleton instance.
     *
     * @return instance
     */
    public static ContactRequestValidatorImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void validateContactRequestBodyDto(ContactRequestBodyDto contactRequestBodyDto) {
        Set<ConstraintViolation<ContactRequestBodyDto>> violations = validator.validate(contactRequestBodyDto);
        if (!violations.isEmpty()) {
            throw new ContactRequestBodyValidationException(violations.iterator().next().getMessage());
        }
    }

    @Override
    public void validateContactRequestDto(ContactRequestDto contactRequestDto) {
        Set<ConstraintViolation<ContactRequestDto>> violations = validator.validate(contactRequestDto);
        if (!violations.isEmpty()) {
            throw new ContactRequestValidationException(violations.iterator().next().getMessage());
        }
        try {
            validateContactRequestBodyDto(contactRequestDto.getBody());
        } catch (ContactRequestBodyValidationException e) {
            throw new ContactRequestValidationException(e);
        }
    }

    @Override
    public void validateContactRequestDto(ContactRequestPartialUpdateDto contactRequestDto) {
        Set<ConstraintViolation<ContactRequestPartialUpdateDto>> violations = validator.validate(contactRequestDto);
        if (!violations.isEmpty()) {
            throw new ContactRequestValidationException(violations.iterator().next().getMessage());
        }
        if (contactRequestDto.getBody().isPresent()) {
            try {
                validateContactRequestBodyDto(contactRequestDto.getBody().get());
            } catch (ContactRequestBodyValidationException e) {
                throw new ContactRequestValidationException(e);
            }
        }

    }

    @Override
    public void validateContactInfoDto(ContactInfoDto contactInfoDto) {
        Set<ConstraintViolation<ContactInfoDto>> violations = validator.validate(contactInfoDto);
        if (!violations.isEmpty()) {
            throw new ContactInfoValidationException(violations.iterator().next().getMessage());
        }
    }

    @Override
    public void validateContactInfoDto(ContactInfoPartialUpdateDto contactInfoDto) {
        Set<ConstraintViolation<ContactInfoPartialUpdateDto>> violations = validator.validate(contactInfoDto);
        if (!violations.isEmpty()) {
            throw new ContactInfoValidationException(violations.iterator().next().getMessage());
        }
    }

    @Override
    public void validateContactAttemptDto(ContactAttemptDto contactAttemptDto) {
        Set<ConstraintViolation<ContactAttemptDto>> violations = validator.validate(contactAttemptDto);
        if (!violations.isEmpty()) {
            throw new ContactAttemptValidationException(violations.iterator().next().getMessage());
        }
    }

    @Override
    public void validateContactAttemptDto(ContactAttemptPartialUpdateDto contactAttemptDto) {
        Set<ConstraintViolation<ContactAttemptPartialUpdateDto>> violations = validator.validate(contactAttemptDto);
        if (!violations.isEmpty()) {
            throw new ContactAttemptValidationException(violations.iterator().next().getMessage());
        }
    }

    private static class Holder {
        private static final ContactRequestValidatorImpl INSTANCE = new ContactRequestValidatorImpl();
    }

}
