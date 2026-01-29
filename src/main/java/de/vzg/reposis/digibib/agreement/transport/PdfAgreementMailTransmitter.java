package de.vzg.reposis.digibib.agreement.transport;

import java.util.List;
import java.util.function.Supplier;

import org.mycore.common.config.annotation.MCRConfigurationProxy;
import org.mycore.common.config.annotation.MCRProperty;

import de.vzg.reposis.digibib.agreement.exceptions.AgreementTransportException;
import de.vzg.reposis.digibib.email.EmailClient;
import de.vzg.reposis.digibib.email.EmailClientFactory;
import de.vzg.reposis.digibib.email.exception.EmailException;
import de.vzg.reposis.digibib.email.model.EmailAttachment;
import de.vzg.reposis.digibib.email.model.EmailMessage;
import de.vzg.reposis.digibib.email.model.EmailSendRequest;

/**
 * Implementation of {@link AgreementTransmitter} that sends agreements via email.
 * <p>
 * This class uses an {@link EmailClient} to send the agreement as a PDF attachment
 * to a specified recipient.
 */
@MCRConfigurationProxy(proxyClass = PdfAgreementMailTransmitter.Factory.class)
public class PdfAgreementMailTransmitter implements AgreementTransmitter {

    private static final String CONTENT_TYPE_PDF = "application/pdf";

    private final EmailClient client;

    private final String recipient;

    /**
     * Constructs a new {@code AgreementMailTransmitter} with the specified email client and recipient.
     *
     * @param client the email client used to send emails
     * @param recipient the recipient email address
     */
    public PdfAgreementMailTransmitter(EmailClient client, String recipient) {
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

    /**
     * Factory class for creating {@link PdfAgreementMailTransmitter} instances
     * with properties injected from MyCoRe configuration.
     */
    public static final class Factory implements Supplier<PdfAgreementMailTransmitter> {

        /**
         * The recipient email address for the mail transmitter.
         */
        @MCRProperty(name = "Recipient")
        public String recipient;

        /**
         * The name of the email client to use, as configured in MyCoRe.
         */
        @MCRProperty(name = "EmailClient")
        public String clientName;

        @Override
        public PdfAgreementMailTransmitter get() {
            return new PdfAgreementMailTransmitter(EmailClientFactory.obstainInstance(clientName), recipient);
        }

    }

}
