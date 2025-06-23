package de.vzg.reposis.digibib.pdf.processor;

import org.mycore.common.MCRException;

/**
 * Exception class for errors related to pdf processing.
 * <p>
 * This exception extends {@link MCRException} and is used to indicate
 * issues specifically arising within the Agreement domain.
 */
public class PdfFormProcessorException extends MCRException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new PdfFormProcessorException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public PdfFormProcessorException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new PdfFormProcessorException with the specified detail message.
     *
     * @param message the detail message
     */
    public PdfFormProcessorException(String message) {
        super(message);
    }

    /**
     * Constructs a new PdfFormProcessorException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public PdfFormProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
