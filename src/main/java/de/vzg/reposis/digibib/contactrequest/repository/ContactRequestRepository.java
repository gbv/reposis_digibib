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

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.mycore.datamodel.metadata.MCRObjectID;

import de.vzg.reposis.digibib.contactrequest.model.ContactRequest;

/**
 * Repository implementation for managing {@link ContactRequest} entities.
 * Extends {@link ContactBaseRepositoryImpl} to inherit basic CRUD operations and entity management.
 */
public class ContactRequestRepository extends ContactBaseRepositoryImpl<ContactRequest> {

    @Override
    public Collection<ContactRequest> findAll() {
        return getEntityManager().createNamedQuery("ContactRequest.findAll", ContactRequest.class).getResultList();
    }

    @Override
    public Optional<ContactRequest> findById(UUID id) {
        return Optional.ofNullable(getEntityManager().find(ContactRequest.class, id));
    }

    @Override
    public ContactRequest save(ContactRequest entity) {
        if (entity.getId() == null) {
            getEntityManager().persist(entity);
            return entity;
        } else {
            return getEntityManager().merge(entity);
        }
    }

    /**
     * Retrieves a collection of {@link ContactRequest} entities by their object id.
     *
     * @param objectId the object id to search for
     * @return a collection of contact request entities matching the given object id.
     */
    public Collection<ContactRequest> findByObjectId(MCRObjectID objectId) {
        return getEntityManager().createNamedQuery("ContactRequest.findByObjectId", ContactRequest.class)
            .setParameter("objectId", objectId).getResultList();
    }

    /**
     * Retrieves a collection of {@link ContactRequest} entities by their status.
     *
     * @param status the status to search for
     * @return a collection of contact request entities matching the given status.
     */
    public Collection<ContactRequest> findByStatus(ContactRequest.Status status) {
        return getEntityManager().createNamedQuery("ContactRequest.findByStatus", ContactRequest.class)
            .setParameter("status", status)
            .getResultList();
    }

}
