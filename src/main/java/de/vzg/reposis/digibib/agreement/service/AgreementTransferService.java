package de.vzg.reposis.digibib.agreement.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.config.MCRConfiguration2;

import de.vzg.reposis.digibib.agreement.model.Agreement;
import de.vzg.reposis.digibib.agreement.pdf.service.AgreementPdfService;
import de.vzg.reposis.digibib.agreement.pdf.service.AgreementPdfServiceProvider;
import de.vzg.reposis.digibib.agreement.transport.AgreementTransmitter;
import de.vzg.reposis.digibib.agreement.transport.AgreementTransportException;

public class AgreementTransferService {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String AGREEMENT_SERVICE_PROP_PREFIX = "Digibib.Agreement.Service.";

    private AgreementTransmitter transmitter;

    private AgreementPdfServiceProvider pdfServiceProvider;

    public AgreementTransferService(AgreementTransmitter transmitter,
        AgreementPdfServiceProvider pdfServiceProvider) {
        this.transmitter = transmitter;
        this.pdfServiceProvider = pdfServiceProvider;
    }

    public static AgreementTransferService obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    public static AgreementTransferService createInstance() {
        final String senderClassProperty = AGREEMENT_SERVICE_PROP_PREFIX + "Transmitter.Class";
        final AgreementTransmitter sender
            = MCRConfiguration2.<AgreementTransmitter>getInstanceOf(senderClassProperty).orElseThrow();
        return new AgreementTransferService(sender, AgreementPdfServiceProvider.obtainInstance());
    }

    public void transferAgreement(Agreement agreement) {
        final String doi = agreement.getContent().getDoi();
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            AgreementPdfService serivce = pdfServiceProvider.getPdfService(agreement.getAgreementName()).orElseThrow();
            serivce.generatePdf(agreement.getContent(), output);
            byte[] pdfBytes = output.toByteArray();
            transmitter.send(doi + ".pdf", pdfBytes);
            LOGGER.info("Agreement with DOI {} was sent successfully.", doi);
        } catch (IOException | RuntimeException e) {
            throw new AgreementTransportException("Failed to send agreement for DOI " + doi, e);
        }
    }

    private static final class InstanceHolder {
        private static final AgreementTransferService SHARED_INSTANCE = createInstance();
    }
}
