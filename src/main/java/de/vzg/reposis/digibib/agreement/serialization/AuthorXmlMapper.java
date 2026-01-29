package de.vzg.reposis.digibib.agreement.serialization;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.mycore.common.content.MCRJAXBContent;

import de.vzg.reposis.digibib.agreement.model.Author;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Utility class for marshalling and unmarshalling {@link Author} objects to and from XML.
 * <p>
 * Provides static methods to convert XML streams into {@link Author} instances and to
 * convert {@link Author} objects into JDOM {@link Document} representations.
 * <p>
 * This class uses a shared {@link JAXBContext} initialized for the {@link Author} class.
 * <p>
 * This is a static utility class and should not be instantiated.
 */
public class AuthorXmlMapper {

    private static final JAXBContext JAXB_CONTEXT = initJaxbContext();

    private AuthorXmlMapper() {
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
            return JAXBContext.newInstance(Author.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
