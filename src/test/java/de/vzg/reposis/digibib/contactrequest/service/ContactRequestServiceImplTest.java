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
import org.junit.Test;
import org.mockito.Mockito;
import org.mycore.common.MCRTestCase;
import org.mycore.datamodel.metadata.MCRObjectID;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestBodyDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.persistence.ContactAttemptRepository;
import de.vzg.reposis.digibib.contactrequest.persistence.ContactInfoRepository;
import de.vzg.reposis.digibib.contactrequest.persistence.ContactRequestRepository;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactRequest;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactRequestBody;

public class ContactRequestServiceImplTest extends MCRTestCase {

    private final String OBJECT_ID = "mcr_test_00000001";

    @Override
    protected Map<String, String> getTestProperties() {
        Map<String, String> testProperties = super.getTestProperties();
        testProperties.put("MCR.Metadata.Type.test", Boolean.TRUE.toString());
        testProperties.put("Digibib.ContactRequest.SendRequestConfirmationEmail", Boolean.FALSE.toString());
        testProperties.put("Digibib.ContactRequest.SendNewRequestInfoEmailToStaff", Boolean.FALSE.toString());
        testProperties.put("Digibib.ContactRequest.CollectContactInfos", Boolean.FALSE.toString());
        return testProperties;
    }

    @Test
    public void testCreateContactTicket() {
        final ContactRequestBodyDto contactRequestDto = new ContactRequestBodyDto();
        contactRequestDto.setEmail("test@test.de");
        contactRequestDto.setName("test");
        contactRequestDto.setMessage("test message");
        final ContactRequestDto contactTicketDto = new ContactRequestDto();
        contactTicketDto.setBody(contactRequestDto);
        contactTicketDto.setObjectId(MCRObjectID.getInstance(OBJECT_ID));

        final ContactRequest contactTicket = new ContactRequest();
        contactTicket.setObjectId(MCRObjectID.getInstance(OBJECT_ID));
        contactTicket.setId(UUID.randomUUID());
        final ContactRequestRepository ticketRepositoryMock = Mockito.mock(ContactRequestRepository.class);
        Mockito.when(ticketRepositoryMock.save(Mockito.any(ContactRequest.class))).thenReturn(contactTicket);

        final ContactRequestServiceImpl contactTicketService
            = new ContactRequestServiceImpl(ticketRepositoryMock, null, null);
        final ContactRequestDto createContactTicketDto = contactTicketService.createContactRequest(contactTicketDto);
        Assert.assertNotNull(createContactTicketDto);
        Assert.assertEquals(contactTicket.getId(), createContactTicketDto.getId());

        Mockito.verify(ticketRepositoryMock, Mockito.times(1)).save(Mockito.any(ContactRequest.class));
    }

    @Test
    public void testGetContactTicket() {
        final ContactRequestBody contactRequest = new ContactRequestBody();
        contactRequest.setEmail("test@test.de");
        contactRequest.setName("test");
        contactRequest.setMessage("test message");
        final ContactRequest contactTicket = new ContactRequest();
        contactTicket.setBody(contactRequest);
        contactTicket.setObjectId(MCRObjectID.getInstance(OBJECT_ID));
        contactTicket.setId(UUID.randomUUID());

        final ContactRequestRepository requestRepositoryMock = Mockito.mock(ContactRequestRepository.class);
        Mockito.when(requestRepositoryMock.findById(contactTicket.getId())).thenReturn(Optional.of(contactTicket));

        final ContactRequestServiceImpl contactTicketService
            = new ContactRequestServiceImpl(requestRepositoryMock, null, null);
        final ContactRequestDto foundContactTicketDto
            = contactTicketService.getContactRequestById(contactTicket.getId());
        Assert.assertNotNull(foundContactTicketDto);
        Assert.assertEquals(contactTicket.getId(), foundContactTicketDto.getId());

        Mockito.verify(requestRepositoryMock, Mockito.times(1)).findById(contactTicket.getId());
    }

    @Test
    public void testCreateContactAttempt() {
        final ContactRequestRepository ticketRepositoryMock = Mockito.mock(ContactRequestRepository.class);
        final ContactInfoRepository infoRepositoryMock = Mockito.mock(ContactInfoRepository.class);
        final ContactAttemptRepository attemptRepositoryMock = Mockito.mock(ContactAttemptRepository.class);

        final ContactRequest contactTicketReference = new ContactRequest();
        contactTicketReference.setId(UUID.randomUUID());
        Mockito.when(ticketRepositoryMock.findById(contactTicketReference.getId()))
            .thenReturn(Optional.of(contactTicketReference));

        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(UUID.randomUUID());
        contactInfo.setName("name");
        contactInfo.setEmail("test@email.de");
        contactInfo.setOrigin("test");
        Mockito.when(infoRepositoryMock.findById(contactInfo.getId())).thenReturn(Optional.of(contactInfo));

        final ContactAttempt attempt = new ContactAttempt();
        attempt.setId(UUID.randomUUID());
        attempt.setContactInfo(contactInfo);
        Mockito.when(attemptRepositoryMock.save(Mockito.any(ContactAttempt.class))).thenReturn(attempt);

        final ContactRequestServiceImpl ticketService
            = new ContactRequestServiceImpl(ticketRepositoryMock, infoRepositoryMock, attemptRepositoryMock);

        final ContactAttemptDto attemptDto = new ContactAttemptDto();
        final ContactAttemptDto createdAttemptDto
            = ticketService.createContactAttempt(contactTicketReference.getId(), contactInfo.getId(), attemptDto);
        Assert.assertNotNull(createdAttemptDto);
        Assert.assertEquals(attempt.getId(), createdAttemptDto.getId());
        Assert.assertNotNull(createdAttemptDto.getContactInfo());
        Assert.assertEquals(contactInfo.getId(), contactInfo.getId());

        Mockito.verify(ticketRepositoryMock, Mockito.times(1)).findById(contactTicketReference.getId());
        Mockito.verify(infoRepositoryMock, Mockito.times(1)).findById(contactInfo.getId());
        Mockito.verify(attemptRepositoryMock, Mockito.times(1)).save(Mockito.any(ContactAttempt.class));
    }

}
