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

import java.util.Optional;

import de.vzg.reposis.digibib.contact.dto.ContactTicketDto;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;

/**
 * The ContactTicketMapper class provides methods to convert between
 * {@link ContactTicket} and {@link ContactTicketDto}.
 */
public class ContactTicketMapper {

    /**
     * Converts a {@link ContactTicketDto} to a {@link ContactTicket} entity.
     *
     * @param contactTicketDto the DTO to be converted
     * @return the converted ContactTicket entity
     */
    public static ContactTicket toEntity(ContactTicketDto contactTicketDto) {
        final ContactTicket contactTicket = new ContactTicket();
        contactTicket.setObjectId(contactTicketDto.getObjectId());
        contactTicket.setComment(contactTicketDto.getComment());
        contactTicket.setId(contactTicketDto.getId());
        contactTicket.setStatus(contactTicketDto.getStatus());
        Optional.ofNullable(contactTicketDto.getCreated()).map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactTicket::setCreated);
        contactTicket.setCreatedBy(contactTicketDto.getCreatedBy());
        Optional.ofNullable(contactTicketDto.getContactRequest()).map(ContactRequestMapper::toEntity)
            .ifPresent(contactTicket::setContactRequest);
        return contactTicket;
    }

    /**
     * Converts a {@link ContactTicket} entity to a {@link ContactTicketDto}.
     *
     * @param contactTicket the entity to be converted
     * @return the converted ContactTicketDto
     */
    public static ContactTicketDto toDto(ContactTicket contactTicket) {
        final ContactTicketDto contactTicketDto = new ContactTicketDto();
        contactTicketDto.setId(contactTicket.getId());
        contactTicketDto.setObjectId(contactTicket.getObjectId());
        contactTicketDto.setComment(contactTicket.getComment());
        Optional.ofNullable(contactTicket.getContactRequest()).map(ContactRequestMapper::toDto)
            .ifPresent(contactTicketDto::setContactRequest);
        contactTicketDto.setStatus(contactTicket.getStatus());
        Optional.ofNullable(contactTicket.getCreated()).map(ContactMapperUtil::localDateToDate)
            .ifPresent(contactTicketDto::setCreated);
        contactTicketDto.setCreatedBy(contactTicket.getCreatedBy());
        return contactTicketDto;
    }

}
