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
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;

/**
 * This interface defines operations related to {@link ContactAttempt}.
 * It allows adding new contact attempt to a {@link ContactInfo}.
 */
public interface ContactAttemptService {

    /**
     * Partially updates an existing {@link ContactAttempt}.
     *
     * @param contactAttemptId the id of the contact attempt
     * @param contactAttemptDto the DTO representing the contact attempt to be updated
     * @return the updated contact attempt
     * @throws ContactAttemptNotFoundException if the contact attempt does not exist
     */
    ContactAttemptDto partialUpdateContactAttempt(UUID contactAttemptId,
        ContactAttemptPartialUpdateDto contactAttemptDto);
}
