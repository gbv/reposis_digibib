package de.vzg.reposis.digibib.agreement.factory;

import java.io.IOException;
import java.io.InputStream;

import org.mycore.common.content.MCRContent;
import org.mycore.common.content.MCRJDOMContent;
import org.mycore.common.content.transformer.MCRContentTransformerFactory;
import org.mycore.datamodel.metadata.MCRObject;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.model.AgreementFormData;
import de.vzg.reposis.digibib.agreement.xml.AgreementXmlMapper;
import jakarta.xml.bind.JAXBException;

/**
 * Factory class for creating {@link AgreementFormData} instances from {@link MCRObject} instances.
 * <p>
 * This class performs an XSLT transformation on the XML representation of the MCRObject
 * using a predefined transformer, then unmarshals the transformed XML into an AgreementFormData
 * object via JAXB.
 */
public class AgreementFormDataFactory {

    private static final String TRANSFORMER_ID_PREFIX = "mycoreobject-";
    private static final String TRANSFORMER_ID_SUFFIX = "-formdata";

    /**
     * Returns the shared singleton instance of {@code AgreementContentFactory}.
     *
     * @return the shared {@code AgreementContentFactory} instance
     */
    public static AgreementFormDataFactory obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    /**
     * Creates a new instance of {@code AgreementContentFactory}.
     *
     * @return a new {@code AgreementContentFactory} instance
     */
    public static AgreementFormDataFactory createInstance() {
        return new AgreementFormDataFactory();
    }

    /**
     * Converts an MCRObject into an AgreementFormData using XSLT and JAXB mapping.
     *
     * @param obj the MCRObject to transform
     * @param agreementName the agreement name
     * @return AgreementFormData mapped from the object
     * @throws AgreementException if transformation or parsing fails
     */
    public AgreementFormData fromObject(MCRObject obj, String agreementName) {
        try {
            final MCRContent transformed = transformObject(obj, agreementName);
            return parseAgreementContent(transformed);
        } catch (IOException | JAXBException e) {
            throw new AgreementException("Unable to create AgreementFormData from object: " + obj.getId(), e);
        }
    }

    private MCRContent transformObject(MCRObject obj, String agreementName) throws IOException {
        final MCRJDOMContent input = new MCRJDOMContent(obj.createXML());
        return MCRContentTransformerFactory
            .getTransformer(TRANSFORMER_ID_PREFIX + agreementName + TRANSFORMER_ID_SUFFIX).transform(input);
    }

    private AgreementFormData parseAgreementContent(MCRContent content) throws IOException, JAXBException {
        try (InputStream is = content.getInputStream()) {
            return AgreementXmlMapper.unmarshalAgreementFormData(is);
        }
    }

    private static final class InstanceHolder {
        private static final AgreementFormDataFactory SHARED_INSTANCE = createInstance();
    }
}
