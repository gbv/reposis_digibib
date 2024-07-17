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

package de.vzg.reposis.digibib.accesskey.mapper;

import org.mycore.mcr.acl.accesskey.model.MCRAccessKey;

import de.vzg.reposis.digibib.accesskey.dto.AccessKeyDto;
import de.vzg.reposis.digibib.accesskey.model.PermissionType;

/**
 * Utility class for mapping between {@link AccessKeyDto} and {@link MCRAccessKey} entities.
 *
 * The {@code AccessKeyMapper} provides static methods to convert between the DTO
 * representation and the entity representation of access keys.
 */
public class AccessKeyMapper {

    /**
     * Converts an {@link AccessKeyDto} to an {@link MCRAccessKey} entity.
     *
     * @param accessKeyDto the DTO to be converted
     * @return the converted access key entity
     */
    public static MCRAccessKey toEntity(AccessKeyDto accessKeyDto) {
        final MCRAccessKey accessKey = new MCRAccessKey(accessKeyDto.getValue(),
            accessKeyDto.getPermission().getValue());
        accessKey.setComment(accessKeyDto.getComment());
        accessKey.setCreated(accessKeyDto.getCreated());
        accessKey.setCreatedBy(accessKeyDto.getCreatedBy());
        accessKey.setExpiration(accessKeyDto.getExpiration());
        accessKey.setIsActive(accessKeyDto.getActive());
        accessKey.setLastModified(accessKeyDto.getLastModified());
        accessKey.setLastModifiedBy(accessKeyDto.getLastModifiedBy());
        accessKey.setObjectId(accessKeyDto.getObjectId());
        return accessKey;
    }

    /**
     * Converts an {@link MCRAccessKey} entity to an {@link AccessKeyDto}.
     *
     * @param accessKey the entity to be converted
     * @return the converted access key DTO
     */
    public static AccessKeyDto toDto(MCRAccessKey accessKey) {
        final AccessKeyDto accessKeyDto = new AccessKeyDto();
        accessKeyDto.setValue(accessKey.getSecret());
        accessKeyDto.setPermission(PermissionType.fromValue(accessKey.getType()));
        accessKeyDto.setComment(accessKey.getComment());
        accessKeyDto.setCreated(accessKey.getCreated());
        accessKeyDto.setCreatedBy(accessKey.getCreatedBy());
        accessKeyDto.setExpiration(accessKey.getExpiration());
        accessKeyDto.setActive(accessKey.getIsActive());
        accessKeyDto.setLastModified(accessKey.getLastModified());
        accessKeyDto.setLastModifiedBy(accessKey.getLastModifiedBy());
        accessKeyDto.setObjectId(accessKey.getObjectId());
        return accessKeyDto;
    }

}
