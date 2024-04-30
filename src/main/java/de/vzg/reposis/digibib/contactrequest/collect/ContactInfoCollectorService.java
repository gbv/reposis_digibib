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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mycore.common.config.MCRConfiguration2;
import org.mycore.datamodel.metadata.MCRObject;

import de.vzg.reposis.digibib.contactrequest.ContactRequestConstants;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;

/**
 * Service class for collecting contact information from various sources.
 * <p>
 * This service utilizes a list of {@link ContactInfoCollector} instances that are configured
 * and initialized based on properties defined in the system configuration.
 * </p>
 */
public class ContactInfoCollectorService {

    private final static List<ContactInfoCollector> COLLECTORS = new ArrayList<>();

    private static final String COLLECTOR_PROP_PREFIX = ContactRequestConstants.CONF_PREFIX + "ContactInfoCollector.";

    static {
        MCRConfiguration2.getSubPropertiesMap(COLLECTOR_PROP_PREFIX).entrySet().stream()
            .filter(p -> p.getKey().endsWith(".Class")).map(p -> COLLECTOR_PROP_PREFIX.concat(p.getKey()))
            .map(p -> MCRConfiguration2.<ContactInfoCollector>getSingleInstanceOf(p).orElseThrow())
            .forEach(COLLECTORS::add);
    }

    /**
     * Collects and aggregates contact information from a specified {@link MCRObject}.
     *
     * @param object the object from which contact information is collected
     * @return a list of DTO objects representing the collected contact information
     */
    public List<ContactInfoDto> collectContactInfos(MCRObject object) {
        return COLLECTORS.stream().map(c -> c.collect(object)).flatMap(List::stream).collect(Collectors.toList());
    }
}
