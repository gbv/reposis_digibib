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

import de.vzg.reposis.digibib.contact.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contact.exception.ContactAttemptInvalidException;
import de.vzg.reposis.digibib.contact.exception.ContactAttemptNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketNotFoundException;
import de.vzg.reposis.digibib.contact.mapper.ContactAttemptMapper;
import de.vzg.reposis.digibib.contact.mapper.ContactMapperUtil;
import de.vzg.reposis.digibib.contact.persistence.ContactAttemptRepository;
import de.vzg.reposis.digibib.contact.persistence.ContactInfoRepository;
import de.vzg.reposis.digibib.contact.persistence.ContactTicketRepository;
import de.vzg.reposis.digibib.contact.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import de.vzg.reposis.digibib.contact.validation.ContactValidator;

/**
 * This class implements the {@link ContactAttemptService} interface.
 */
public class ContactAttemptServiceImpl implements ContactAttemptService {

    private final ContactTicketRepository contactTicketRepository;

    private final ContactInfoRepository contactInfoRepository;

    private final ContactAttemptRepository contactAttemptRepository;

    private ContactAttemptServiceImpl() {
        contactTicketRepository = new ContactTicketRepository();
        contactInfoRepository = new ContactInfoRepository();
        contactAttemptRepository = new ContactAttemptRepository();
    }

    /**
     * Returns the singleton instance of {@link ContactAttemptServiceImpl}.
     *
     * @return the singleton instance
     */
    public static ContactAttemptServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public ContactAttempt createContactAttempt(UUID contactTicketId, ContactAttemptDto contactAttemptDto) {
        if (!ContactValidator.getInstance().validateContactAttemptDto(contactAttemptDto)) {
            throw new ContactAttemptInvalidException();
        }
        final ContactInfo contactInfo = contactInfoRepository.findReferenceById(contactAttemptDto.getContactInfoId())
            .orElseThrow(() -> new ContactInfoNotFoundException());
        final ContactTicket contactTicket = contactTicketRepository.findReferenceById(contactTicketId)
            .orElseThrow(() -> new ContactTicketNotFoundException());
        final ContactAttempt contactAttempt = ContactAttemptMapper.toEntity(contactAttemptDto);
        contactAttempt.setContactInfo(contactInfo);
        contactAttempt.setContactTicket(contactTicket);
        contactAttemptRepository.insert(contactAttempt);
        contactAttemptRepository.flush();
        contactTicketRepository.detach(contactTicket);
        contactInfoRepository.detach(contactInfo);
        contactAttemptRepository.detach(contactAttempt);
        return contactAttempt;
    }

    @Override
    public List<ContactAttempt> getContactAttemptsForContactTicket(UUID contactTicketId) {
        return contactTicketRepository.findById(contactTicketId).map(t -> {
            final List<ContactAttempt> contactAttempts = t.getContactAttempts();
            contactAttempts.stream().forEach(a -> contactTicketRepository.detach(a.getContactTicket()));
            return contactAttempts;
        }).orElseThrow(() -> new ContactTicketNotFoundException());
    }

    @Override
    public List<ContactAttempt> getContactAttemptsForContactTicketAndContactInfo(UUID contactTicketId,
        UUID contactInfoId) {
        return contactTicketRepository.findById(contactTicketId).map(t -> {
            return t.getContactAttempts().stream()
                .filter(a -> Objects.equals(contactInfoId, a.getContactInfo().getId()))
                .peek(a -> contactTicketRepository.detach(a.getContactTicket())).toList();
        }).orElseThrow(() -> new ContactTicketNotFoundException());
    }

    @Override
    public ContactAttempt partialUpdateContactAttempt(ContactAttemptDto contactAttemptDto) {
        if (contactAttemptDto.getId() == null) {
            throw new ContactAttemptInvalidException("attempt id is required to update");
        }
        final ContactAttempt contactAttempt = contactAttemptRepository.findReferenceById(contactAttemptDto.getId())
            .orElseThrow(() -> new ContactAttemptNotFoundException());
        Optional.ofNullable(contactAttemptDto.getSuccessDate()).map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactAttempt::setSuccessDate);
        Optional.ofNullable(contactAttemptDto.getErrorDate()).map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactAttempt::setErrorDate);
        Optional.ofNullable(contactAttemptDto.getSendDate()).map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactAttempt::setSendDate);
        contactAttemptRepository.flush();
        contactAttemptRepository.detach(contactAttempt);
        return contactAttempt;
    }

    private static class Holder {
        static final ContactAttemptServiceImpl INSTANCE = new ContactAttemptServiceImpl();
    }

}
