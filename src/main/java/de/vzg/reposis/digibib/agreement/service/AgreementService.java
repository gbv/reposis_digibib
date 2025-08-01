package de.vzg.reposis.digibib.agreement.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.config.MCRConfiguration2;

import de.vzg.reposis.digibib.agreement.model.Agreement;
import de.vzg.reposis.digibib.agreement.pdf.AgreementPdfCreatorFactory;
import de.vzg.reposis.digibib.agreement.transport.AgreementTransmitter;
import de.vzg.reposis.digibib.agreement.transport.AgreementTransportException;

public class AgreementService {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String AGREEMENT_SERVICE_PROP_PREFIX = "Digibib.AgreementService.";

    private AgreementTransmitter transmitter;

    private AgreementPdfCreatorFactory pdfCreatorFactory;

    public AgreementService(AgreementTransmitter transmitter, AgreementPdfCreatorFactory pdfCreatorFactory) {
        this.transmitter = transmitter;
        this.pdfCreatorFactory = pdfCreatorFactory;
    }

    public static AgreementService obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    public static AgreementService createInstance() {
        final String senderClassProperty = AGREEMENT_SERVICE_PROP_PREFIX + "Transmitter.Class";
        final AgreementTransmitter sender
            = MCRConfiguration2.<AgreementTransmitter>getInstanceOf(senderClassProperty).orElseThrow();
        final String creatorFactoryClassProperty = AGREEMENT_SERVICE_PROP_PREFIX + "PdfCreatorFactory.Class";
        final AgreementPdfCreatorFactory creatorFactory
            = MCRConfiguration2.<AgreementPdfCreatorFactory>getInstanceOf(creatorFactoryClassProperty).orElseThrow();
        return new AgreementService(sender, creatorFactory);
    }

    public void transferAgreement(Agreement agreement) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            pdfCreatorFactory.getCreatorFor(agreement).createPdf(agreement, output);
            byte[] pdfBytes = output.toByteArray();
            transmitter.send(agreement.doi(), pdfBytes);
            LOGGER.info("Agreement with DOI {} was sent successfully.", agreement.doi());
        } catch (IOException | RuntimeException e) {
            throw new AgreementTransportException("Failed to send agreement for DOI " + agreement.doi(), e);
        }
    }

    private static final class InstanceHolder {
        private static final AgreementService SHARED_INSTANCE = createInstance();
    }
}
