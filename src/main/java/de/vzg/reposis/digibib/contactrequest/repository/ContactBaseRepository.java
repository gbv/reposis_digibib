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

/**
 * A generic repository interface providing basic CRUD operations and entity management.
 *
 * @param <T> The type of entity managed by this repository.
 */
public interface ContactBaseRepository<T> {

    /**
     * Retrieves all entities of type T.
     *
     * @return A collection containing all entities
     */
    Collection<T> findAll();

    /**
     * Retrieves an entity by its id.
     *
     * @param id the id of the entity to retrieve.
     * @return an Optional containing the entity, or empty if not found
     */
    Optional<T> findById(UUID id);

    /**
     * Saves entity.
     *
     * @param entity the entity to save
     * @return returns the saved entity
     */
    T save(T entity);

    /**
     * Removes an entity from the repository.
     *
     * @param entity the entity to remove
     */
    void remove(T entity);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id the id
     * @return true if an entity with the given id exists, false otherwise
     */
    boolean existsById(UUID id);

    /**
     * Detaches an entity from the current persistence context.
     *
     * @param entity The entity to detach
     */
    void detach(T entity);

    /**
     * Flushes pending changes to the underlying persistence store
     */
    void flush();
}
