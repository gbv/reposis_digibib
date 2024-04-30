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

package de.vzg.reposis.digibib.contactrequest.restapi;

/**
 * Provides constants for REST.
 */
public class ContactRestConstants {

    /**
     * Header name for total count info.
     */
    public static final String HEADER_TOTAL_COUNT = "X-Total-Count";

    /**
     * Contact request id path parameter.
     */
    public static final String PATH_PARAM_CONTACT_REQUEST_ID = "contactRequestId";

    /**
     * Contact info id path parameter.
     */
    public static final String PATH_PARAM_CONTACT_INFO_ID = "contactInfoId";

    /**
     * Contact attempt id path parameter.
     */
    public static final String PATH_PARAM_CONTACT_ATTEMPT_ID = "contactAttemptId";

    /**
     * Contact request id query parameter.
     */
    public static final String QUERY_PARAM_CONTACT_REQUEST_ID = "requestId";

    /**
     * Contact info id query parameter.
     */
    public static final String QUERY_PARAM_CONTACT_INFO_ID = "contactId";

    /**
     * Offset query parameter.
     */
    public static final String QUERY_PARAM_OFFSET = "offset";

    /**
     * Limit query parameter.
     */
    public static final String QUERY_PARAM_LIMIT = "limit";

}
