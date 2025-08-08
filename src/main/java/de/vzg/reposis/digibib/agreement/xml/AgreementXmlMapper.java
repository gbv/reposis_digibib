package de.vzg.reposis.digibib.agreement.xml;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.mycore.common.content.MCRJAXBContent;

import de.vzg.reposis.digibib.agreement.model.Agreement;
import de.vzg.reposis.digibib.agreement.model.AgreementContent;
import de.vzg.reposis.digibib.agreement.model.Author;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Utility class for marshalling and unmarshalling XML representations of agreement-related objects.
 * <p>
 * This class provides static methods to unmarshal XML streams into {@link AgreementContent} or {@link Author}
 * objects, and to marshal {@link Author} or {@link Agreement} objects into JDOM {@link Document} instances.
 * <p>
 * Internally uses a shared {@link JAXBContext} initialized for the relevant classes.
 */
public class AgreementXmlMapper {

    private static final JAXBContext JAXB_CONTEXT = initJaxbContext();

    private AgreementXmlMapper() {
    }

    /**
     * Unmarshals an XML input stream into an {@link AgreementContent} instance.
     *
     * @param stream the input stream containing XML data
     * @return the unmarshalled {@link AgreementContent} object
     * @throws JAXBException if unmarshalling fails or the XML structure is invalid
     */
    public static AgreementContent unmarshalAgreementContent(InputStream stream) throws JAXBException {
        return transform(stream, AgreementContent.class);
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

    private static <T> T transform(InputStream stream, Class<T> clazz) throws JAXBException {
        Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
        Object result = unmarshaller.unmarshal(stream);

        if (!clazz.isInstance(result)) {
            throw new JAXBException(
                "Unexpected type: expected " + clazz.getName() + " but got " + result.getClass().getName());
        }

        return clazz.cast(result);
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

    /**
     * Marshals an {@link Agreement} object into a JDOM {@link Document}.
     *
     * @param agreement the {@link Agreement} instance to marshal
     * @return a JDOM {@link Document} representing the XML content
     * @throws IOException if an I/O error occurs during marshalling
     */
    public static Document toDocument(Agreement agreement) throws IOException {
        return new MCRJAXBContent<Agreement>(JAXB_CONTEXT, agreement).asXML();
    }

    private static JAXBContext initJaxbContext() {
        try {
            return JAXBContext.newInstance(AgreementContent.class, Author.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
