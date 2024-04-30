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

import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.mycore.common.MCRTestCase;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.model.ContactAttempt;

public class ContactAttemptMapperTest extends MCRTestCase {

    private static final UUID ID = UUID.randomUUID();

    private static final Date SEND_DATE = new Date();

    private static final Date SUCCESS_DATE = new Date();

    private static final Date ERROR_DATE = new Date();

    private static final String RECIPIENT_NAME = "test";

    private static final String RECIPIENT_EMAIL = "test@test.de";

    private static final String COMMENT = "comment";

    private static final ContactAttempt.AttemptType TYPE = ContactAttempt.AttemptType.EMAIL;

    @Test
    public void testToEntity() {
        final ContactAttemptDto contactAttemptDto = new ContactAttemptDto();
        contactAttemptDto.setId(ID);
        contactAttemptDto.setType(TYPE.toString());
        contactAttemptDto.setRecipientName(RECIPIENT_NAME);
        contactAttemptDto.setRecipientReference(RECIPIENT_EMAIL);
        contactAttemptDto.setSendDate(SEND_DATE);
        contactAttemptDto.setSuccessDate(SUCCESS_DATE);
        contactAttemptDto.setErrorDate(ERROR_DATE);
        contactAttemptDto.setComment(COMMENT);
        final ContactAttempt contactAttempt = ContactAttemptMapper.toEntity(contactAttemptDto);
        assertEquals(ID, contactAttempt.getId());
        assertEquals(TYPE, contactAttempt.getType());
        assertEquals(RECIPIENT_NAME, contactAttempt.getRecipientName());
        assertEquals(RECIPIENT_EMAIL, contactAttempt.getRecipientReference());
        assertEquals(ContactMapperUtil.dateToLocalDate(SEND_DATE), contactAttempt.getSendDate());
        assertEquals(ContactMapperUtil.dateToLocalDate(SUCCESS_DATE), contactAttempt.getSuccessDate());
        assertEquals(ContactMapperUtil.dateToLocalDate(ERROR_DATE), contactAttempt.getErrorDate());
        assertEquals(COMMENT, contactAttempt.getComment());
    }

    @Test
    public void testToDto() {
        final ContactAttempt contactAttempt = new ContactAttempt();
        contactAttempt.setId(ID);
        contactAttempt.setType(TYPE);
        contactAttempt.setRecipientName(RECIPIENT_NAME);
        contactAttempt.setRecipientReference(RECIPIENT_EMAIL);
        contactAttempt.setSendDate(ContactMapperUtil.dateToLocalDate(SEND_DATE));
        contactAttempt.setSuccessDate(ContactMapperUtil.dateToLocalDate(SUCCESS_DATE));
        contactAttempt.setErrorDate(ContactMapperUtil.dateToLocalDate(ERROR_DATE));
        contactAttempt.setComment(COMMENT);
        final ContactAttemptDto contactAttemptDto = ContactAttemptMapper.toDto(contactAttempt);
        assertEquals(ID, contactAttemptDto.getId());
        assertEquals(TYPE.toString(), contactAttemptDto.getType());
        assertEquals(RECIPIENT_NAME, contactAttemptDto.getRecipientName());
        assertEquals(RECIPIENT_EMAIL, contactAttemptDto.getRecipientReference());
        assertEquals(SEND_DATE, contactAttemptDto.getSendDate());
        assertEquals(SUCCESS_DATE, contactAttemptDto.getSuccessDate());
        assertEquals(ERROR_DATE, contactAttemptDto.getErrorDate());
        assertEquals(COMMENT, contactAttemptDto.getComment());
    }

}
