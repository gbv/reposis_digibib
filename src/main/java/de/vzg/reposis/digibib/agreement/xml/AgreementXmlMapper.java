package de.vzg.reposis.digibib.agreement.xml;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.mycore.common.content.MCRJAXBContent;

import de.vzg.reposis.digibib.agreement.model.AgreementFormData;
import de.vzg.reposis.digibib.agreement.model.Author;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Utility class for marshalling and unmarshalling XML representations of agreement-related objects.
 * <p>
 * This class provides static methods to unmarshal XML streams into {@link AgreementFormData} or {@link Author}
 * objects, and to marshal {@link Author} or {@link AgreementFormData} objects into JDOM {@link Document} instances.
 * <p>
 * Internally uses a shared {@link JAXBContext} initialized for the relevant classes.
 */
public class AgreementXmlMapper {

    private static final JAXBContext JAXB_CONTEXT = initJaxbContext();

    private AgreementXmlMapper() {
    }

    /**
     * Unmarshals an XML input stream into an {@link AgreementFormData} instance.
     *
     * @param stream the input stream containing XML data
     * @return the unmarshalled {@link AgreementFormData} object
     * @throws JAXBException if unmarshalling fails or the XML structure is invalid
     */
    public static AgreementFormData unmarshalAgreementFormData(InputStream stream) throws JAXBException {
        return transform(stream, AgreementFormData.class);
    }

    /**
     * Unmarshals an XML input stream into an {@link Author} instance.
     *
     * @param stream the input stream containing XML data
     * @return the unmarshalled {@link Author} object
     * @throws JAXBException if unmarshalling fails or the XML structure is invalid
     */
    public static Author unmarshalAuthor(InputStream stream) throws JAXBException {
        return transform(stream, Author.class);
    }

    /**
     * Marshals an {@link AgreementFormData} object into a JDOM {@link Document}.
     *
     * @param author the {@link AgreementFormData} instance to marshal
     * @return a JDOM {@link Document} representing the XML content
     * @throws IOException if an I/O error occurs during marshalling
     */
    public static Document toDocument(AgreementFormData formData) throws IOException {
        return new MCRJAXBContent<AgreementFormData>(JAXB_CONTEXT, formData).asXML();
    }

    /**
     * Marshals an {@link Author} object into a JDOM {@link Document}.
     *
     * @param author the {@link Author} instance to marshal
     * @return a JDOM {@link Document} representing the XML content
     * @throws IOException if an I/O error occurs during marshalling
     */
    public static Document toDocument(Author author) throws IOException {
        return new MCRJAXBContent<Author>(JAXB_CONTEXT, author).asXML();
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
            return JAXBContext.newInstance(AgreementFormData.class, Author.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
