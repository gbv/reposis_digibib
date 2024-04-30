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

import java.io.IOException;
import java.util.Objects;

import org.mycore.access.MCRAccessManager;

import jakarta.annotation.Priority;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;

/**
 * A filter for handling authorization checks for contact request management.
 */
@Priority(Priorities.AUTHORIZATION)
public class ContactRestAuthorizationFilter implements ContainerRequestFilter {

    private static final String CONTACT_REQUEST_ID = "contact-request";

    private static final String MANAGE_PERMISSION = "manage";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (Objects.equals(HttpMethod.OPTIONS, requestContext.getMethod())) {
            return;
        }
        if (!MCRAccessManager.checkPermission(CONTACT_REQUEST_ID, MANAGE_PERMISSION)) {
            throw new ForbiddenException("You do not have manage permission on contact-requests");
        }
    }

}
