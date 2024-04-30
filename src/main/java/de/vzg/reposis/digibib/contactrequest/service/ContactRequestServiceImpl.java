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

package de.vzg.reposis.digibib.contactrequest.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.mycore.common.MCRSessionMgr;
import org.mycore.common.events.MCREvent;
import org.mycore.common.events.MCREventManager;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestSummaryDto;
import de.vzg.reposis.digibib.contactrequest.event.ContactRequestEventHandlerBase;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoAlreadyExistsException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestValidationException;
import de.vzg.reposis.digibib.contactrequest.mapper.ContactAttemptMapper;
import de.vzg.reposis.digibib.contactrequest.mapper.ContactInfoMapper;
import de.vzg.reposis.digibib.contactrequest.mapper.ContactRequestBodyMapper;
import de.vzg.reposis.digibib.contactrequest.mapper.ContactRequestMapper;
import de.vzg.reposis.digibib.contactrequest.model.ContactAttempt;
import de.vzg.reposis.digibib.contactrequest.model.ContactInfo;
import de.vzg.reposis.digibib.contactrequest.model.ContactRequest;
import de.vzg.reposis.digibib.contactrequest.repository.ContactAttemptRepository;
import de.vzg.reposis.digibib.contactrequest.repository.ContactInfoRepository;
import de.vzg.reposis.digibib.contactrequest.repository.ContactRequestRepository;
import de.vzg.reposis.digibib.contactrequest.validation.ValidatorFacade;

/**
 * Implementation of the {@link ContactRequestService} interface that manages
 * operations related to {@link ContactRequest} entities.
 */
public class ContactRequestServiceImpl implements ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;

    private final ContactInfoRepository contactInfoRepository;

    private final ContactAttemptRepository contactAttemptRepository;

    private final ValidatorFacade validatorFacade;

    /**
     * Constructs a new instance of {@code ContactRequestServiceImpl} with
     * the specified repositories and validation services.
     *
     * @param contactRequestRepository the repository for managing contact request entities
     * @param contactInfoRepository the repository for managing contact info entities
     * @param contactAttemptRepository the repository for managing contact attempt entities
     * @param validatorFacade the facade providing validation services for DTOs
     */
    public ContactRequestServiceImpl(ContactRequestRepository contactRequestRepository,
        ContactInfoRepository contactInfoRepository, ContactAttemptRepository contactAttemptRepository,
        ValidatorFacade validatorFacade) {
        this.contactRequestRepository = contactRequestRepository;
        this.contactInfoRepository = contactInfoRepository;
        this.contactAttemptRepository = contactAttemptRepository;
        this.validatorFacade = validatorFacade;
    }

    @Override
    public ContactRequestDto createContactRequest(ContactRequestDto contactRequestDto) {
        validatorFacade.getContactRequestValidator().validate(contactRequestDto);
        final ContactRequest contactRequest = ContactRequestMapper.toEntity(contactRequestDto);
        contactRequest.setId(null);
        contactRequest.setCreated(LocalDateTime.now());
        contactRequest.setCreatedBy(MCRSessionMgr.getCurrentSession().getUserInformation().getUserID());
        contactRequest.setStatus(ContactRequest.Status.OPEN);
        final ContactRequest insertedContactRequest = contactRequestRepository.save(contactRequest);
        contactRequestRepository.flush();
        final MCREvent evt = MCREvent.customEvent(ContactRequestEventHandlerBase.CONTACT_REQUEST_TYPE,
            MCREvent.EventType.CREATE);
        evt.put(ContactRequestEventHandlerBase.CONTACT_REQUEST_TYPE,
            ContactRequestMapper.toDto(insertedContactRequest));
        MCREventManager.instance().handleEvent(evt);
        return ContactRequestMapper.toDto(insertedContactRequest);
    }

    @Override
    public ContactRequestDto getContactRequestById(UUID contactRequestId) {
        return contactRequestRepository.findById(contactRequestId).map(ContactRequestMapper::toDto)
            .orElseThrow(() -> new ContactRequestNotFoundException());
    }

    @Override
    public List<ContactRequestDto> getAllContactRequests() {
        return contactRequestRepository.findAll().stream().map(ContactRequestMapper::toDto).toList();
    }

    @Override
    public ContactRequestDto updateContactRequest(ContactRequestDto contactRequestDto) {
        if (contactRequestDto.getId() == null) {
            throw new ContactRequestValidationException("contact request id is required to update");
        }
        validatorFacade.getContactRequestValidator().validate(contactRequestDto);
        final ContactRequest contactRequest = contactRequestRepository.findById(contactRequestDto.getId())
            .orElseThrow(() -> new ContactRequestNotFoundException());
        contactRequest.setStatus(ContactRequest.Status.valueOf(contactRequestDto.getStatus()));
        contactRequest.setComment(contactRequestDto.getComment());
        contactRequest.setBody(ContactRequestBodyMapper.toEntity(contactRequestDto.getBody()));
        contactRequest.setObjectId(contactRequestDto.getObjectId());
        contactRequestRepository.flush();
        return ContactRequestMapper.toDto(contactRequest);
    }

    @Override
    public ContactRequestDto partialUpdateContactRequest(UUID contactRequestId,
        ContactRequestPartialUpdateDto contactRequestDto) {
        validatorFacade.getContactRequestValidator().validate(contactRequestDto);
        final ContactRequest contactRequest = contactRequestRepository.findById(contactRequestId)
            .orElseThrow(() -> new ContactRequestNotFoundException());
        contactRequestDto.getBody().getOptional().map(ContactRequestBodyMapper::toEntity)
            .ifPresent(contactRequest::setBody);
        contactRequestDto.getComment().getOptional().ifPresent(contactRequest::setComment);
        contactRequestDto.getObjectId().getOptional().ifPresent(contactRequest::setObjectId);
        contactRequestDto.getStatus().getOptional().ifPresent(contactRequest::setStatus);
        contactRequestRepository.flush();
        return ContactRequestMapper.toDto(contactRequest);
    }

    @Override
    public void deleteContactRequestById(UUID contactRequestId) {
        contactRequestRepository.findById(contactRequestId).ifPresentOrElse(contactRequestRepository::remove,
            () -> {
                throw new ContactRequestNotFoundException();
            });
    }

    @Override
    public ContactInfoDto createContactInfo(UUID contactRequestId, ContactInfoDto contactInfoDto) {
        validatorFacade.getContactInfoValidator().validate(contactInfoDto);
        final ContactRequest contactRequest = contactRequestRepository.findById(contactRequestId)
            .orElseThrow(() -> new ContactRequestNotFoundException());
        if (ContactInfoServiceImpl.checkContactExists(contactRequest.getContactInfos(), contactInfoDto)) {
            throw new ContactInfoAlreadyExistsException();
        }
        final ContactInfo contactInfo = ContactInfoMapper.toEntity(contactInfoDto);
        contactInfo.setId(null);
        contactInfo.setContactRequest(contactRequest);
        final ContactInfo createdContactInfo = contactInfoRepository.save(contactInfo);
        contactInfoRepository.flush();
        return ContactInfoMapper.toDto(createdContactInfo);
    }

    @Override
    public List<ContactInfoDto> getContactInfosById(UUID contactRequestId) {
        final ContactRequest contactRequest = contactRequestRepository.findById(contactRequestId)
            .orElseThrow(() -> new ContactRequestNotFoundException());
        return contactRequest.getContactInfos().stream().map(ContactInfoMapper::toDto).toList();
    }

    @Override
    public ContactAttemptDto createContactAttempt(UUID contactRequestId, ContactAttemptDto contactAttemptDto) {
        validatorFacade.getContactAttemptValidator().validate(contactAttemptDto);
        final ContactRequest contactRequest = contactRequestRepository.findById(contactRequestId)
            .orElseThrow(() -> new ContactRequestNotFoundException());
        final ContactAttempt contactAttempt = ContactAttemptMapper.toEntity(contactAttemptDto);
        contactAttempt.setContactRequest(contactRequest);
        final ContactAttempt createdContactAttempt = contactAttemptRepository.save(contactAttempt);
        contactAttemptRepository.flush();
        final MCREvent evt = MCREvent.customEvent(ContactRequestEventHandlerBase.CONTACT_ATTEMPT_TYPE,
            MCREvent.EventType.CREATE);
        evt.put(ContactRequestEventHandlerBase.CONTACT_ATTEMPT_TYPE,
            ContactAttemptMapper.toDto(createdContactAttempt));
        evt.put(ContactRequestEventHandlerBase.CONTACT_REQUEST_TYPE, ContactRequestMapper.toDto(contactRequest));
        MCREventManager.instance().handleEvent(evt);
        return ContactAttemptMapper.toDto(createdContactAttempt);
    }

    @Override
    public List<ContactAttemptDto> getContactAttemptsById(UUID contactRequestId) {
        return contactRequestRepository.findById(contactRequestId).map(t -> {
            return t.getEmailContactAttempts().stream().map(ContactAttemptMapper::toDto).toList();
        }).orElseThrow(() -> new ContactRequestNotFoundException());
    }

    @Override
    public ContactRequestSummaryDto getStatusSummaryById(UUID contactRequestId) {
        final ContactRequest contactRequest = contactRequestRepository.findById(contactRequestId)
            .orElseThrow(() -> new ContactRequestNotFoundException());
        return ContactRequestMapper.toSummaryDto(contactRequest);
    }

}
