package de.vzg.reposis.digibib.agreement.xml;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.mycore.common.content.MCRJAXBContent;

import de.vzg.reposis.digibib.agreement.model.Agreement;
import de.vzg.reposis.digibib.agreement.model.Author;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class AgreementXmlMapper {

    private static final JAXBContext JAXB_CONTEXT = initJaxbContext();

    private AgreementXmlMapper() {

    }

    public static Agreement transformToAgreement(InputStream stream) throws JAXBException {
        return transform(stream, Agreement.class);
    }

    public static Author transformToAuthor(InputStream stream) throws JAXBException {
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

    public static Document toDocument(Author author) throws IOException {
        return new MCRJAXBContent<Author>(JAXB_CONTEXT, author).asXML();
    }

    public static Document toDocument(Agreement agreement) throws IOException {
        return new MCRJAXBContent<Agreement>(JAXB_CONTEXT, agreement).asXML();
    }

    private static JAXBContext initJaxbContext() {
        try {
            return JAXBContext.newInstance(Agreement.class, Author.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
