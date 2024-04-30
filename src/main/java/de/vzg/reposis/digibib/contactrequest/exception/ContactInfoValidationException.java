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
 * Exception thrown when a contact info is found to be invalid or does not meet validation criteria.
 */
public class ContactInfoValidationException extends ContactRequestException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code ContactInfoValidationException} with a default error message.
     * <p>
     * The default message is "invalid contact info", which is used to indicate that the contact information
     * provided is not valid. The default error code for this exception is "invalidContactInfo".
     * </p>
     */
    public ContactInfoValidationException() {
        this("invalid contact info");
    }

    /**
     * Constructs a new {@code ContactInfoValidationException} with a specified error message.
     * <p>
     * This constructor allows for a custom error message to be provided, which describes the specific validation
     * issue encountered with the contact information. The error code for this exception is set to "invalidContactInfo".
     * </p>
     *
     * @param message the detail message explaining the cause of the exception
     */
    public ContactInfoValidationException(String message) {
        super(message, "invalidContactInfo");
    }
}
