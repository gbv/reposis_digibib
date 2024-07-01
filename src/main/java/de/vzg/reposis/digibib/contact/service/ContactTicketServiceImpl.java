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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.mycore.common.MCRSessionMgr;
import org.mycore.common.config.MCRConfiguration2;
import org.mycore.services.queuedjob.MCRJobQueue;
import org.mycore.services.queuedjob.MCRJobQueueManager;

import de.vzg.reposis.digibib.contact.ContactConstants;
import de.vzg.reposis.digibib.contact.collect.ContactInfoCollectorJobAction;
import de.vzg.reposis.digibib.contact.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contact.dto.ContactTicketDto;
import de.vzg.reposis.digibib.contact.email.ContactEmailJobAction;
import de.vzg.reposis.digibib.contact.email.ContactEmailService;
import de.vzg.reposis.digibib.contact.exception.ContactEmailException;
import de.vzg.reposis.digibib.contact.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketInvalidException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketStatusException;
import de.vzg.reposis.digibib.contact.mapper.ContactRequestMapper;
import de.vzg.reposis.digibib.contact.mapper.ContactTicketMapper;
import de.vzg.reposis.digibib.contact.persistence.ContactInfoRepository;
import de.vzg.reposis.digibib.contact.persistence.ContactTicketRepository;
import de.vzg.reposis.digibib.contact.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import de.vzg.reposis.digibib.contact.validation.ContactValidator;

/**
 * This class implements the {@link ContactTicketService} interface.
 */
public class ContactTicketServiceImpl implements ContactTicketService {

    private static final String STAFF_EMAIL = MCRConfiguration2
        .getStringOrThrow(ContactConstants.CONF_PREFIX + "StaffEmail");

    private static final MCRJobQueue CONTACT_COLLECTOR_JOB_QUEUE = MCRJobQueueManager.getInstance()
        .getJobQueue(ContactInfoCollectorJobAction.class);

    private static final MCRJobQueue EMAIL_JOB_QUEUE = MCRJobQueueManager.getInstance()
        .getJobQueue(ContactEmailJobAction.class);

    private final ContactTicketRepository ticketRepository;

    private final ContactInfoRepository contactInfoRepository;

    private ContactTicketServiceImpl() {
        ticketRepository = new ContactTicketRepository();
        contactInfoRepository = new ContactInfoRepository();
    }

    /**
     * Returns the singleton instance of {@link ContactTicketServiceImpl}.
     *
     * @return the singleton instance
     */
    public static ContactTicketServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public ContactTicket createContactTicket(ContactTicketDto contactTicketDto) {
        if (!ContactValidator.getInstance().validateContactTicketDto(contactTicketDto)) {
            throw new ContactTicketInvalidException();
        }
        final ContactTicket contactTicket = ContactTicketMapper.toEntity(contactTicketDto);
        contactTicket.setId(null);
        contactTicket.setCreated(LocalDateTime.now());
        contactTicket.setCreatedBy(MCRSessionMgr.getCurrentSession().getUserInformation().getUserID());
        contactTicket.setStatus(ContactTicket.Status.OPEN);
        ticketRepository.insert(contactTicket);
        ticketRepository.flush();
        ticketRepository.detach(contactTicket);
        // may improve by using eventhandler
        CONTACT_COLLECTOR_JOB_QUEUE.add(ContactInfoCollectorJobAction.createJob(contactTicket.getId()));
        EMAIL_JOB_QUEUE.add(ContactEmailJobAction.createRequestConfirmationJob(contactTicket.getId()));
        EMAIL_JOB_QUEUE.add(ContactEmailJobAction.createNewRequestInfoJob(contactTicket.getId(), STAFF_EMAIL));
        return contactTicket;
    }

    @Override
    public ContactTicket getContactTicketById(UUID contactTicketId) {
        return ticketRepository.findById(contactTicketId).map(t -> {
            ticketRepository.detach(t);
            return t;
        }).orElseThrow(() -> new ContactTicketNotFoundException());
    }

    @Override
    public List<ContactTicket> getAllContactTickets() {
        return ticketRepository.findAll().stream().peek(ticketRepository::detach).toList();
    }

    @Override
    public ContactTicket updateContactTicket(ContactTicketDto contactTicketDto) {
        if (contactTicketDto.getId() == null) {
            throw new ContactTicketInvalidException("contact ticket id is required to update");
        }
        if (!ContactValidator.getInstance().validateContactTicketDto(contactTicketDto)) {
            throw new ContactTicketInvalidException();
        }
        final ContactTicket contactTicket = ticketRepository.findReferenceById(contactTicketDto.getId())
            .orElseThrow(() -> new ContactTicketNotFoundException());
        contactTicket.setStatus(contactTicketDto.getStatus());
        contactTicket.setComment(contactTicketDto.getComment());
        contactTicket.setContactRequest(ContactRequestMapper.toEntity(contactTicketDto.getContactRequest()));
        contactTicket.setObjectId(contactTicketDto.getObjectId());
        ticketRepository.flush();
        ticketRepository.detach(contactTicket);
        return contactTicket;
    }

    @Override
    public ContactTicket partialUpdateContactTicket(ContactTicketDto contactTicketDto) {
        if (contactTicketDto.getId() == null) {
            throw new ContactTicketInvalidException("contact ticket id is required to update");
        }
        final ContactTicket contactTicket = ticketRepository.findReferenceById(contactTicketDto.getId())
            .orElseThrow(() -> new ContactTicketNotFoundException());
        Optional.ofNullable(contactTicketDto.getComment()).ifPresent(contactTicket::setComment);
        Optional.ofNullable(contactTicketDto.getStatus()).ifPresent(contactTicket::setStatus);
        Optional.ofNullable(contactTicketDto.getObjectId()).ifPresent(contactTicket::setObjectId);
        ticketRepository.flush();
        ticketRepository.detach(contactTicket);
        return contactTicket;
    }

    @Override
    public void deleteTicket(UUID contactTicketId) {
        ticketRepository.findReferenceById(contactTicketId).ifPresentOrElse(ticketRepository::remove, () -> {
            throw new ContactTicketNotFoundException();
        });
    }

    @Override
    public void closeTicket(UUID contactTicketId) {
        final ContactTicket contactTicket = ticketRepository.findReferenceById(contactTicketId)
            .orElseThrow(() -> new ContactTicketNotFoundException());
        if (Objects.equals(ContactTicket.Status.CLOSED, contactTicket.getStatus())) {
            throw new ContactTicketStatusException("Ticket is already closed");
        }
        contactTicket.setStatus(ContactTicket.Status.CLOSED);
        ticketRepository.flush();
        ticketRepository.detach(contactTicket);
        EMAIL_JOB_QUEUE.add(ContactEmailJobAction.createRequestCompletedConfirmationJob(contactTicket.getId()));
    }

    @Override
    public ContactAttempt forwardContactRequest(UUID contactTicketId, UUID contactInfoId) {
        final ContactTicket contactTicket = ticketRepository.findById(contactTicketId)
            .orElseThrow(() -> new ContactTicketNotFoundException());
        final ContactInfo contactInfo = contactInfoRepository.findById(contactInfoId)
            .orElseThrow(() -> new ContactInfoNotFoundException());
        final ContactAttemptDto contactAttemptDto = new ContactAttemptDto();
        contactAttemptDto.setContactInfoId(contactInfoId);
        final ContactAttempt contactAttempt = ContactAttemptServiceImpl.getInstance()
            .createContactAttempt(contactTicketId, contactAttemptDto);
        contactAttemptDto.setId(contactAttempt.getId());
        try {
            ContactEmailService.sendRequestForwarding(contactTicket, contactInfo, contactAttempt.getId());
            contactAttemptDto.setSendDate(new Date());
        } catch (ContactEmailException e) {
            contactAttemptDto.setSendDate(new Date());
        } finally {
            ContactAttemptServiceImpl.getInstance().partialUpdateContactAttempt(contactAttemptDto);
        }
        // TODO may detach
        return contactAttempt;
    }

    private static class Holder {
        static final ContactTicketServiceImpl INSTANCE = new ContactTicketServiceImpl();
    }

}
