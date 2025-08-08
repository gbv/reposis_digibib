package de.vzg.reposis.digibib.agreement.factory;

import java.io.IOException;
import java.io.InputStream;

import org.mycore.common.content.MCRContent;
import org.mycore.common.content.MCRJDOMContent;
import org.mycore.common.content.transformer.MCRContentTransformerFactory;
import org.mycore.datamodel.metadata.MCRObject;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.model.AgreementContent;
import de.vzg.reposis.digibib.agreement.xml.AgreementXmlMapper;
import jakarta.xml.bind.JAXBException;

/**
 * Factory class for creating {@link AgreementContent} instances from {@link MCRObject} instances.
 * <p>
 * This class performs an XSLT transformation on the XML representation of the MCRObject
 * using a predefined transformer, then unmarshals the transformed XML into an AgreementContent
 * object via JAXB.
 */
public class AgreementContentFactory {

    private static final String TRANSFORMER_ID = "mycoreobject-agreement";

    /**
     * Converts an MCRObject into an AgreementContent using XSLT and JAXB mapping.
     *
     * @param obj the MCRObject to transform
     * @return AgreementContent mapped from the object
     * @throws AgreementException if transformation or parsing fails
     */
    public AgreementContent fromObject(MCRObject obj) {
        try {
            MCRContent transformed = transformObject(obj);
            return parseAgreementContent(transformed);
        } catch (IOException | JAXBException e) {
            throw new AgreementException("Unable to create AgreementContent from object: " + obj.getId(), e);
        }
    }

    private MCRContent transformObject(MCRObject obj) throws IOException {
        MCRJDOMContent input = new MCRJDOMContent(obj.createXML());
        return MCRContentTransformerFactory.getTransformer(TRANSFORMER_ID).transform(input);
    }

    private AgreementContent parseAgreementContent(MCRContent content) throws IOException, JAXBException {
        try (InputStream is = content.getInputStream()) {
            return AgreementXmlMapper.unmarshalAgreementContent(is);
        }
    }
}
