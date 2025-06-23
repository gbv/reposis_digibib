package de.vzg.reposis.digibib.agreement.exceptions;

/**
 * Exception indicating an error occurred during agreement transport operations.
 * <p>
 * This exception is a specialized subclass of {@link AgreementException} used to signal
 * problems encountered while transmitting agreements, such as network failures or email sending errors.
 */
public class AgreementTransportException extends AgreementException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code AgreementTransportException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param e the cause of this exception
     */
    public AgreementTransportException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Constructs a new {@code AgreementTransportException} with the specified cause.
     *
     * @param e the cause of this exception
     */
    public AgreementTransportException(Throwable e) {
        super(e);
    }

}
