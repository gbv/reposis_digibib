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

/**
 * Facade for managing access to various validators in the application.
 */
public class ValidatorFacade {

    private final ContactRequestValidator contactRequestValidator;

    private final ContactInfoValidator contactInfoValidator;

    private final ContactAttemptValidator contactAttemptValidator;

    /**
     * Constructs new {@link ValidatorFacade} instance with single validators.
     *
     * @param contactRequestValidator the contact request validator
     * @param contactInfoValidator the contact info validator
     * @param contactAttemptValidator the contact attempt validator
     */
    public ValidatorFacade(ContactRequestValidator contactRequestValidator, ContactInfoValidator contactInfoValidator,
        ContactAttemptValidator contactAttemptValidator) {
        this.contactRequestValidator = contactRequestValidator;
        this.contactInfoValidator = contactInfoValidator;
        this.contactAttemptValidator = contactAttemptValidator;
    }

    /**
     * Returns the {@link ContactRequestValidator}.
     *
     * @return the validator
     */
    public ContactRequestValidator getContactRequestValidator() {
        return contactRequestValidator;
    }

    /**
     * Returns the {@link ContactInfoValidator}.
     *
     * @return the validator
     */
    public ContactInfoValidator getContactInfoValidator() {
        return contactInfoValidator;
    }

    /**
     * Returns the {@link ContactAttemptValidator}.
     *
     * @return the validator
     */
    public ContactAttemptValidator getContactAttemptValidator() {
        return contactAttemptValidator;
    }

}
