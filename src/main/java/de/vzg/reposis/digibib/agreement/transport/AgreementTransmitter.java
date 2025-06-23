package de.vzg.reposis.digibib.agreement.transport;

import de.vzg.reposis.digibib.agreement.exceptions.AgreementTransportException;

/**
 * Interface defining the contract for transmitting agreement data.
 * <p>
 * Implementations of this interface are responsible for sending agreement content
 * represented as a byte array, typically to some external system or storage.
 */
public interface AgreementTransmitter {

    /**
     * Sends the agreement data with the specified filename and content.
     *
     * @param filename the name of the agreement file to be sent
     * @param agreementBytes the content of the agreement as a byte array
     * @throws AgreementTransportException if there is an error during transmission
     */
    void send(String filename, byte[] agreementBytes) throws AgreementTransportException;
}
