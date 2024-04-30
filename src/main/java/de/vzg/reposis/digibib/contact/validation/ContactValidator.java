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

package de.vzg.reposis.digibib.contact.validation;

import de.vzg.reposis.digibib.contact.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contact.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contact.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contact.dto.ContactTicketDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * This class provides a validation service.
 */
public class ContactValidator {

    private final Validator validator;

    private ContactValidator() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Returns singleton instance.
     *
     * @return instance
     */
    public static ContactValidator getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Validates {@link ContactRequestDto} against model.
     *
     * @param contactRequestDto contact request DTO
     * @return true if contact request DTO is valid
     */
    public boolean validateContactRequestDto(ContactRequestDto contactRequestDto) {
        return validator.validate(contactRequestDto).size() == 0;
    }

    /**
     * Validates {@link ContactTicketDto} against model.
     *
     * @param contactTicketDto contact ticket DTO
     * @return true if contact ticket DTO is valid
     */
    public boolean validateContactTicketDto(ContactTicketDto contactTicketDto) {
        return validator.validate(contactTicketDto).size() == 0
            && validateContactRequestDto(contactTicketDto.getContactRequest());
    }

    /**
     * Validates {@link ContactInfoDto} against model.
     *
     * @param contactInfoDto contact info DTO
     * @return true if contact info DTO is valid
     */
    public boolean validateContactInfoDto(ContactInfoDto contactInfoDto) {
        return validator.validate(contactInfoDto).size() == 0;
    }

    /**
     * Validates {@link ContactAttemptDto} against model.
     *
     * @param contactAttemptDto contact info DTO
     * @return true if contact attempt DTO is valid
     */
    public boolean validateContactAttemptDto(ContactAttemptDto contactAttemptDto) {
        return validator.validate(contactAttemptDto).size() == 0;
    }

    private static class Holder {
        private static final ContactValidator INSTANCE = new ContactValidator();
    }

}
