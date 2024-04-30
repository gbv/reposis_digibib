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

package de.vzg.reposis.digibib.email.exception;

import org.mycore.common.MCRException;

/**
 * Custom exception class for handling email-related errors.
 * <p>
 * This class extends {@link MCRException} and is used to indicate errors that occur during email operations,
 * such as sending or receiving emails.
 * </p>
 */
public class EmailException extends MCRException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code EmailException} with the specified detail message and cause.
     *
     * @param message the detail message, which provides information about the reason for the exception
     * @param cause the cause of the exception, which can be another throwable that triggered this exception
     */
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code EmailException} with the specified detail message.
     *
     * @param message the detail message, which provides information about the reason for the exception
     */
    public EmailException(String message) {
        super(message);
    }
}
