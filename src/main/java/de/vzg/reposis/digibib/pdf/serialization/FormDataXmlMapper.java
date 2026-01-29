package de.vzg.reposis.digibib.pdf.serialization;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.mycore.common.content.MCRJAXBContent;

import de.vzg.reposis.digibib.pdf.model.FormData;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Utility class for marshalling and unmarshalling {@link FormData} objects to and from XML.
 * <p>
 * Provides static methods to convert XML streams into {@link FormData} instances and to
 * convert {@link FormData} objects into JDOM {@link Document} representations.
 * <p>
 * This class uses a shared {@link JAXBContext} initialized for the {@link FormData} class.
 * <p>
 * This is a static utility class and should not be instantiated.
 */
public class FormDataXmlMapper {

    private static final JAXBContext JAXB_CONTEXT = initJaxbContext();

    private FormDataXmlMapper() {
        // Prevent instantiation
    }

    /**
     * Unmarshals an XML input stream into a {@link FormData} instance.
     *
     * @param stream the input stream containing XML data
     * @return the unmarshalled {@link FormData} object
     * @throws JAXBException if unmarshalling fails or the XML structure is invalid
     */
    public static FormData unmarshalAgreementFormData(InputStream stream) throws JAXBException {
        return transform(stream, FormData.class);
    }

    /**
     * Marshals an {@link FormData} object into a JDOM {@link Document}.
     *
     * @param formData the {@link FormData} instance to marshal
     * @return a JDOM {@link Document} representing the XML content
     * @throws IOException if an I/O error occurs during marshalling
     */
    public static Document toDocument(FormData formData) throws IOException {
        return new MCRJAXBContent<FormData>(JAXB_CONTEXT, formData).asXML();
    }

    private static <T> T transform(InputStream stream, Class<T> clazz) throws JAXBException {
        Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
        Object result = unmarshaller.unmarshal(stream);

        if (!clazz.isInstance(result)) {
            throw new JAXBException(
                "Unexpected type: expected " + clazz.getName() + " but got " + result.getClass().getName());
        }

        return clazz.cast(result);
    }

    private static JAXBContext initJaxbContext() {
        try {
            return JAXBContext.newInstance(FormData.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
