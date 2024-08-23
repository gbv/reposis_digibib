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

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.model.ContactInfo;

/**
 * The ContactInfoMapper class provides methods to convert between
 * {@link ContactInfo} and {@link ContactInfoDto}.
 */
public class ContactInfoMapper {

    /**
     * Converts a {@link ContactInfoDto} to a {@link ContactInfo} entity.
     *
     * @param contactInfoDto the DTO to be converted
     * @return the converted entity
     */
    public static ContactInfo toEntity(ContactInfoDto contactInfoDto) {
        final ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(contactInfoDto.getId());
        contactInfo.setName(contactInfoDto.getName());
        contactInfo.setEmail(contactInfoDto.getEmail());
        contactInfo.setOrigin(contactInfoDto.getOrigin());
        contactInfo.setReference(contactInfoDto.getReference());
        return contactInfo;
    }

    /**
     * Converts a {@link ContactInfo} entity to a {@link ContactInfoDto}.
     *
     * @param contactInfo the entity to be converted
     * @return the converted DTO
     */
    public static ContactInfoDto toDto(ContactInfo contactInfo) {
        final ContactInfoDto contactInfoDto = new ContactInfoDto();
        contactInfoDto.setId(contactInfo.getId());
        contactInfoDto.setName(contactInfo.getName());
        contactInfoDto.setEmail(contactInfo.getEmail());
        contactInfoDto.setOrigin(contactInfo.getOrigin());
        contactInfoDto.setReference(contactInfo.getReference());
        return contactInfoDto;
    }

}
