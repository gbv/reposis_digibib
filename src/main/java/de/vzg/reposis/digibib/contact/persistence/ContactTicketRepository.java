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

package de.vzg.reposis.digibib.contact.persistence;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.mycore.datamodel.metadata.MCRObjectID;

import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;

/**
 * Repository implementation for managing {@link ContactTicket} entities.
 * Extends {@link ContactBaseRepositoryImpl} to inherit basic CRUD operations and entity management.
 */
public class ContactTicketRepository extends ContactBaseRepositoryImpl<ContactTicket> {

    @Override
    public Collection<ContactTicket> findAll() {
        return getEntityManager().createNamedQuery("ContactTicket.findAll", ContactTicket.class).getResultList();
    }

    @Override
    public Optional<ContactTicket> findById(UUID id) {
        return Optional.ofNullable(getEntityManager().find(ContactTicket.class, id));
    }

    @Override
    public Optional<ContactTicket> findReferenceById(UUID id) {
        return Optional.ofNullable(getEntityManager().getReference(ContactTicket.class, id));
    }

    /**
     * Retrieves a collection of {@link ContactTicket} entities by their object id.
     *
     * @param objectId the object id to search for
     * @return a collection of contact ticket entities matching the given object id.
     */
    public Collection<ContactTicket> findByObjectId(MCRObjectID objectId) {
        return getEntityManager().createNamedQuery("ContactTicket.findByObjectId", ContactTicket.class)
            .setParameter("objectId", objectId).getResultList();
    }

    /**
     * Retrieves a collection of {@link ContactTicket} entities by their status.
     *
     * @param status the status to search for
     * @return a collection of contact ticket entities matching the given status.
     */
    public Collection<ContactTicket> findByStatus(ContactTicket.Status status) {
        return getEntityManager().createNamedQuery("ContactTicket.findByStatus", ContactTicket.class)
            .setParameter("status", status)
            .getResultList();
    }

}
