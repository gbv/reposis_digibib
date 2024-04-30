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

package de.vzg.reposis.digibib.contactrequest.restapi.resources;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.mycore.restapi.annotations.MCRRequireTransaction;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.restapi.ContactRestConstants;
import de.vzg.reposis.digibib.contactrequest.service.ContactRequestFactory;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * RESTful resource for managing contact attempt entities associated with a contact request.
 * <p>
 * This resource provides endpoints to handle operations related to contact attempts for a specific contact request.
 * Operations include retrieving contact attempts by ID, querying contact attempts based on email reference,
 * and creating new contact attempts.
 * </p>
 */
@Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID + "}/attempts")
public class ContactRequestAttemptResource {

    private static final String QUERY_PARAM_REFERENCE = "reference";

    @Context
    private UriInfo info;

    /**
     * Retrieves a specific contact attempt in {@link ContactAttemptDto} representation by its ID.
     *
     * @param contactRequestId the ID of the contact request
     * @param contactAttemptId the ID of the contact attempt to retrieve
     * @return a DTO representing the contact attempt
     */
    @GET
    @Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_ATTEMPT_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public ContactAttemptDto getContactInfoById(
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_ATTEMPT_ID) UUID contactAttemptId) {
        return ContactRequestFactory.getContactAttemptService().getContactAttemptById(contactAttemptId);
    }

    /**
     * Retrieves a list of contact attempts as {@link ContactAttemptDto} elements associated
     * with the specified contact request.
     *
     * @param contactRequestId the ID of the contact request
     * @param reference optional filter for recipient reference
     * @param offset the number of items to skip (pagination)
     * @param limit the maximum number of items to return (pagination)
     * @param response the HTTP response to set headers
     * @return a list of DTOs matching the criteria
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContactAttemptDto> getContactAttempts(
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @DefaultValue("") @QueryParam(QUERY_PARAM_REFERENCE) String reference,
        @DefaultValue("0") @QueryParam(ContactRestConstants.QUERY_PARAM_OFFSET) int offset,
        @DefaultValue("128") @QueryParam(ContactRestConstants.QUERY_PARAM_LIMIT) int limit,
        @Context HttpServletResponse response) {
        List<ContactAttemptDto> contactAttempts
            = ContactRequestFactory.getContactRequestService().getContactAttemptsById(contactRequestId);
        if (!reference.isEmpty()) {
            contactAttempts
                = contactAttempts.stream().filter(a -> Objects.equals(reference, a.getRecipientReference())).toList();
        }
        response.setHeader(ContactRestConstants.HEADER_TOTAL_COUNT, Integer.toString(contactAttempts.size()));
        return contactAttempts.stream().skip(offset).limit(limit).toList();
    }

    /**
     * Creates a new contact attempt for the specified contact request.
     *
     * @param contactRequestId the ID of the contact request to associate with the new attempt
     * @param contactAttemptDto the DTO representing the contact attempt to create
     * @return a response indicating the outcome of the creation operation
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response createContactAttempt(
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        ContactAttemptDto contactAttemptDto) {
        final ContactAttemptDto createdAttemptDto = ContactRequestFactory.getContactRequestService()
            .createContactAttempt(contactRequestId, contactAttemptDto);
        return Response.created(info.getAbsolutePathBuilder().path(createdAttemptDto.getId().toString()).build())
            .build();
    }

}
