package de.vzg.reposis.digibib.agreement.exceptions;

import org.mycore.common.MCRException;

/**
 * Exception class for errors related to Agreement processing.
 * <p>
 * This exception extends {@link MCRException} and is used to indicate
 * issues specifically arising within the Agreement domain.
 */
public class AgreementException extends MCRException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AgreementException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public AgreementException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new AgreementException with the specified detail message.
     *
     * @param message the detail message
     */
    public AgreementException(String message) {
        super(message);
    }

    /**
     * Constructs a new AgreementException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public AgreementException(String message, Throwable cause) {
        super(message, cause);
    }
}
