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

package de.vzg.reposis.digibib.contactrequest.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.mycore.common.MCRTestCase;
import org.mycore.datamodel.metadata.MCRObjectID;

import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestBodyDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestSummaryDto;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactRequest;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactRequestBody;

public class ContactRequestMapperTest extends MCRTestCase {

    private final String OBJECT_ID = "mcr_test_00000001";

    private static final String COMMENT = "bla";

    private static final Date CREATED = new Date();

    private static final String CREATED_BY = "Test";

    private static final ContactRequest.Status STATUS = ContactRequest.Status.CLOSED;

    private static final UUID ID = UUID.randomUUID();

    private static final String REQUEST_EMAIL = "test@test.de";

    private static final String REQUEST_NAME = "name";

    private static final String REQUEST_MESSAGE = "message";

    private static final String CONTACT_EMAIL = "test@test.de";

    @Override
    protected Map<String, String> getTestProperties() {
        Map<String, String> testProperties = super.getTestProperties();
        testProperties.put("MCR.Metadata.Type.test", Boolean.TRUE.toString());
        return testProperties;
    }

    @Test
    public void testToEntity() {
        final ContactRequestDto contactTicketDto = new ContactRequestDto();
        contactTicketDto.setComment(COMMENT);
        contactTicketDto.setCreated(CREATED);
        contactTicketDto.setCreatedBy(CREATED_BY);
        contactTicketDto.setStatus(STATUS);
        contactTicketDto.setId(ID);
        contactTicketDto.setObjectId(MCRObjectID.getInstance(OBJECT_ID));
        final ContactRequestBodyDto contactRequestDto = new ContactRequestBodyDto();
        contactRequestDto.setEmail(REQUEST_EMAIL);
        contactRequestDto.setMessage(REQUEST_MESSAGE);
        contactRequestDto.setName(REQUEST_NAME);
        contactTicketDto.setBody(contactRequestDto);
        final ContactRequest contactTicket = ContactRequestMapper.toEntity(contactTicketDto);
        assertEquals(COMMENT, contactTicket.getComment());
        assertEquals(CREATED, ContactMapperUtil.localDateToDate(contactTicket.getCreated()));
        assertEquals(CREATED_BY, contactTicket.getCreatedBy());
        assertEquals(STATUS, contactTicket.getStatus());
        assertEquals(ID, contactTicket.getId());
        assertNotNull(contactTicket.getBody());
        assertEquals(REQUEST_NAME, contactTicket.getBody().getName());
        assertEquals(REQUEST_MESSAGE, contactTicket.getBody().getMessage());
        assertEquals(CONTACT_EMAIL, contactTicket.getBody().getEmail());
    }

    @Test
    public void testToDto() {
        final ContactRequest contactTicket = new ContactRequest();
        contactTicket.setComment(COMMENT);
        contactTicket.setCreated(ContactMapperUtil.dateToLocalDate(CREATED));
        contactTicket.setCreatedBy(CREATED_BY);
        contactTicket.setStatus(STATUS);
        contactTicket.setId(ID);
        contactTicket.setObjectId(MCRObjectID.getInstance(OBJECT_ID));
        final ContactRequestBody contactRequest = new ContactRequestBody();
        contactRequest.setEmail(REQUEST_EMAIL);
        contactRequest.setMessage(REQUEST_MESSAGE);
        contactRequest.setName(REQUEST_NAME);
        contactTicket.setBody(contactRequest);
        final ContactRequestDto contactTicketDto = ContactRequestMapper.toDto(contactTicket);
        assertEquals(COMMENT, contactTicketDto.getComment());
        assertEquals(CREATED, contactTicketDto.getCreated());
        assertEquals(CREATED_BY, contactTicketDto.getCreatedBy());
        assertEquals(STATUS, contactTicketDto.getStatus());
        assertEquals(ID, contactTicketDto.getId());
        assertNotNull(contactTicketDto.getBody());
        assertEquals(REQUEST_NAME, contactTicketDto.getBody().getName());
        assertEquals(REQUEST_MESSAGE, contactTicketDto.getBody().getMessage());
        assertEquals(CONTACT_EMAIL, contactTicketDto.getBody().getEmail());
    }

    @Test
    public void testToSummaryDto() {
        final ContactRequest contactTicket = new ContactRequest();
        contactTicket.setStatus(STATUS);
        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(CONTACT_EMAIL);
        final ContactAttempt contactAttempt = new ContactAttempt();
        contactAttempt.setContactInfo(contactInfo);
        contactTicket.getContactAttempts().add(contactAttempt);
        final ContactRequestSummaryDto summaryDto = ContactRequestMapper.toSummaryDto(contactTicket);
        assertEquals(STATUS.toString().toLowerCase(), summaryDto.getStatusString());
        assertEquals(1, summaryDto.getEmails().size());
        assertEquals(CONTACT_EMAIL, summaryDto.getEmails().get(0));
    }

}
