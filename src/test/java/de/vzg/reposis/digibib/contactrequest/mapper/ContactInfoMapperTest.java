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

import java.util.UUID;

import org.junit.Test;
import org.mycore.common.MCRTestCase;

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.model.ContactInfo;

public class ContactInfoMapperTest extends MCRTestCase {

    private static final String EMAIL = "test@test.de";

    private static final String NAME = "test";

    private static final String ORIGIN = "origin";

    private static final String REFERENCE = "reference";

    private static final UUID ID = UUID.randomUUID();

    @Test
    public void testToEntity() {
        final ContactInfoDto contactInfoDto = new ContactInfoDto();
        contactInfoDto.setEmail(EMAIL);
        contactInfoDto.setName(NAME);
        contactInfoDto.setOrigin(ORIGIN);
        contactInfoDto.setReference(REFERENCE);
        contactInfoDto.setId(ID);
        final ContactInfo contactInfo = ContactInfoMapper.toEntity(contactInfoDto);
        assertEquals(EMAIL, contactInfo.getEmail());
        assertEquals(NAME, contactInfo.getName());
        assertEquals(ORIGIN, contactInfo.getOrigin());
        assertEquals(REFERENCE, contactInfo.getReference());
        assertEquals(ID, contactInfo.getId());
    }

    @Test
    public void testToDto() {
        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(EMAIL);
        contactInfo.setName(NAME);
        contactInfo.setOrigin(ORIGIN);
        contactInfo.setReference(REFERENCE);
        contactInfo.setId(ID);
        final ContactInfoDto contactInfoDto = ContactInfoMapper.toDto(contactInfo);
        assertEquals(EMAIL, contactInfoDto.getEmail());
        assertEquals(NAME, contactInfoDto.getName());
        assertEquals(ORIGIN, contactInfoDto.getOrigin());
        assertEquals(REFERENCE, contactInfoDto.getReference());
        assertEquals(ID, contactInfoDto.getId());
    }
}
