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
import java.util.UUID;

import org.junit.Test;
import org.mycore.common.MCRTestCase;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;

public class ContactAttemptMapperTest extends MCRTestCase {

    private static final UUID ID = UUID.randomUUID();

    private static final Date SEND_DATE = new Date();

    private static final Date SUCCESS_DATE = new Date();

    private static final Date ERROR_DATE = new Date();

    private static final UUID CONTACT_INFO_ID = UUID.randomUUID();

    @Test
    public void testToEntity() {
        final ContactAttemptDto contactAttemptDto = new ContactAttemptDto();
        contactAttemptDto.setId(ID);
        contactAttemptDto.setSendDate(SEND_DATE);
        contactAttemptDto.setSuccessDate(SUCCESS_DATE);
        contactAttemptDto.setErrorDate(ERROR_DATE);
        final ContactInfoDto contactInfoDto = new ContactInfoDto();
        contactInfoDto.setId(CONTACT_INFO_ID);
        contactAttemptDto.setContactInfo(contactInfoDto);
        final ContactAttempt contactAttempt = ContactAttemptMapper.toEntity(contactAttemptDto);
        assertEquals(ID, contactAttempt.getId());
        assertEquals(ContactMapperUtil.dateToLocalDate(SEND_DATE), contactAttempt.getSendDate());
        assertEquals(ContactMapperUtil.dateToLocalDate(SUCCESS_DATE), contactAttempt.getSuccessDate());
        assertEquals(ContactMapperUtil.dateToLocalDate(ERROR_DATE), contactAttempt.getErrorDate());
        assertNotNull(contactAttempt.getContactInfo());
        assertEquals(CONTACT_INFO_ID, contactAttempt.getContactInfo().getId());
    }

    @Test
    public void testToDto() {
        final ContactAttempt contactAttempt = new ContactAttempt();
        contactAttempt.setId(ID);
        contactAttempt.setSendDate(ContactMapperUtil.dateToLocalDate(SEND_DATE));
        contactAttempt.setSuccessDate(ContactMapperUtil.dateToLocalDate(SUCCESS_DATE));
        contactAttempt.setErrorDate(ContactMapperUtil.dateToLocalDate(ERROR_DATE));
        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(CONTACT_INFO_ID);
        contactAttempt.setContactInfo(contactInfo);
        final ContactAttemptDto contactAttemptDto = ContactAttemptMapper.toDto(contactAttempt);
        assertEquals(ID, contactAttemptDto.getId());
        assertEquals(SEND_DATE, contactAttemptDto.getSendDate());
        assertEquals(SUCCESS_DATE, contactAttemptDto.getSuccessDate());
        assertEquals(ERROR_DATE, contactAttemptDto.getErrorDate());
        assertNotNull(contactAttemptDto.getContactInfo());
        assertEquals(CONTACT_INFO_ID, contactAttemptDto.getContactInfo().getId());
    }

}
