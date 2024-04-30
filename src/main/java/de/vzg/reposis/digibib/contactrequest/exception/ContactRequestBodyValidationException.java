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
 * This class represents an exception that is thrown
 * when a contact request is invalid.
 */
public class ContactRequestBodyValidationException extends ContactRequestException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ContactRequestBodyValidationException with a default message.
     */
    public ContactRequestBodyValidationException() {
        this("invalid contact request body");
    }

    /**
     * Constructs a new ContactRequestBodyValidationException with the specified message.
     *
     * @param message the detail message
     */
    public ContactRequestBodyValidationException(String message) {
        super(message, "invalidContactRequestBody");
    }
}
