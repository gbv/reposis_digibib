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

import de.vzg.reposis.digibib.contactrequest.model.ContactInfo;

/**
 * Repository implementation for managing {@link ContactInfo} entities.
 * Extends {@link ContactBaseRepositoryImpl} to inherit basic CRUD operations and entity management.
 */
public class ContactInfoRepository extends ContactBaseRepositoryImpl<ContactInfo> {

    @Override
    public Collection<ContactInfo> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<ContactInfo> findById(UUID id) {
        return Optional.ofNullable(getEntityManager().find(ContactInfo.class, id));
    }

    @Override
    public ContactInfo save(ContactInfo entity) {
        if (entity.getId() == null) {
            getEntityManager().persist(entity);
            return entity;
        } else {
            return getEntityManager().merge(entity);
        }
    }

}
