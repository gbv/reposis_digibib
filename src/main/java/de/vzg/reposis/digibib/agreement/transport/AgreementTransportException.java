package de.vzg.reposis.digibib.agreement.transport;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;

public class AgreementTransportException extends AgreementException {

    private static final long serialVersionUID = 1L;

    public AgreementTransportException(String message, Throwable e) {
        super(message, e);
    }

    public AgreementTransportException(Throwable e) {
        super(e);
    }

}
