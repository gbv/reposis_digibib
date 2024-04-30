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
 * Exception thrown when a contact request is found to be invalid or does not meet validation criteria.
 */
public class ContactRequestValidationException extends ContactRequestException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code ContactRequestValidationException} with a default error message.
     * <p>
     * The default message "invalid contact request" is used to indicate that the contact request is
     * not valid according to the defined criteria. The error code associated with this exception is
     * "invalidContactRequest".
     * </p>
     */
    public ContactRequestValidationException() {
        this("invalid contact request");
    }

    /**
     * Constructs a new {@code ContactRequestValidationException} with a specified error message.
     * <p>
     * This constructor allows for a custom message to be provided, which describes the specific issue
     * with the contact request. The error code associated with this exception is "invalidContactRequest".
     * </p>
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ContactRequestValidationException(String message) {
        super(message, "invalidContactRequest");
    }

    /**
     * Constructs a new {@code ContactRequestValidationException} with a specified cause.
     * <p>
     * This constructor allows for a throwable cause to be provided, which represents the underlying cause
     * of the exception. The error message is set to {@code null}, and the error code is set to
     * "invalidContactRequest".
     * </p>
     *
     * @param cause the cause of the exception, or {@code null} if the cause is nonexistent or unknown
     */
    public ContactRequestValidationException(Throwable cause) {
        super(cause);
    }
}
