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
import de.vzg.reposis.digibib.contactrequest.model.ContactAttempt;
import de.vzg.reposis.digibib.contactrequest.model.ContactRequest;
import de.vzg.reposis.digibib.contactrequest.model.ContactRequestBody;

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
        final ContactRequestDto contactRequesttDto = new ContactRequestDto();
        contactRequesttDto.setComment(COMMENT);
        contactRequesttDto.setCreated(CREATED);
        contactRequesttDto.setCreatedBy(CREATED_BY);
        contactRequesttDto.setStatus(STATUS.toString());
        contactRequesttDto.setId(ID);
        contactRequesttDto.setObjectId(MCRObjectID.getInstance(OBJECT_ID));
        final ContactRequestBodyDto contactRequestDto = new ContactRequestBodyDto();
        contactRequestDto.setEmail(REQUEST_EMAIL);
        contactRequestDto.setMessage(REQUEST_MESSAGE);
        contactRequestDto.setName(REQUEST_NAME);
        contactRequesttDto.setBody(contactRequestDto);
        final ContactRequest contactRequest = ContactRequestMapper.toEntity(contactRequesttDto);
        assertEquals(COMMENT, contactRequest.getComment());
        assertEquals(CREATED, ContactMapperUtil.localDateToDate(contactRequest.getCreated()));
        assertEquals(CREATED_BY, contactRequest.getCreatedBy());
        assertEquals(STATUS, contactRequest.getStatus());
        assertEquals(ID, contactRequest.getId());
        assertNotNull(contactRequest.getBody());
        assertEquals(REQUEST_NAME, contactRequest.getBody().getName());
        assertEquals(REQUEST_MESSAGE, contactRequest.getBody().getMessage());
        assertEquals(CONTACT_EMAIL, contactRequest.getBody().getEmail());
    }

    @Test
    public void testToDto() {
        final ContactRequest contactRequest = new ContactRequest();
        contactRequest.setComment(COMMENT);
        contactRequest.setCreated(ContactMapperUtil.dateToLocalDate(CREATED));
        contactRequest.setCreatedBy(CREATED_BY);
        contactRequest.setStatus(STATUS);
        contactRequest.setId(ID);
        contactRequest.setObjectId(MCRObjectID.getInstance(OBJECT_ID));
        final ContactRequestBody contactRequestBody = new ContactRequestBody();
        contactRequestBody.setEmail(REQUEST_EMAIL);
        contactRequestBody.setMessage(REQUEST_MESSAGE);
        contactRequestBody.setName(REQUEST_NAME);
        contactRequest.setBody(contactRequestBody);
        final ContactRequestDto contactRequestDto = ContactRequestMapper.toDto(contactRequest);
        assertEquals(COMMENT, contactRequestDto.getComment());
        assertEquals(CREATED, contactRequestDto.getCreated());
        assertEquals(CREATED_BY, contactRequestDto.getCreatedBy());
        assertEquals(STATUS.toString(), contactRequestDto.getStatus());
        assertEquals(ID, contactRequestDto.getId());
        assertNotNull(contactRequestDto.getBody());
        assertEquals(REQUEST_NAME, contactRequestDto.getBody().getName());
        assertEquals(REQUEST_MESSAGE, contactRequestDto.getBody().getMessage());
        assertEquals(CONTACT_EMAIL, contactRequestDto.getBody().getEmail());
    }

    @Test
    public void testToSummaryDto() {
        final ContactRequest contactRequest = new ContactRequest();
        contactRequest.setStatus(STATUS);
        final ContactAttempt contactAttempt = new ContactAttempt();
        contactAttempt.setRecipientReference(CONTACT_EMAIL);
        contactRequest.getEmailContactAttempts().add(contactAttempt);
        final ContactRequestSummaryDto summaryDto = ContactRequestMapper.toSummaryDto(contactRequest);
        assertEquals(STATUS.toString().toLowerCase(), summaryDto.getStatusString());
        assertEquals(1, summaryDto.getEmails().size());
        assertEquals(CONTACT_EMAIL, summaryDto.getEmails().get(0));
    }

}
