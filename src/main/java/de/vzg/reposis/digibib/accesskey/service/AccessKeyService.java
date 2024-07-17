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

package de.vzg.reposis.digibib.accesskey.service;

import java.util.List;

import org.mycore.access.MCRAccessException;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.mcr.acl.accesskey.exception.MCRAccessKeyCollisionException;
import org.mycore.mcr.acl.accesskey.exception.MCRAccessKeyException;
import org.mycore.mcr.acl.accesskey.exception.MCRAccessKeyNotFoundException;

import de.vzg.reposis.digibib.accesskey.dto.AccessKeyDto;
import de.vzg.reposis.digibib.accesskey.dto.AccessKeyPartialUpdateDto;
import de.vzg.reposis.digibib.accesskey.exception.AccessKeyValidationException;
import de.vzg.reposis.digibib.accesskey.model.PermissionType;

/**
 * Service interface for managing access keys.
 *
 * This service provides methods to create, retrieve, update, and delete access keys
 * associated with objects identified by {@link MCRObjectID}. It supports operations
 * to handle access keys of specific types and references.
 */
public interface AccessKeyService {

    /**
     * Checks whether an access key exists.
     *
     * @param objectId the objectID
     * @param value the value
     * @return true if an access key exists.
     */
    boolean existsAccessKey(MCRObjectID objectId, String value);

    /**
     * Retrieves a list of access keys associated with the specified object ID.
     *
     * @param objectId the ID of the object for which access keys are to be retrieved
     * @return a list of access key DTO associated with the specified object ID
     * @throws MCRAccessKeyException if the object does not exist
     * @throws MCRAccessException if current user is not allowed to read access keys
     */
    public List<AccessKeyDto> getAccessKeysByObjectId(MCRObjectID objectId) throws MCRAccessException;

    /**
     * Retrieves a list of access keys of a specific permission associated with the specified object ID.
     *
     * @param objectId the ID of the object for which access keys are to be retrieved
     * @param permission the permission of access keys to be retrieved
     * @return a list of access key DTO of the specified type associated with the specified object ID
     * @throws MCRAccessKeyException if the object does not exist
     * @throws MCRAccessException if current user is not allowed to read access keys
     */
    public List<AccessKeyDto> getAccessKeysByPermission(MCRObjectID objectId, PermissionType permission)
        throws MCRAccessException;

    /**
     * Retrieves an access key by its reference associated with the specified object ID.
     *
     * @param objectId the ID of the object for which access keys are to be retrieved
     * @param value the value of the access key to be retrieved
     * @return the access key DTO corresponding to the specified reference and object ID
     * @throws MCRAccessKeyException if the object does not exist
     * @throws MCRAccessKeyNotFoundException if the access key does not exist
     * @throws MCRAccessException if current user is not allowed to read access key
     */
    public AccessKeyDto getAccessKey(MCRObjectID objectId, String value) throws MCRAccessException;

    /**
     * Creates a new access key.
     *
     * @param accessKeyDto the DTO containing the details of the access key to be created
     * @return the create access key DTO
     * @throws AccessKeyValidationException if the DTO is invalid
     * @throws MCRAccessKeyException if the object does not exist
     * @throws MCRAccessException if current user is not allowed to create access key
     * @throws MCRAccessKeyCollisionException if there is already and access key with the reference
     */
    public AccessKeyDto createAccessKey(AccessKeyDto accessKeyDto) throws MCRAccessException;

    /**
     * Partially updates an existing access key.
     *
     * @param objectId the ID of the object for which the access key is to be updated
     * @param value the value of the access key to be updated
     * @param accessKeyDto the DTO representing the access key to be updated
     * @return the updated access key
     * @throws AccessKeyValidationException if the DTO is invalid
     * @throws MCRAccessKeyNotFoundException if the access key does not exist
     * @throws MCRAccessKeyCollisionException if the access key already exists
     * @throws MCRAccessException if current user is not allowed to update access key
     */
    AccessKeyDto partialUpdateAccessKey(MCRObjectID objectId, String value, AccessKeyPartialUpdateDto accessKeyDto)
        throws MCRAccessException;

    /**
     * Updates an existing access key.
     *
     * @param objectId the ID of the object for which the access key is to be updated
     * @param value the value of the access key to be updated
     * @param accessKeyDto the DTO containing the updated details of the access key
     * @return the updated access key DTO
     * @throws AccessKeyValidationException if the DTO is invalid
     * @throws MCRAccessKeyNotFoundException if the access key does not exist
     * @throws MCRAccessKeyCollisionException if the access key already exists
     * @throws MCRAccessException if current user is not allowed to update access key
     */
    public AccessKeyDto updateAccessKey(MCRObjectID objectId, String value, AccessKeyDto accessKeyDto)
        throws MCRAccessException;

    /**
     * Removes an access key by its reference associated with the specified object ID.
     *
     * @param objectId the ID of the object for which the access key is to be removed
     * @param value the value of the access key to be removed
     * @throws MCRAccessKeyNotFoundException if the access key does not exist
     * @throws MCRAccessException if current user is not allowed to remove access key
     */
    public void removeAccessKey(MCRObjectID objectId, String value) throws MCRAccessException;

}
