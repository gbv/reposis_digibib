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

package de.vzg.reposis.digibib.accesskey.model;

import org.mycore.access.MCRAccessManager;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration representing the type of access key.
 *
 * The {@code AccessKeyType} enum defines two types of access keys:
 * <ul>
 *     <li>{@link #READ} - Represents read-only access.</li>
 *     <li>{@link #WRITE} - Represents write access.</li>
 * </ul>
 */
public enum PermissionType {

    /**
     * Represents read-only access.
     */
    READ(MCRAccessManager.PERMISSION_READ),

    /**
     * Represents write access.
     */
    WRITE(MCRAccessManager.PERMISSION_WRITE);

    private final String value;

    PermissionType(String value) {
        this.value = value;
    }

    /**
     * Returns {@link PermissionType} from value.
     *
     * @param value the permission type value
     * @return the permission type
     * @throws IllegalArgumentException if value is unknown
     */
    public static PermissionType fromValue(String value) {
        return switch (value) {
            case MCRAccessManager.PERMISSION_READ:
                yield READ;
            case MCRAccessManager.PERMISSION_WRITE:
                yield WRITE;
            default:
                throw new IllegalArgumentException("Unexpected value: " + value);
        };
    }

    /**
     * Returns {@link PermissionType} value.
     *
     * @return the value
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
