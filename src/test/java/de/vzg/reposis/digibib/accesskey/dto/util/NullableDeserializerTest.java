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

package de.vzg.reposis.digibib.accesskey.dto.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mycore.common.MCRTestCase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.vzg.reposis.digibib.accesskey.dto.AccessKeyPartialUpdateDto;

public class NullableDeserializerTest extends MCRTestCase {

    @Test
    public void testDeserialize() throws JsonMappingException, JsonProcessingException {
        String jsonString = "{\"permission\": null, \"value\": \"test\"}";

        final AccessKeyPartialUpdateDto dto = new ObjectMapper().readValue(jsonString, AccessKeyPartialUpdateDto.class);
        assertNotNull(dto.getPermission());
        assertTrue(dto.getPermission().isPresent());
        assertNull(dto.getPermission().get());

        assertNotNull(dto.getValue());
        assertTrue(dto.getValue().isPresent());
        assertEquals("test", dto.getValue().get());

        assertNotNull(dto.getObjectId());
        assertFalse(dto.getObjectId().isPresent());
    }

}
