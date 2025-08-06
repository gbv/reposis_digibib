package de.vzg.reposis.digibib.agreement.exception;

import org.mycore.common.MCRException;

public class AgreementException extends MCRException {

    private static final long serialVersionUID = 1L;

    public AgreementException(Throwable cause) {
        super(cause);
    }

    public AgreementException(String message) {
        super(message);
    }

    public AgreementException(String message, Throwable cause) {
        super(message, cause);
    }
}
