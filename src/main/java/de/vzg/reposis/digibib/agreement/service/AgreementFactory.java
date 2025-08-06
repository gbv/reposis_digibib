package de.vzg.reposis.digibib.agreement.service;

import java.io.IOException;
import java.io.InputStream;

import org.mycore.common.content.MCRContent;
import org.mycore.common.content.MCRJDOMContent;
import org.mycore.common.content.transformer.MCRContentTransformerFactory;
import org.mycore.datamodel.metadata.MCRObject;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.model.Agreement;
import de.vzg.reposis.digibib.agreement.xml.AgreementXmlMapper;
import jakarta.xml.bind.JAXBException;

public class AgreementFactory {

    private static final String TRANSFORMER_ID = "mycoreobject-agreement";

    private AgreementFactory() {
    }

    public static Agreement fromObject(MCRObject obj) {
        final MCRJDOMContent input = new MCRJDOMContent(obj.createXML());
        try {
            final MCRContent result = MCRContentTransformerFactory.getTransformer(TRANSFORMER_ID).transform(input);
            try (InputStream is = result.getInputStream()) {
                return AgreementXmlMapper.transformToAgreement(is);
            }
        } catch (IOException | JAXBException e) {
            throw new AgreementException("Error while creating agreement", e);
        }
    }
}
