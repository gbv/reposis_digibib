package de.vzg.reposis.digibib.agreement.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;

import org.jdom2.JDOMException;
import org.junit.Test;
import org.mockito.Mockito;
import org.mycore.common.MCRTestCase;
import org.mycore.common.content.MCRContent;
import org.mycore.common.content.MCRURLContent;
import org.mycore.datamodel.metadata.MCRObject;

import de.vzg.reposis.digibib.agreement.factory.AgreementContentFactory;
import de.vzg.reposis.digibib.agreement.model.AgreementContent;
import de.vzg.reposis.digibib.agreement.model.Author;
import de.vzg.reposis.digibib.agreement.xml.AgreementAuthorURIResolver;

public class AgreementContentFactoryTest extends MCRTestCase {

    private static final String INPUT_FILE_PATH = "/agreement/mods_object.xml";

    private static final String TITLE = "Title";

    private static final String DOI = "10.1000/182";

    private static final String LICENSE = "cc_by-nc-sa_4.0";

    private static final LocalDate EMBARGO = LocalDate.parse("2025-01-01");

    private static final String AUTHOR_NAME = "Author";

    private static final String AUTHOR_EMAIL = "test@test.de";

    private static final String AUTHOR_INSTITUTE = "institue";

    private static final String AUTHOR_PHONE = "01234567899";

    @Override
    protected Map<String, String> getTestProperties() {
        Map<String, String> testProperties = super.getTestProperties();
        testProperties.put("MCR.URIResolver.ModuleResolver.agreementauthor",
            AgreementTestAuthorURIResolver.class.getName());
        return testProperties;
    }

    @Test
    public void testCreateAgreementFromObject() throws JDOMException, IOException {
        final URL classResourceUrl = AgreementContentFactoryTest.class.getResource(INPUT_FILE_PATH);
        assertNotNull(classResourceUrl);

        final MCRContent input = new MCRURLContent(classResourceUrl);
        final MCRObject object = Mockito.mock(MCRObject.class);
        Mockito.when(object.createXML()).thenReturn(input.asXML());

        final AgreementContent agreement = new AgreementContentFactory().fromObject(object);
        assertNotNull(agreement);
        assertEquals(TITLE, agreement.getTitle());
        assertEquals(DOI, agreement.getDoi());
        assertEquals(LICENSE, agreement.getLicense());
        assertEquals(EMBARGO, agreement.getEmbargo());

        final Author author = agreement.getAuthor();
        assertNotNull(author);
        assertEquals(AUTHOR_NAME, author.getName());
        assertEquals(AUTHOR_EMAIL, author.getEmail());
        assertEquals(AUTHOR_INSTITUTE, author.getInstitute());
        assertEquals(AUTHOR_PHONE, author.getPhone());
    }

    public static class AgreementTestAuthorURIResolver extends AgreementAuthorURIResolver {

        @Override
        public Author resolveLocalAuthor(String username) {
            return new Author.Builder()
                .name(AUTHOR_NAME)
                .email(AUTHOR_EMAIL)
                .institute(AUTHOR_INSTITUTE)
                .phone(AUTHOR_PHONE)
                .build();
        }
    }
}
