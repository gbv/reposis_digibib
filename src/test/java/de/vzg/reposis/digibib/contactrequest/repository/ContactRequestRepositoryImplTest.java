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

package de.vzg.reposis.digibib.contactrequest.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mycore.common.MCRJPATestCase;
import org.mycore.datamodel.metadata.MCRObjectID;

import de.vzg.reposis.digibib.contactrequest.model.ContactRequest;
import de.vzg.reposis.digibib.contactrequest.model.ContactRequestBody;

public class ContactRequestRepositoryImplTest extends MCRJPATestCase {

    private final String OBJECT_ID = "mcr_test_00000001";

    @Override
    protected Map<String, String> getTestProperties() {
        Map<String, String> testProperties = super.getTestProperties();
        testProperties.put("MCR.Metadata.Type.test", Boolean.TRUE.toString());
        return testProperties;
    }

    @Test
    public void testFindById() {
        final ContactRequestRepository repository = new ContactRequestRepository();
        ContactRequest contactTicket = createContactTicket();
        repository.save(contactTicket);
        endTransaction();

        assertNotNull(contactTicket.getId());
        assertTrue(repository.findById(contactTicket.getId()).isPresent());
    }

    @Test
    public void testInsert() {
        final ContactRequestRepository repository = new ContactRequestRepository();
        ContactRequest contactTicket = createContactTicket();
        repository.save(contactTicket);
        repository.flush();
        repository.detach(contactTicket);
        endTransaction();

        final List<ContactRequest> contactTickets = List.copyOf(repository.findAll());
        assertEquals(1, contactTickets.size());
    }

    private ContactRequest createContactTicket() {
        final ContactRequest contactTicket = new ContactRequest();
        final ContactRequestBody contactRequest = new ContactRequestBody("", "", "", "");
        contactTicket.setBody(contactRequest);
        contactTicket.setCreated(LocalDateTime.now());
        contactTicket.setCreatedBy("test");
        contactTicket.setStatus(ContactRequest.Status.OPEN);
        contactTicket.setObjectId(MCRObjectID.getInstance(OBJECT_ID));
        return contactTicket;
    }
}
