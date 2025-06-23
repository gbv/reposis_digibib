package de.vzg.reposis.digibib.agreement.transport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import de.vzg.reposis.digibib.email.EmailClient;
import de.vzg.reposis.digibib.email.model.EmailSendRequest;

public class PDFAgreementMailTransmitterTest {

    private static final String AGREEMENT_NAME = "testAgreement.pdf";
    private static final String RECIPIENT = "test@mycore.de";

    private AgreementTransmitter transmitter;
    private EmailClient client;

    @Before
    public void setUp() {
        client = mock(EmailClient.class);
        transmitter = new PdfAgreementMailTransmitter(client, RECIPIENT);
    }

    @Test
    public void testTransferAgreement_success() throws Exception {
        final byte[] agreementData = new byte[] { 1, 2, 3, 4 };
        doNothing().when(client).sendEmail(any(EmailSendRequest.class));
        transmitter.send(AGREEMENT_NAME, agreementData);
        verify(client).sendEmail(any(EmailSendRequest.class));
    }

    @Test(expected = RuntimeException.class)
    public void testTransferAgreement_transmitterThrowsException() throws Exception {
        final byte[] agreementData = new byte[] { 1, 2, 3, 4 };
        doThrow(new RuntimeException("Transport error")).when(client).sendEmail(any(EmailSendRequest.class));
        transmitter.send(AGREEMENT_NAME, agreementData);
    }
}
