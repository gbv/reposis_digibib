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

import org.mycore.common.MCRException;

/**
 * Base class for exceptions related to contact request errors.
 * <p>
 * {@code ContactRequestException} serves as a general exception class for errors related to contact requests.
 * It extends {@link MCRException} and provides additional functionality to handle error codes associated with
 * specific contact request errors.
 * </p>
 */
public class ContactRequestException extends MCRException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;

    /**
     * Constructs a new {@code ContactRequestException} with the specified detail message, error code, and cause.
     * <p>
     * This constructor allows you to specify an error message, an error code, and the underlying cause of the
     * exception. It is useful for providing detailed information about the error and its context.
     * </p>
     *
     * @param message the detail message, which provides more information about the exception
     * @param errorCode a specific code representing the type of error
     * @param cause the cause of the exception, which can be another throwable that triggered this exception
     */
    public ContactRequestException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new {@code ContactRequestException} with the specified detail message and cause.
     * <p>
     * This constructor is used when you want to provide an error message and specify the underlying cause
     * of the exception. The error code is set to a default value of "contactError".
     * </p>
     *
     * @param message the detail message, which provides more information about the exception
     * @param cause the cause of the exception, which can be another throwable that triggered this exception
     */
    public ContactRequestException(String message, Throwable cause) {
        this(message, "contactError", cause);
    }

    /**
     * Constructs a new {@code ContactRequestException} with the specified detail message and error code.
     * <p>
     * This constructor allows you to provide an error message and a specific error code. The cause of the
     * exception is set to {@code null}.
     * </p>
     *
     * @param message the detail message, which provides more information about the exception
     * @param errorCode a specific code representing the type of error
     */
    public ContactRequestException(String message, String errorCode) {
        this(message, errorCode, null);
    }

    /**
     * Constructs a new {@code ContactRequestException} with the specified cause.
     * <p>
     * This constructor is used when the cause of the exception is known but no specific error message or code is provided.
     * The message is set to {@code null} and the error code is set to "contactError".
     * </p>
     *
     * @param cause the cause of the exception, which can be another throwable that triggered this exception
     */
    public ContactRequestException(Throwable cause) {
        this(null, cause);
    }

    /**
     * Constructs a new {@code ContactRequestException} with the specified detail message.
     * <p>
     * This constructor is used when only a detail message is provided. The error code is set to the default value "contactError".
     * </p>
     *
     * @param message the detail message, which provides more information about the exception
     */
    public ContactRequestException(String message) {
        super(message);
        this.errorCode = "contactError";
    }

    /**
     * Returns the error code associated with this exception.
     * <p>
     * The error code can be used to identify the specific type of error that occurred and to perform error handling or
     * logging based on the error type.
     * </p>
     *
     * @return the error code representing the type of error
     */
    public String getErrorCode() {
        return errorCode;
    }
}
