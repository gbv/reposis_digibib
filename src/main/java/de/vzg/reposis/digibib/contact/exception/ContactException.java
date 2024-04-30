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

package de.vzg.reposis.digibib.contact.exception;

import org.mycore.common.MCRException;

/**
 * The ContactException class represents a custom exception for contact-related errors.
 */
public class ContactException extends MCRException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;

    /**
     * Constructs a new ContactException with the specified message, errorCode, and cause.
     *
     * @param message the detail message
     * @param errorCode the error code
     * @param cause the cause of the exception
     */
    public ContactException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new ContactException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ContactException(String message, Throwable cause) {
        this(message, "contactError", cause);
    }

    /**
     * Constructs a new ContactException with the specified message and errorCode.
     *
     * @param message the detail message
     * @param errorCode the error code
     */
    public ContactException(String message, String errorCode) {
        this(message, errorCode, null);
    }

    /**
     * Constructs a new ContactException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public ContactException(Throwable cause) {
        this(null, cause);
    }

    /**
     * Constructs a new ContactException with the specified message.
     *
     * @param message the detail message
     */
    public ContactException(String message) {
        super(message);
        this.errorCode = "contactError";
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
