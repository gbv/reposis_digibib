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
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mycore.common.MCRTestCase;

import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestBodyDto;
import de.vzg.reposis.digibib.contactrequest.model.ContactRequestBody;

public class ContactRequestBodyMapperTest extends MCRTestCase {

    private static final String EMAIL = "test@test.de";

    private static final String NAME = "name";

    private static final String MESSAGE = "message";

    private static final String ORCID = "orcid";

    @Test
    public void testToEntity() {
        final ContactRequestBodyDto contactRequestDto = new ContactRequestBodyDto();
        contactRequestDto.setEmail(EMAIL);
        contactRequestDto.setMessage(MESSAGE);
        contactRequestDto.setName(NAME);
        contactRequestDto.setOrcid(ORCID);
        final ContactRequestBody contactRequest = ContactRequestBodyMapper.toEntity(contactRequestDto);
        assertEquals(EMAIL, contactRequest.getEmail());
        assertEquals(NAME, contactRequest.getName());
        assertEquals(MESSAGE, contactRequest.getMessage());
        assertEquals(ORCID, contactRequest.getOrcid());
    }

    @Test
    public void testToEntity_noOrcid() {
        final ContactRequestBodyDto contactRequestDto = new ContactRequestBodyDto();
        contactRequestDto.setEmail(EMAIL);
        contactRequestDto.setMessage(MESSAGE);
        contactRequestDto.setName(NAME);
        final ContactRequestBody contactRequest = ContactRequestBodyMapper.toEntity(contactRequestDto);
        assertEquals(EMAIL, contactRequest.getEmail());
        assertEquals(NAME, contactRequest.getName());
        assertEquals(MESSAGE, contactRequest.getMessage());
        assertNull(contactRequest.getOrcid());
    }

    @Test
    public void testToDto() {
        final ContactRequestBody contactRequest = new ContactRequestBody();
        contactRequest.setEmail(EMAIL);
        contactRequest.setMessage(MESSAGE);
        contactRequest.setName(NAME);
        contactRequest.setOrcid(ORCID);
        final ContactRequestBodyDto contactRequestDto = ContactRequestBodyMapper.toDto(contactRequest);
        assertEquals(EMAIL, contactRequestDto.getEmail());
        assertEquals(NAME, contactRequestDto.getName());
        assertEquals(MESSAGE, contactRequestDto.getMessage());
        assertEquals(ORCID, contactRequestDto.getOrcid());
    }
}
