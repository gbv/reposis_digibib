package de.vzg.reposis.digibib.agreement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.vzg.reposis.digibib.agreement.model.Agreement;
import de.vzg.reposis.digibib.agreement.model.AgreementContent;
import de.vzg.reposis.digibib.agreement.pdf.provider.AgreementPdfServiceProvider;
import de.vzg.reposis.digibib.agreement.pdf.service.AgreementPdfService;
import de.vzg.reposis.digibib.agreement.transport.AgreementTransmitter;
import de.vzg.reposis.digibib.agreement.transport.AgreementTransportException;

public class AgreementTransferServiceTest {

    private static final String AGREEMENT_NAME = "testAgreement";
    private static final String DOI = "doi-123";

    private AgreementTransmitter transmitter;
    private AgreementPdfServiceProvider pdfCreatorFactory;
    private AgreementPdfService pdfCreator;

    private AgreementTransferService agreementService;

    @Before
    public void setUp() {
        transmitter = mock(AgreementTransmitter.class);
        pdfCreatorFactory = mock(AgreementPdfServiceProvider.class);
        pdfCreator = mock(AgreementPdfService.class);

        agreementService = new AgreementTransferService(transmitter, pdfCreatorFactory);
    }

    @Test
    public void testTransferAgreement_success() throws Exception {
        final Agreement agreement = mock(Agreement.class);
        final AgreementContent content = mock(AgreementContent.class);

        when(agreement.getContent()).thenReturn(content);
        when(content.getDoi()).thenReturn(DOI);
        when(agreement.getAgreementName()).thenReturn(AGREEMENT_NAME);
        when(pdfCreatorFactory.getPdfService(AGREEMENT_NAME)).thenReturn(pdfCreator);

        doAnswer(invocation -> {
            ByteArrayOutputStream outputStream = invocation.getArgument(1);
            outputStream.write("PDF content".getBytes());
            return null;
        }).when(pdfCreator).generatePdf(eq(agreement), any(ByteArrayOutputStream.class));

        agreementService.transferAgreement(agreement);
        verify(pdfCreatorFactory).getPdfService(AGREEMENT_NAME);
        verify(pdfCreator).generatePdf(eq(agreement), any(ByteArrayOutputStream.class));
        verify(transmitter).send(eq(DOI), any(byte[].class));
    }

    @Test(expected = AgreementTransportException.class)
    public void testTransferAgreement_pdfCreatorThrowsException() throws Exception {
        final Agreement agreement = mock(Agreement.class);
        final AgreementContent content = mock(AgreementContent.class);

        when(agreement.getContent()).thenReturn(content);
        when(content.getDoi()).thenReturn(DOI);

        when(pdfCreatorFactory.getPdfService(AGREEMENT_NAME)).thenReturn(pdfCreator);

        doThrow(new IOException("PDF generation failed")).when(pdfCreator).generatePdf(eq(agreement),
            any(ByteArrayOutputStream.class));

        agreementService.transferAgreement(agreement);
    }

    @Test(expected = AgreementTransportException.class)
    public void testTransferAgreement_transmitterThrowsException() throws Exception {
        final Agreement agreement = mock(Agreement.class);
        final AgreementContent content = mock(AgreementContent.class);

        when(agreement.getContent()).thenReturn(content);
        when(content.getDoi()).thenReturn(DOI);

        when(pdfCreatorFactory.getPdfService(AGREEMENT_NAME)).thenReturn(pdfCreator);

        doAnswer(invocation -> {
            ByteArrayOutputStream outputStream = invocation.getArgument(1);
            outputStream.write("PDF content".getBytes());
            return null;
        }).when(pdfCreator).generatePdf(eq(agreement), any(ByteArrayOutputStream.class));

        doThrow(new RuntimeException("Transmit failed")).when(transmitter).send(eq(DOI), any(byte[].class));

        agreementService.transferAgreement(agreement);
    }
}
