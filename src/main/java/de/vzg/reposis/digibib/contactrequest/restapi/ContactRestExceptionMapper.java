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

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.mycore.restapi.v2.MCRErrorResponse;

import de.vzg.reposis.digibib.contactrequest.exception.ContactAttemptNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactInfoNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestValidationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

/**
 * {@link ExceptionMapper} to handle {@link ContactRequestException}.
 */
public class ContactRestExceptionMapper implements ExceptionMapper<ContactRequestException> {

    @Context
    private Request request;

    @Override
    public Response toResponse(ContactRequestException e) {
        if (e instanceof ContactRequestNotFoundException || e instanceof ContactInfoNotFoundException
            || e instanceof ContactAttemptNotFoundException) {
            return getResponse(e, Response.Status.NOT_FOUND.getStatusCode(), e.getErrorCode());
        }
        if (e instanceof ContactRequestValidationException) {
            return getResponse(e, Response.Status.BAD_REQUEST.getStatusCode(), e.getErrorCode());
        }
        return getResponse(e, Response.Status.BAD_REQUEST.getStatusCode(), e.getErrorCode());
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
