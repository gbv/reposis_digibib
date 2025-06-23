/*
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * MyCoRe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyCoRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyCoRe.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.vzg.reposis.digibib.contactrequest.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mycore.common.MCRJPATestCase;
import org.mycore.common.events.MCREventManager;
import org.mycore.datamodel.metadata.MCRObjectID;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestBodyDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.mapper.ContactAttemptMapper;
import de.vzg.reposis.digibib.contactrequest.model.ContactAttempt;
import de.vzg.reposis.digibib.contactrequest.model.ContactRequest;
import de.vzg.reposis.digibib.contactrequest.model.ContactRequestBody;
import de.vzg.reposis.digibib.contactrequest.repository.ContactAttemptRepository;
import de.vzg.reposis.digibib.contactrequest.repository.ContactInfoRepository;
import de.vzg.reposis.digibib.contactrequest.repository.ContactRequestRepository;
import de.vzg.reposis.digibib.contactrequest.validation.ContactAttemptValidator;
import de.vzg.reposis.digibib.contactrequest.validation.ContactInfoValidator;
import de.vzg.reposis.digibib.contactrequest.validation.ContactRequestValidator;
import de.vzg.reposis.digibib.contactrequest.validation.ValidatorFacade;

public class ContactRequestServiceImplTest extends MCRJPATestCase {

    private final String OBJECT_ID = "mcr_test_00000001";

    private ContactRequestRepository requestRepoMock;

    private ContactInfoRepository infoRepoyMock;

    private ContactAttemptRepository attemptRepoMock;

    private ContactRequestServiceImpl requestService;

    private ContactRequestValidator validatorMock;

    private ContactInfoValidator infoValidatorMock;

    private ContactAttemptValidator attemptValidatorMock;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        MCREventManager.instance().clear();
        requestRepoMock = Mockito.mock(ContactRequestRepository.class);
        infoRepoyMock = Mockito.mock(ContactInfoRepository.class);
        attemptRepoMock = Mockito.mock(ContactAttemptRepository.class);
        validatorMock = Mockito.mock(ContactRequestValidator.class);
        infoValidatorMock = Mockito.mock(ContactInfoValidator.class);
        attemptValidatorMock = Mockito.mock(ContactAttemptValidator.class);
        final ValidatorFacade validatorFacade
            = new ValidatorFacade(validatorMock, infoValidatorMock, attemptValidatorMock);
        requestService
            = new ContactRequestServiceImpl(requestRepoMock, infoRepoyMock, attemptRepoMock, validatorFacade);
    }

    @Override
    protected Map<String, String> getTestProperties() {
        Map<String, String> testProperties = super.getTestProperties();
        testProperties.put("MCR.Metadata.Type.test", Boolean.TRUE.toString());
        return testProperties;
    }

    @Test
    public void testCreateContactRequest() {
        final ContactRequestBodyDto contactRequestBodyDto = new ContactRequestBodyDto();
        contactRequestBodyDto.setEmail("test@test.de");
        contactRequestBodyDto.setName("test");
        contactRequestBodyDto.setMessage("test message");
        final ContactRequestDto contactRequestDto = new ContactRequestDto();
        contactRequestDto.setBody(contactRequestBodyDto);
        contactRequestDto.setObjectId(MCRObjectID.getInstance(OBJECT_ID));

        final ContactRequest contactRequest = new ContactRequest();
        contactRequest.setObjectId(MCRObjectID.getInstance(OBJECT_ID));
        contactRequest.setId(UUID.randomUUID());
        contactRequest.setStatus(ContactRequest.Status.OPEN);
        Mockito.when(requestRepoMock.save(Mockito.any(ContactRequest.class))).thenReturn(contactRequest);

        final ContactRequestDto createContactTicketDto = requestService.createContactRequest(contactRequestDto);
        Assert.assertNotNull(createContactTicketDto);
        Assert.assertEquals(contactRequest.getId(), createContactTicketDto.getId());

        Mockito.verify(requestRepoMock, Mockito.times(1)).save(Mockito.any(ContactRequest.class));
    }

    @Test
    public void testGetContactRequest() {
        final ContactRequestBody contactRequestBody = new ContactRequestBody();
        contactRequestBody.setEmail("test@test.de");
        contactRequestBody.setName("test");
        contactRequestBody.setMessage("test message");
        final ContactRequest contactRequest = new ContactRequest();
        contactRequest.setBody(contactRequestBody);
        contactRequest.setObjectId(MCRObjectID.getInstance(OBJECT_ID));
        contactRequest.setId(UUID.randomUUID());
        contactRequest.setStatus(ContactRequest.Status.OPEN);

        Mockito.when(requestRepoMock.findById(contactRequest.getId())).thenReturn(Optional.of(contactRequest));
        final ContactRequestDto foundContactTicketDto = requestService.getContactRequestById(contactRequest.getId());
        Assert.assertNotNull(foundContactTicketDto);
        Assert.assertEquals(contactRequest.getId(), foundContactTicketDto.getId());
        Mockito.verify(requestRepoMock, Mockito.times(1)).findById(contactRequest.getId());
    }

    @Test
    public void testCreateContactAttempt() {
        final ContactRequest contactRequest = new ContactRequest();
        contactRequest.setId(UUID.randomUUID());
        contactRequest.setStatus(ContactRequest.Status.OPEN);
        Mockito.when(requestRepoMock.findById(contactRequest.getId())).thenReturn(Optional.of(contactRequest));

        final ContactAttempt attempt = new ContactAttempt();
        attempt.setId(UUID.randomUUID());
        attempt.setRecipientName("name");
        attempt.setRecipientReference("test@email.de");
        attempt.setType(ContactAttempt.AttemptType.EMAIL);
        Mockito.when(attemptRepoMock.save(Mockito.any(ContactAttempt.class))).thenReturn(attempt);

        final ContactAttemptDto attemptDto = ContactAttemptMapper.toDto(attempt);
        final ContactAttemptDto createdAttemptDto
            = requestService.createContactAttempt(contactRequest.getId(), attemptDto);
        Assert.assertNotNull(createdAttemptDto);
        Assert.assertEquals(attempt.getId(), createdAttemptDto.getId());
        Assert.assertEquals(attempt.getType().toString(), createdAttemptDto.getType());
        Assert.assertEquals(attempt.getRecipientName(), createdAttemptDto.getRecipientName());
        Assert.assertEquals(attempt.getRecipientReference(), createdAttemptDto.getRecipientReference());

        Mockito.verify(requestRepoMock, Mockito.times(1)).findById(contactRequest.getId());
        Mockito.verify(attemptRepoMock, Mockito.times(1)).save(Mockito.any(ContactAttempt.class));
    }

}
