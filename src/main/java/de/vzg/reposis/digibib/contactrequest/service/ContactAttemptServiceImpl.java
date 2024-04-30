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

import java.util.UUID;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactAttemptNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contactrequest.mapper.ContactAttemptMapper;
import de.vzg.reposis.digibib.contactrequest.mapper.ContactMapperUtil;
import de.vzg.reposis.digibib.contactrequest.model.ContactAttempt;
import de.vzg.reposis.digibib.contactrequest.repository.ContactAttemptRepository;
import de.vzg.reposis.digibib.contactrequest.validation.ContactAttemptValidator;

/**
 * Implementation of the {@link ContactAttemptService} interface that manages contact attempts.
 */
public class ContactAttemptServiceImpl implements ContactAttemptService {

    private final ContactAttemptRepository contactAttemptRepository;

    private final ContactAttemptValidator contactAttemptValidator;

    /**
     * Constructs a new {@code ContactAttemptServiceImpl} with the specified repository and validation service.
     *
     * @param contactAttemptRepository the repository used for retrieving and storing contact attempts
     * @param contactAttemptValidator the validation service used to validate contact attempt data
     */
    public ContactAttemptServiceImpl(ContactAttemptRepository contactAttemptRepository,
        ContactAttemptValidator contactAttemptValidator) {
        this.contactAttemptRepository = contactAttemptRepository;
        this.contactAttemptValidator = contactAttemptValidator;
    }

    @Override
    public ContactAttemptDto getContactAttemptById(UUID contactAttemptId) {
        return contactAttemptRepository.findById(contactAttemptId).map(ContactAttemptMapper::toDto)
            .orElseThrow(() -> new ContactInfoNotFoundException());
    }

    @Override
    public ContactAttemptDto partialUpdateContactAttempt(UUID contactAttemptId,
        ContactAttemptPartialUpdateDto contactAttemptDto) {
        contactAttemptValidator.validate(contactAttemptDto);
        final ContactAttempt contactAttempt = contactAttemptRepository.findById(contactAttemptId)
            .orElseThrow(() -> new ContactAttemptNotFoundException());
        contactAttemptDto.getErrorDate().getOptional().map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactAttempt::setErrorDate);
        contactAttemptDto.getSendDate().getOptional().map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactAttempt::setSendDate);
        contactAttemptDto.getSuccessDate().getOptional().map(ContactMapperUtil::dateToLocalDate)
            .ifPresent(contactAttempt::setSuccessDate);
        contactAttemptRepository.flush();
        return ContactAttemptMapper.toDto(contactAttempt);
    }

}
