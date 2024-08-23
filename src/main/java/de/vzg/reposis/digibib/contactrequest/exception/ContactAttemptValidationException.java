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

package de.vzg.reposis.digibib.contactrequest.exception;

/**
 * Exception thrown when a contact attempt is found to be invalid or does not meet validation criteria.
 */
public class ContactAttemptValidationException extends ContactRequestException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code ContactAttemptValidationException} with a default message.
     * <p>
     * The default message is "invalid contact attempt", which indicates that the contact attempt did not pass validation.
     * </p>
     */
    public ContactAttemptValidationException() {
        this("invalid contact attempt");
    }

    /**
     * Constructs a new {@code ContactAttemptValidationException} with the specified detail message.
     * <p>
     * This constructor allows for a custom message to be provided, which describes the specific validation issue
     * with the contact attempt.
     * </p>
     *
     * @param message the detail message, which provides additional information about the validation error
     */
    public ContactAttemptValidationException(String message) {
        super(message, "invalidContactAttempt");
    }
}
