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

package de.vzg.reposis.digibib.accesskey.rest.resource;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.mycore.access.MCRAccessException;
import org.mycore.restapi.v2.MCRErrorResponse;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * {@link ExceptionMapper} to handle {@link MCRAccessException}.
 */
@Provider
public class AccessExceptionMapper implements ExceptionMapper<MCRAccessException> {

    @Context
    Request request;

    @Override
    public Response toResponse(MCRAccessException e) {
        return getResponse(e, Response.Status.FORBIDDEN.getStatusCode(), "notAuthorized");
    }

    private static Response getResponse(Exception e, int statusCode, String errorCode) {
        MCRErrorResponse response = MCRErrorResponse.fromStatus(statusCode).withCause(e).withMessage(e.getMessage())
            .withDetail(Optional.of(e).map(ex -> (ex instanceof WebApplicationException) ? ex.getCause() : ex)
                .map(Object::getClass).map(Class::getName).orElse(null))
            .withErrorCode(errorCode);
        LogManager.getLogger().error(response::toException);
        return Response.status(response.getStatus()).entity(response).build();
    }
}
