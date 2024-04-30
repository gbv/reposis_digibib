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

package de.vzg.reposis.digibib.contact.mapper;

import de.vzg.reposis.digibib.contact.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contact.persistence.model.ContactRequest;

/**
 * The ContactInfoMapper class provides methods to convert between
 * {@link ContactRequest} and {@link ContactRequestDto}.
 */
public class ContactRequestMapper {

    /**
     * Converts a {@link ContactRequestDto} to a {@link ContactRequest} entity.
     *
     * @param contactRequestDto the DTO to be converted
     * @return the converted ContactRequest entity
     */
    public static ContactRequest toEntity(ContactRequestDto contactRequestDto) {
        final ContactRequest contactRequest = new ContactRequest();
        contactRequest.setEmail(contactRequestDto.getEmail());
        contactRequest.setMessage(contactRequestDto.getMessage());
        contactRequest.setName(contactRequestDto.getName());
        contactRequest.setOrcid(contactRequestDto.getOrcid());
        return contactRequest;
    }

    /**
     * Converts a {@link ContactRequest} entity to a {@link ContactRequestDto}.
     *
     * @param contactRequest the entity to be converted
     * @return the converted ContactRequestDto
     */
    public static ContactRequestDto toDto(ContactRequest contactRequest) {
        final ContactRequestDto contactRequestDto = new ContactRequestDto();
        contactRequestDto.setEmail(contactRequest.getEmail());
        contactRequestDto.setName(contactRequest.getName());
        contactRequestDto.setMessage(contactRequest.getMessage());
        contactRequestDto.setOrcid(contactRequest.getOrcid());
        return contactRequestDto;
    }
}
