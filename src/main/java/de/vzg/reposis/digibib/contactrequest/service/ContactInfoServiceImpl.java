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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoAlreadyExistsException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestException;
import de.vzg.reposis.digibib.contactrequest.mapper.ContactInfoMapper;
import de.vzg.reposis.digibib.contactrequest.persistence.ContactInfoRepository;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contactrequest.validation.ContactRequestValidatorImpl;

/**
 * This class implements the {@link ContactInfoService} interface.
 */
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;

    /**
     * Constructs new Service with given repository.
     *
     * @param contactInfoRepository the contact info repository
     */
    protected ContactInfoServiceImpl(ContactInfoRepository contactInfoRepository) {
        this.contactInfoRepository = contactInfoRepository;
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
    public ContactInfoDto getContactInfoById(UUID contactInfoId) {
        return contactInfoRepository.findById(contactInfoId).map(ContactInfoMapper::toDto)
            .orElseThrow(() -> new ContactInfoNotFoundException());
    }

    @Override
    public ContactInfoDto updateContactInfo(ContactInfoDto contactInfoDto) {
        if (contactInfoDto.getId() == null) {
            throw new IllegalArgumentException("contact info id is required to update");
        }
        ContactRequestValidatorImpl.getInstance().validateContactInfoDto(contactInfoDto);
        final ContactInfo contactInfo = contactInfoRepository.findById(contactInfoDto.getId())
            .orElseThrow(() -> new ContactInfoNotFoundException());
        if (!Objects.equals(contactInfo.getEmail(), contactInfoDto.getEmail())) {
            if (checkContactExists(contactInfo.getContactRequest().getContactInfos(), contactInfoDto)) {
                throw new ContactInfoAlreadyExistsException();
            }
        }
        contactInfo.setName(contactInfoDto.getName());
        contactInfo.setEmail(contactInfoDto.getEmail());
        contactInfo.setReference(contactInfoDto.getReference());
        contactInfo.setOrigin(contactInfoDto.getOrigin());
        contactInfoRepository.flush();
        return ContactInfoMapper.toDto(contactInfo);
    }

    @Override
    public ContactInfoDto partialUpdateContactInfo(UUID contactInfoId, ContactInfoPartialUpdateDto contactInfoDto) {
        ContactRequestValidatorImpl.getInstance().validateContactInfoDto(contactInfoDto);
        final ContactInfo contactInfo = contactInfoRepository.findById(contactInfoId)
            .orElseThrow(() -> new ContactInfoNotFoundException());
        contactInfoDto.getName().getOptional().ifPresent(contactInfo::setName);
        contactInfoDto.getOrigin().getOptional().ifPresent(contactInfo::setOrigin);
        contactInfoDto.getReference().getOptional().ifPresent(contactInfo::setReference);
        contactInfoRepository.flush();
        return ContactInfoMapper.toDto(contactInfo);
    }

    @Override
    public void deleteContactInfoById(UUID contactInfoId) {
        final ContactInfo contactInfoData = contactInfoRepository.findById(contactInfoId)
            .orElseThrow(() -> new ContactInfoNotFoundException());
        if (contactInfoData.getContactAttempts().size() > 0) {
            throw new ContactRequestException("Contact is assoicated to attempts");
        }
        contactInfoRepository.remove(contactInfoData);
    }

    /**
     * Checks if contact info list includes contact info.
     *
     * @param contactInfos the contact info list
     * @param contactInfoDto the contact info
     * @return true if includes
     */
    protected static boolean checkContactExists(List<ContactInfo> contactInfos, ContactInfoDto contactInfoDto) {
        return contactInfos.stream().anyMatch(i -> Objects.equals(i.getEmail(), contactInfoDto.getEmail()));
    }

    private static class Holder {
        static final ContactInfoServiceImpl INSTANCE = new ContactInfoServiceImpl(new ContactInfoRepository());
    }
}
