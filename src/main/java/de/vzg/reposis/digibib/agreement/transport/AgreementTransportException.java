package de.vzg.reposis.digibib.agreement.transport;

import org.mycore.common.MCRException;

public class AgreementTransportException extends MCRException {

    private static final long serialVersionUID = 1L;

    public AgreementTransportException(String message, Throwable e) {
        super(message, e);
    }

    public AgreementTransportException(Throwable e) {
        super(e);
    }

}
