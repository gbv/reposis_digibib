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

package de.vzg.reposis.digibib.contact.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import de.vzg.reposis.digibib.contact.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contact.exception.ContactInfoAlreadyExistsException;
import de.vzg.reposis.digibib.contact.exception.ContactInfoInvalidException;
import de.vzg.reposis.digibib.contact.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketNotFoundException;
import de.vzg.reposis.digibib.contact.mapper.ContactInfoMapper;
import de.vzg.reposis.digibib.contact.persistence.ContactInfoRepository;
import de.vzg.reposis.digibib.contact.persistence.ContactTicketRepository;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import de.vzg.reposis.digibib.contact.validation.ContactValidator;

/**
 * This class implements the {@link ContactInfoService} interface.
 */
public class ContactInfoServiceImpl implements ContactInfoService {

    private ContactTicketRepository ticketRepository;

    private ContactInfoRepository contactInfoRepository;

    private ContactInfoServiceImpl() {
        ticketRepository = new ContactTicketRepository();
        contactInfoRepository = new ContactInfoRepository();
    }

    /**
     * Returns the singleton instance of {@link ContactInfoServiceImpl}.
     *
     * @return the singleton instance
     */
    public static ContactInfoServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public ContactInfo createContactInfo(UUID contactTicketId, ContactInfoDto contactInfoDto) {
        if (!ContactValidator.getInstance().validateContactInfoDto(contactInfoDto)) {
            throw new ContactInfoInvalidException();
        }
        final ContactTicket contactTicket = ticketRepository.findReferenceById(contactTicketId)
            .orElseThrow(() -> new ContactTicketNotFoundException());
        final ContactInfo contactInfo = ContactInfoMapper.toEntity(contactInfoDto);
        if (checkContactExists(contactTicket.getContactInfos(), ContactInfoMapper.toEntity(contactInfoDto))) {
            throw new ContactInfoAlreadyExistsException();
        }
        contactInfo.setId(null);
        contactInfo.setContactTicket(contactTicket);
        contactInfoRepository.insert(contactInfo);
        contactInfoRepository.flush();
        return contactInfo;
    }

    @Override
    public ContactInfo getContactInfoById(UUID contactInfoId) {
        return contactInfoRepository.findById(contactInfoId).map(c -> {
            contactInfoRepository.detach(c);
            return c;
        }).orElseThrow(() -> new ContactInfoNotFoundException());
    }

    @Override
    public List<ContactInfo> getContactInfosWithChildrenForContactTicket(UUID contactTicketId) {
        return ticketRepository.findById(contactTicketId).map(t -> {
            final List<ContactInfo> contactInfos = t.getContactInfos().stream().peek(c -> {
                c.getContactAttempts().size();
            }).peek(contactInfoRepository::detach).toList();
            ticketRepository.detach(t);
            return contactInfos;
        }).orElseThrow(() -> new ContactTicketNotFoundException());
    }

    @Override
    public ContactInfo updateContactInfo(ContactInfoDto contactInfoDto) {
        if (contactInfoDto.getId() == null) {
            throw new ContactInfoInvalidException("contact info id is required to update");
        }
        if (!ContactValidator.getInstance().validateContactInfoDto(contactInfoDto)) {
            throw new ContactInfoInvalidException();
        }
        final ContactInfo contactInfo = contactInfoRepository.findReferenceById(contactInfoDto.getId())
            .orElseThrow(() -> new ContactInfoNotFoundException());
        if (!Objects.equals(contactInfo.getEmail(), contactInfoDto.getEmail())) {
            if (checkContactExists(contactInfo.getContactTicket().getContactInfos(),
                ContactInfoMapper.toEntity(contactInfoDto))) {
                throw new ContactInfoAlreadyExistsException();
            }
        }
        contactInfo.setName(contactInfoDto.getName());
        contactInfo.setEmail(contactInfoDto.getEmail());
        contactInfo.setReference(contactInfoDto.getReference());
        contactInfo.setOrigin(contactInfoDto.getOrigin());
        contactInfoRepository.flush();
        contactInfoRepository.detach(contactInfo);
        return contactInfo;
    }

    @Override
    public ContactInfo partialUpdateContactInfo(ContactInfoDto contactInfoDto) {
        if (contactInfoDto.getId() == null) {
            throw new ContactInfoInvalidException("contact info id is required to update");
        }
        final ContactInfo contactInfo = contactInfoRepository.findReferenceById(contactInfoDto.getId())
            .orElseThrow(() -> new ContactInfoNotFoundException());
        Optional.ofNullable(contactInfoDto.getEmail()).ifPresent(contactInfo::setEmail);
        Optional.ofNullable(contactInfoDto.getName()).ifPresent(contactInfo::setName);
        Optional.ofNullable(contactInfoDto.getOrigin()).ifPresent(contactInfo::setOrigin);
        Optional.ofNullable(contactInfoDto.getReference()).ifPresent(contactInfo::setReference);
        contactInfoRepository.flush();
        contactInfoRepository.detach(contactInfo);
        return contactInfo;
    }

    @Override
    public void deleteContactInfo(UUID contactInfoId) {
        final ContactInfo contactInfoData = contactInfoRepository.findReferenceById(contactInfoId)
            .orElseThrow(() -> new ContactInfoNotFoundException());
        contactInfoRepository.remove(contactInfoData);
    }

    private boolean checkContactExists(List<ContactInfo> contactInfos, ContactInfo contactInfo) {
        return contactInfos.stream().anyMatch(i -> Objects.equals(i.getEmail(), contactInfo.getEmail()));
    }

    private static class Holder {
        static final ContactInfoServiceImpl INSTANCE = new ContactInfoServiceImpl();
    }
}
