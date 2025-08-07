package de.vzg.reposis.digibib.agreement.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mycore.services.queuedjob.MCRJob;

import de.vzg.reposis.digibib.agreement.model.Agreement;
import de.vzg.reposis.digibib.agreement.model.AgreementContent;
import de.vzg.reposis.digibib.agreement.model.Author;
import de.vzg.reposis.digibib.agreement.service.AgreementTransferService;

public class TransferAgreementJobActionTest {

    private static final String VALID_NAME = "agreement";
    private static final String VALID_COMMENT = "comment";
    private static final String VALID_LICENSE = "license";
    private static final LocalDate VALID_EMBARGO = LocalDate.now();
    private static final String VALID_TITLE = "title";
    private static final String VALID_DOI = "doi";
    private static final String VALID_AUTHOR_NAME = "author";
    private static final String VALID_AUTHOR_EMAIL = "email";
    private static final String VALID_AUTHOR_INSTITUTE = "institute";
    private static final String VALID_AUTHOR_PHONE = "phone";

    private AgreementTransferService agreementService;
    private Author author;
    private AgreementContent content;
    private Agreement agreement;

    @Before
    public void setUp() throws Exception {
        agreementService = mock(AgreementTransferService.class);
    }

    @Test
    public void testCreateJob_setsCorrectParametersAndType() {
        initAgreementMock();
        final MCRJob resultJob = TransferAgreementJobAction.createJob(agreement);
        final TransferAgreementJobAction action = new TransferAgreementJobAction(resultJob, agreementService);
        final Agreement result = action.getAgreement();

        assertNotNull(result);
        assertEquals(VALID_NAME, result.getAgreementName());
        assertNotNull(result.getContent());
        final AgreementContent resultContent = result.getContent();
        assertEquals(VALID_DOI, resultContent.getDoi());
        assertEquals(VALID_COMMENT, resultContent.getComment());
        assertEquals(VALID_LICENSE, resultContent.getLicense());
        assertEquals(VALID_TITLE, resultContent.getTitle());
        // TODO
        assertEquals(VALID_EMBARGO, resultContent.getEmbargo());
        assertNotNull(resultContent.getAuthor());
        final Author resultAuthor = resultContent.getAuthor();
        assertEquals(VALID_AUTHOR_NAME, resultAuthor.getName());
        assertEquals(VALID_AUTHOR_EMAIL, resultAuthor.getEmail());
        assertEquals(VALID_AUTHOR_INSTITUTE, resultAuthor.getInstitute());
        assertEquals(VALID_AUTHOR_PHONE, resultAuthor.getPhone());
    }

    @Test
    public void testExecute_callsTransferAgreement() throws Exception {
        final MCRJob job = mock(MCRJob.class);
        final TransferAgreementJobAction action = new TransferAgreementJobAction(job, agreementService);
        action.execute();
        verify(agreementService).transferAgreement(any(Agreement.class));
    }

    private void initAgreementMock() {
        author = mock(Author.class);
        when(author.getName()).thenReturn(VALID_AUTHOR_NAME);
        when(author.getEmail()).thenReturn(VALID_AUTHOR_EMAIL);
        when(author.getInstitute()).thenReturn(VALID_AUTHOR_INSTITUTE);
        when(author.getPhone()).thenReturn(VALID_AUTHOR_PHONE);
        content = mock(AgreementContent.class);
        when(content.getComment()).thenReturn(VALID_COMMENT);
        when(content.getLicense()).thenReturn(VALID_LICENSE);
        when(content.getTitle()).thenReturn(VALID_TITLE);
        when(content.getDoi()).thenReturn(VALID_DOI);
        when(content.getEmbargo()).thenReturn(VALID_EMBARGO);
        when(content.getAuthor()).thenReturn(author);
        agreement = mock(Agreement.class);
        when(agreement.getAgreementName()).thenReturn(VALID_NAME);
        when(agreement.getContent()).thenReturn(content);
    }

}
