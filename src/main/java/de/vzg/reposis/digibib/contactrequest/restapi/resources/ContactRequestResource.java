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
import java.util.UUID;

import org.mycore.restapi.annotations.MCRRequireTransaction;

import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.restapi.ContactRestConstants;
import de.vzg.reposis.digibib.contactrequest.service.ContactRequestFactory;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * RESTful resource for managing {@link ContactRequestDto} entities.
 * <p>
 * This resource provides endpoints for performing CRUD operations on contact requests.
 * </p>
 */
@Path("/")
public class ContactRequestResource {

    /**
     * Retrieves a list of all contact requests in {@link ContactRequestDto} representation.
     *
     * @param offset the number of items to skip (pagination offset)
     * @param limit the maximum number of items to return (pagination limit)
     * @param response the HTTP response object used to set headers, such as the total count of contact requests
     * @return a list of DTO elements
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContactRequestDto> getAllContactRequests(
        @DefaultValue("0") @QueryParam(ContactRestConstants.QUERY_PARAM_OFFSET) int offset,
        @DefaultValue("128") @QueryParam(ContactRestConstants.QUERY_PARAM_LIMIT) int limit,
        @Context HttpServletResponse response) {
        final List<ContactRequestDto> contactRequestDtos
            = ContactRequestFactory.getContactRequestService().getAllContactRequests();
        response.setHeader(ContactRestConstants.HEADER_TOTAL_COUNT, Integer.toString(contactRequestDtos.size()));
        return contactRequestDtos.stream().skip(offset).limit(limit).toList();
    }

    /**
     * Retrieves a specific contact request by its ID.
     *
     * @param contactRequestId the ID of the contact request to retrieve
     * @return the contact request in DTO representation
     */
    @GET
    @Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public ContactRequestDto
        getContactRequestById(@PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId) {
        return ContactRequestFactory.getContactRequestService().getContactRequestById(contactRequestId);
    }

    /**
     * Updates an existing contact request by its ID.
     *
     * @param contactRequestId the ID of the contact request to update
     * @param contactRequestDto the updated contact request data
     * @return a response indicating the outcome of the update operation
     * @throws BadRequestException if the provided update DTO is null
     */
    @PUT
    @Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response updateRequestById(
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        ContactRequestDto contactRequestDto) {
        if (contactRequestDto == null) {
            throw new BadRequestException();
        }
        contactRequestDto.setId(contactRequestId);
        ContactRequestFactory.getContactRequestService().updateContactRequest(contactRequestDto);
        return Response.noContent().build();
    }

    /**
     * Partially updates an existing contact request by its ID.
     *
     * @param contactRequestId the ID of the contact request to partially update
     * @param contactRequestDto the partial update data
     * @return a response indicating the outcome of the partial update operation
     * @throws BadRequestException if the provided update DTO is null
     */
    @PATCH
    @Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response partialUpdateRequestById(
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        ContactRequestPartialUpdateDto contactRequestDto) {
        if (contactRequestDto == null) {
            throw new BadRequestException();
        }
        ContactRequestFactory.getContactRequestService().partialUpdateContactRequest(contactRequestId,
            contactRequestDto);
        return Response.noContent().build();
    }

    /**
     * Deletes a {@link ContactRequestDto} by its ID.
     *
     * @param contactRequestId the ID of the contact request to delete
     * @return a response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response
        removeContactRequestById(@PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId) {
        ContactRequestFactory.getContactRequestService().deleteContactRequestById(contactRequestId);
        return Response.noContent().build();
    }

}
