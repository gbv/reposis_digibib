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

import org.mycore.backend.jpa.MCREntityManagerProvider;

import jakarta.persistence.EntityManager;

/**
 * Abstract base class for implementing the {@link ContactBaseRepository} interface.
 * Provides default implementations for basic CRUD operations and entity management.
 *
 * @param <T> The type of entity managed by this repository.
 */
public abstract class ContactBaseRepositoryImpl<T> implements ContactBaseRepository<T> {

    @Override
    public void remove(T entity) {
        final EntityManager entityManager = getEntityManager();
        entityManager.remove(entityManager.merge(entity));
    }

    @Override
    public void detach(T entity) {
        getEntityManager().detach(entity);
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public abstract Collection<T> findAll();

    @Override
    public abstract Optional<T> findById(UUID id);

    /**
     * Returns current {@link EntityManager} for instance.
     *
     * @return the current entity manager
     */
    protected EntityManager getEntityManager() {
        return MCREntityManagerProvider.getCurrentEntityManager();
    }
}
