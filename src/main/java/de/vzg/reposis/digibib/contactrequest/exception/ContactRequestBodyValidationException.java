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
 * Exception thrown when a contact request body is found to be invalid or does not meet validation criteria.
 */
public class ContactRequestBodyValidationException extends ContactRequestException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code ContactRequestBodyValidationException} with a default error message.
     * <p>
     * The default message "invalid contact request body" is used to indicate that the contact request body
     * provided is not valid according to the defined criteria. The default error code for this exception
     * is "invalidContactRequestBody".
     * </p>
     */
    public ContactRequestBodyValidationException() {
        this("invalid contact request body");
    }

    /**
     * Constructs a new {@code ContactRequestBodyValidationException} with a specified error message.
     * <p>
     * This constructor allows for a custom error message to be provided, which describes the specific issue
     * with the contact request body. The error code for this exception is set to "invalidContactRequestBody".
     * </p>
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ContactRequestBodyValidationException(String message) {
        super(message, "invalidContactRequestBody");
    }
}
