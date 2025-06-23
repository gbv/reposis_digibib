package de.vzg.reposis.digibib.agreement.mapping;

import java.io.IOException;
import java.io.InputStream;

import org.mycore.common.content.MCRContent;
import org.mycore.common.content.MCRJDOMContent;
import org.mycore.common.content.transformer.MCRContentTransformerFactory;
import org.mycore.datamodel.metadata.MCRObject;

import de.vzg.reposis.digibib.agreement.exceptions.AgreementException;
import de.vzg.reposis.digibib.pdf.model.FormData;
import de.vzg.reposis.digibib.pdf.serialization.FormDataXmlMapper;
import jakarta.xml.bind.JAXBException;

/**
 * Mapper class for creating {@link FormData} instances from {@link MCRObject} instances.
 * <p>
 * This class performs an XSLT transformation on the XML representation of the MCRObject
 * using a predefined transformer, then unmarshals the transformed XML into an AgreementFormData
 * object via JAXB.
 */
public class AgreementFormDataMapper {

    private static final String TRANSFORMER_ID_PREFIX = "mycoreobject-";
    private static final String TRANSFORMER_ID_SUFFIX = "-agreement-formdata";

    /**
     * Returns the shared singleton instance of {@code AgreementContentFactory}.
     *
     * @return the shared {@code AgreementContentFactory} instance
     */
    public static AgreementFormDataMapper obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    /**
     * Creates a new instance of {@code AgreementContentFactory}.
     *
     * @return a new {@code AgreementContentFactory} instance
     */
    public static AgreementFormDataMapper createInstance() {
        return new AgreementFormDataMapper();
    }

    /**
     * Converts an MCRObject into an AgreementFormData using XSLT and JAXB mapping.
     *
     * @param obj the MCRObject to transform
     * @param agreementId the agreement id
     * @return AgreementFormData mapped from the object
     * @throws AgreementException if transformation or parsing fails
     */
    public FormData fromObject(MCRObject obj, String agreementId) {
        try {
            final MCRContent transformed = transformObject(obj, agreementId);
            return parseAgreementContent(transformed);
        } catch (IOException | JAXBException e) {
            throw new AgreementException("Unable to create AgreementFormData from object: " + obj.getId(), e);
        }
    }

    private MCRContent transformObject(MCRObject obj, String agreementId) throws IOException {
        final MCRJDOMContent input = new MCRJDOMContent(obj.createXML());
        return MCRContentTransformerFactory
            .getTransformer(TRANSFORMER_ID_PREFIX + agreementId + TRANSFORMER_ID_SUFFIX).transform(input);
    }

    private FormData parseAgreementContent(MCRContent content) throws IOException, JAXBException {
        try (InputStream is = content.getInputStream()) {
            return FormDataXmlMapper.unmarshalAgreementFormData(is);
        }
    }

    private static final class InstanceHolder {
        private static final AgreementFormDataMapper SHARED_INSTANCE = createInstance();
    }
}
