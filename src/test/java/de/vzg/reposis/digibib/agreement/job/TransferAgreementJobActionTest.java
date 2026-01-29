package de.vzg.reposis.digibib.agreement.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mycore.common.MCRTestCase;
import org.mycore.datamodel.metadata.MCRObject;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.services.queuedjob.MCRJob;

import de.vzg.reposis.digibib.agreement.service.AgreementMetadataManager;
import de.vzg.reposis.digibib.agreement.service.AgreementService;
import de.vzg.reposis.digibib.pdf.model.FormData;

public class TransferAgreementJobActionTest extends MCRTestCase {

    public static final String AGREEMENT_ID_PARAM = "agreementId";
    private static final String OBJECT_ID_PARAM = "objectId";
    private static final String VALID_OBJECT_ID = "mycore_test_00000001";
    private static final String VALID_AGREEMENT_ID = "agreement";

    private AgreementService agreementService;
    private AgreementMetadataManager metadataManager;
    private MCRObjectID objectId;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        agreementService = mock(AgreementService.class);
        metadataManager = mock(AgreementMetadataManager.class);
        objectId = MCRObjectID.getInstance(VALID_OBJECT_ID);
    }

    @Override
    protected Map<String, String> getTestProperties() {
        Map<String, String> testProperties = super.getTestProperties();
        testProperties.put("MCR.Metadata.Type.test", Boolean.TRUE.toString());
        return testProperties;
    }

    @Test
    public void testCreateJob_setsCorrectParametersAndType() {
        final MCRJob resultJob = TransferAgreementJobAction.createJob(objectId, VALID_AGREEMENT_ID);
        final TransferAgreementJobAction action
            = new TransferAgreementJobAction(resultJob, agreementService, metadataManager);
        assertEquals(VALID_AGREEMENT_ID, action.getAgreementId());
        assertEquals(objectId, action.getObjectId());
    }

    @Test
    public void testExecute_callsTransferAgreement() throws Exception {
        final MCRJob job = mock(MCRJob.class);
        final MCRObject object = mock(MCRObject.class);
        final FormData formData = mock(FormData.class);
        when(job.getParameter(AGREEMENT_ID_PARAM)).thenReturn(VALID_AGREEMENT_ID);
        when(job.getParameter(OBJECT_ID_PARAM)).thenReturn(VALID_OBJECT_ID);
        when(metadataManager.exists(objectId)).thenReturn(true);
        when(metadataManager.retrieveMCRObject(objectId)).thenReturn(object);
        when(agreementService.createAgreementFormData(object, VALID_AGREEMENT_ID)).thenReturn(formData);
        final TransferAgreementJobAction action
            = new TransferAgreementJobAction(job, agreementService, metadataManager);
        action.execute();
        verify(agreementService).createAgreementFormData(object, VALID_AGREEMENT_ID);
        verify(agreementService).transferAgreement(objectId, VALID_AGREEMENT_ID, formData);
    }

}
