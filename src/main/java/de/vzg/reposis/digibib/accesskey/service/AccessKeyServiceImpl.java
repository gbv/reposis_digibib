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

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.mycore.access.MCRAccessException;
import org.mycore.access.MCRAccessManager;
import org.mycore.datamodel.metadata.MCRMetadataManager;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.mcr.acl.accesskey.MCRAccessKeyManager;
import org.mycore.mcr.acl.accesskey.exception.MCRAccessKeyException;
import org.mycore.mcr.acl.accesskey.exception.MCRAccessKeyNotFoundException;
import org.mycore.mcr.acl.accesskey.model.MCRAccessKey;

import de.vzg.reposis.digibib.accesskey.dto.AccessKeyDto;
import de.vzg.reposis.digibib.accesskey.dto.AccessKeyPartialUpdateDto;
import de.vzg.reposis.digibib.accesskey.mapper.AccessKeyMapper;
import de.vzg.reposis.digibib.accesskey.model.PermissionType;
import de.vzg.reposis.digibib.accesskey.validation.AccessKeyDtoValidator;

/**
 * Default implementation of {@link AccessKeyService}.
 *
 */
public class AccessKeyServiceImpl implements AccessKeyService {

    private static final String MANAGE_READ_PERMISSION = "manage-read-access-keys";

    private static final String MANAGE_WRITE_PERMISSION = "manage-write-access-keys";

    /**
     * Constructs a new {@link AccessKeyServiceImpl} instance.
     */
    protected AccessKeyServiceImpl() {

    }

    /**
     * Returns single instance.
     *
     * @return the single instance
     */
    public static AccessKeyServiceImpl getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public List<AccessKeyDto> getAccessKeysByObjectId(MCRObjectID objectId) throws MCRAccessException {
        if (!MCRMetadataManager.exists(objectId)) {
            throw new MCRAccessKeyException("Object with id " + objectId + " does not exist.");
        }
        if (MCRAccessManager.checkPermission(objectId, MANAGE_WRITE_PERMISSION)) {
            return MCRAccessKeyManager.listAccessKeys(objectId).stream().map(AccessKeyMapper::toDto).toList();
        }
        if (MCRAccessManager.checkPermission(objectId, MANAGE_READ_PERMISSION)) {
            return MCRAccessKeyManager.listAccessKeysWithType(objectId, PermissionType.READ.getValue()).stream()
                .map(AccessKeyMapper::toDto).toList();
        }
        throw MCRAccessException.missingPermission("getAccessKeys", objectId.toString(), MANAGE_READ_PERMISSION);
    }

    @Override
    public List<AccessKeyDto> getAccessKeysByPermission(MCRObjectID objectId, PermissionType permission)
        throws MCRAccessException {
        if (!MCRMetadataManager.exists(objectId)) {
            throw new MCRAccessKeyException("Object with id " + objectId + " does not exist.");
        }
        ensurePermission("getAccessKeysByType", objectId, permission.toString());
        return MCRAccessKeyManager.listAccessKeysWithType(objectId, permission.toString()).stream()
            .map(AccessKeyMapper::toDto).toList();
    }

    @Override
    public AccessKeyDto getAccessKey(MCRObjectID objectId, String value) throws MCRAccessException {
        if (!MCRMetadataManager.exists(objectId)) {
            throw new MCRAccessKeyException("Object with id " + objectId + " does not exist.");
        }
        final MCRAccessKey accessKey = MCRAccessKeyManager.getAccessKeyWithSecret(objectId, value);
        if (accessKey == null) {
            throw new MCRAccessKeyNotFoundException("access key with given reference does not exist");
        }
        ensurePermission("getAccessKeyByValue", objectId, accessKey.getType());
        return AccessKeyMapper.toDto(accessKey);
    }

    @Override
    public AccessKeyDto createAccessKey(AccessKeyDto accessKeyDto) throws MCRAccessException {
        AccessKeyDtoValidator.validate(accessKeyDto);
        final MCRObjectID objectId = accessKeyDto.getObjectId();
        if (!MCRMetadataManager.exists(objectId)) {
            throw new MCRAccessKeyException("Object with id " + objectId + " does not exist.");
        }
        ensurePermission("createAccessKey", objectId, accessKeyDto.getPermission().getValue());
        final MCRAccessKey accessKey = AccessKeyMapper.toEntity(accessKeyDto);
        if (accessKeyDto.getActive() == null) {
            accessKey.setIsActive(true);
        }
        MCRAccessKeyManager.createAccessKey(objectId, accessKey);
        return AccessKeyMapper.toDto(MCRAccessKeyManager.getAccessKeyWithSecret(objectId, accessKey.getSecret()));
    }

    @Override
    public AccessKeyDto updateAccessKey(MCRObjectID objectId, String value, AccessKeyDto accessKeyDto)
        throws MCRAccessException {
        final AccessKeyDto outdatedAccessKeyDto = getAccessKey(objectId, value);
        AccessKeyDtoValidator.validate(accessKeyDto);
        if (!Objects.equals(outdatedAccessKeyDto.getPermission(), accessKeyDto.getPermission())) {
            ensurePermission("updateAccessKey", objectId, accessKeyDto.getPermission().getValue());
        }
        final MCRAccessKey accessKey = AccessKeyMapper.toEntity(accessKeyDto);
        MCRAccessKeyManager.updateAccessKey(objectId, value, accessKey);
        if (Objects.equals(value, accessKeyDto.getValue())) {
            return AccessKeyMapper.toDto(MCRAccessKeyManager.getAccessKeyWithSecret(objectId, value));
        }
        final String newValue = MCRAccessKeyManager.hashSecret(accessKeyDto.getValue(), objectId);
        return AccessKeyMapper.toDto(MCRAccessKeyManager.getAccessKeyWithSecret(objectId, newValue));
    }

    @Override
    public AccessKeyDto partialUpdateAccessKey(MCRObjectID objectId, String value,
        AccessKeyPartialUpdateDto accessKeyDto)
        throws MCRAccessException {
        if (!MCRMetadataManager.exists(objectId)) {
            throw new MCRAccessKeyException("Object with id " + objectId + " does not exist.");
        }
        final MCRAccessKey accessKey = MCRAccessKeyManager.getAccessKeyWithSecret(objectId, value);
        if (accessKey == null) {
            throw new MCRAccessKeyNotFoundException("access key with given reference does not exist");
        }
        ensurePermission("getAccessKeyByValue", objectId, accessKey.getType());
        if (accessKeyDto.getPermission().isPresent()) {
            if (Objects.equals(accessKey.getType(), accessKeyDto.getPermission().get().getValue())) {
                ensurePermission("updateAccessKey", objectId, accessKeyDto.getPermission().get().getValue());
            }
            accessKey.setType(accessKeyDto.getPermission().get().getValue());
        }
        if (accessKeyDto.getValue().isPresent()) {
            accessKey.setSecret(accessKeyDto.getValue().get());
        }
        if (accessKeyDto.getObjectId().isPresent()) {
            accessKey.setObjectId(accessKeyDto.getObjectId().get());
        }
        if (accessKeyDto.getActive().isPresent()) {
            accessKey.setIsActive(accessKeyDto.getActive().get());
        }
        if (accessKeyDto.getComment().isPresent()) {
            accessKey.setComment(accessKeyDto.getComment().get());
        }
        if (accessKeyDto.getExpiration().isPresent()) {
            // there is a bug in MCRAccessKeyManager, so set date as workaround to Date(0)
            if (accessKeyDto.getExpiration().get() == null) {
                accessKey.setExpiration(new Date(0));
            } else {
                accessKey.setExpiration(accessKeyDto.getExpiration().get());
            }
        }
        MCRAccessKeyManager.updateAccessKey(objectId, value, accessKey);
        if (accessKeyDto.getValue().isPresent()) {
            final String newValue = MCRAccessKeyManager.hashSecret(accessKeyDto.getValue().get(), objectId);
            return AccessKeyMapper.toDto(MCRAccessKeyManager.getAccessKeyWithSecret(objectId, newValue));
        }
        return AccessKeyMapper.toDto(MCRAccessKeyManager.getAccessKeyWithSecret(objectId, value));
    }

    @Override
    public void removeAccessKey(MCRObjectID objectId, String value) throws MCRAccessException {
        if (!MCRMetadataManager.exists(objectId)) {
            throw new MCRAccessKeyException("Object with id " + objectId + " does not exist.");
        }
        // check permission
        getAccessKey(objectId, value);
        MCRAccessKeyManager.removeAccessKey(objectId, value);
    }

    private static void ensurePermission(String action, MCRObjectID objectId, String type)
        throws MCRAccessException {
        if (Objects.equals(PermissionType.READ.getValue(), type)) {
            if (!MCRAccessManager.checkPermission(objectId, MANAGE_READ_PERMISSION)) {
                throw MCRAccessException.missingPermission(action, objectId.toString(), MANAGE_READ_PERMISSION);
            }
        } else if (!MCRAccessManager.checkPermission(objectId, MANAGE_WRITE_PERMISSION)) {
            throw MCRAccessException.missingPermission(action, objectId.toString(), MANAGE_WRITE_PERMISSION);
        }
    }

    private static class InstanceHolder {
        static final AccessKeyServiceImpl INSTANCE = new AccessKeyServiceImpl();
    }

    @Override
    public boolean existsAccessKey(MCRObjectID objectId, String value) {
        return MCRAccessKeyManager.getAccessKeyWithSecret(objectId, value) != null ? true : false;
    }

}
