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

package de.vzg.reposis.digibib.contactrequest.collect;

import java.util.List;

import org.mycore.datamodel.metadata.MCRObject;

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;

/**
 * Interface for collecting contact information from an {@link MCRObject}.
 *
 * This interface defines a method for extracting and returning a list of {@link ContactInfoDto}
 * elements from an instance of {@link MCRObject}. Implementations of this interface should provide
 * the logic for how the contact information is collected from the given object.
 */
public interface ContactInfoCollector {

    /**
     * Collects and returns a list of {@link ContactInfoDto} elements from the specified {@link MCRObject}.
     *
     * @param object the object from which to collect contact information
     * @return a list of DTO elements representing the collected contact information
     */
    public List<ContactInfoDto> collect(MCRObject object);
}
