package de.vzg.reposis.digibib.agreement.transport;

public interface AgreementTransmitter {

    void send(String filename, byte[] attachmentData) throws AgreementTransportException;
}
