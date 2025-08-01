package de.vzg.reposis.digibib.agreement.transport.impl;

import java.util.List;
import java.util.function.Supplier;

import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRProperty;

import de.vzg.reposis.digibib.agreement.transport.AgreementTransmitter;
import de.vzg.reposis.digibib.agreement.transport.AgreementTransportException;
import de.vzg.reposis.digibib.email.EmailClient;
import de.vzg.reposis.digibib.email.EmailClientFactory;
import de.vzg.reposis.digibib.email.exception.EmailException;
import de.vzg.reposis.digibib.email.model.EmailAttachment;
import de.vzg.reposis.digibib.email.model.EmailMessage;
import de.vzg.reposis.digibib.email.model.EmailSendRequest;

// TODO configuration
@MCRConfigurationProxy(proxyClass = AgreementMailTransmitter.Factory.class)
public class AgreementMailTransmitter implements AgreementTransmitter {

    private static final String CONTENT_TYPE_PDF = "application/pdf";

    private final EmailClient client;

    private final String recipient;

    public AgreementMailTransmitter(EmailClient client, String recipient) {
        this.client = client;
        this.recipient = recipient;
    }

    @Override
    public void send(String filename, byte[] attachmentData) throws AgreementTransportException {
        try {
            final EmailMessage message = new EmailMessage.Builder(recipient).build();
            final EmailAttachment attachment
                = new EmailAttachment(filename, CONTENT_TYPE_PDF, attachmentData);
            client.sendEmail(new EmailSendRequest(message, List.of(attachment)));
        } catch (EmailException e) {
            throw new AgreementTransportException(e);
        }
    }

    public static final class Factory implements Supplier<AgreementMailTransmitter> {

        @MCRProperty(name = "Recipient")
        public String recipient;

        @MCRProperty(name = "EmailClient.Name")
        public String clientName;

        @Override
        public AgreementMailTransmitter get() {
            return new AgreementMailTransmitter(EmailClientFactory.obstainInstance(clientName), recipient);
        }

    }

}
