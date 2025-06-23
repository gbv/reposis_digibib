package de.vzg.reposis.digibib.agreement.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.jdom2.transform.JDOMSource;
import org.junit.Test;
import org.mockito.Mockito;
import org.mycore.common.MCRTestCase;
import org.mycore.common.content.MCRContent;
import org.mycore.common.content.MCRURLContent;
import org.mycore.datamodel.metadata.MCRObject;

import de.vzg.reposis.digibib.agreement.AuthorUriResolver;
import de.vzg.reposis.digibib.agreement.model.Author;
import de.vzg.reposis.digibib.agreement.serialization.AuthorXmlMapper;
import de.vzg.reposis.digibib.pdf.model.FormData;
import de.vzg.reposis.digibib.pdf.model.FormField;

public class AgreementFormDataMapperTest extends MCRTestCase {

    private static final String INPUT_FILE_PATH = "/agreement/mods_object.xml";

    private static final String TITLE = "Title";
    private static final String AUTHOR_NAME = "Author";
    private static final String AUTHOR_EMAIL = "test@test.de";
    private static final String AUTHOR_INSTITUTE = "institue";
    private static final String AUTHOR_PHONE = "01234567899";

    private static final String FIELD_TITLE = "title";

    @Override
    protected Map<String, String> getTestProperties() {
        Map<String, String> testProperties = super.getTestProperties();
        testProperties.put("MCR.URIResolver.ModuleResolver.agreementauthor",
            AgreementTestAuthorUriResolver.class.getName());
        return testProperties;
    }

    @Test
    public void testCreateAgreementFromDataFromObject() throws JDOMException, IOException {
        final URL classResourceUrl = AgreementFormDataMapperTest.class.getResource(INPUT_FILE_PATH);
        assertNotNull(classResourceUrl);

        final MCRContent input = new MCRURLContent(classResourceUrl);
        final MCRObject object = Mockito.mock(MCRObject.class);
        Mockito.when(object.createXML()).thenReturn(input.asXML());

        final FormData formData = new AgreementFormDataMapper().fromObject(object, "default");
        assertNotNull(formData);
        final FormField titleField = formData.getFields().stream().filter(f -> Objects.equals(FIELD_TITLE, f.getName()))
            .findAny().orElseThrow();
        assertEquals(TITLE, titleField.getValue());
    }

    public static class AgreementTestAuthorUriResolver extends AuthorUriResolver {

        @Override
        public Source resolve(String href, String base) throws TransformerException {
            final Author author = new Author.Builder()
                .name(AUTHOR_NAME)
                .email(AUTHOR_EMAIL)
                .institute(AUTHOR_INSTITUTE)
                .phone(AUTHOR_PHONE)
                .build();
            try {
                return new JDOMSource(AuthorXmlMapper.toDocument(author));
            } catch (IOException e) {
                throw new TransformerException(e);
            }
        }
    }
}
