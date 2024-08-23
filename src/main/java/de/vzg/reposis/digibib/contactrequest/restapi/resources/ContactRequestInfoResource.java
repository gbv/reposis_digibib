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

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.restapi.ContactRestConstants;
import de.vzg.reposis.digibib.contactrequest.service.ContactRequestFactory;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * RESTful resource for managing contact info entities associated with a contact request.
 * <p>
 * This resource provides endpoints to perform CRUD operations on contact information within a specific contact request.
 * These operations include creating, retrieving, updating, partially updating, and deleting contact information.
 * </p>
 */
@Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID + "}/contacts")
public class ContactRequestInfoResource {

    @Context
    private UriInfo info;

    /**
     * Creates a new contact info for a given contact request.
     *
     * @param contactRequestId the ID of the contact request to which the contact information is to be added
     * @param contactInfoDto the contact information details in DTO representation to be created
     * @return a response indicating the outcome of the creation operation,
     *         including the URI of the newly created contact information
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response createContactInfo(
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        ContactInfoDto contactInfoDto) {
        final ContactInfoDto contactInfo
            = ContactRequestFactory.getContactRequestService().createContactInfo(contactRequestId, contactInfoDto);
        return Response.created(info.getAbsolutePathBuilder().path(contactInfo.getId().toString()).build()).build();
    }

    /**
     * Retrieves a list of contact info elements associated with a specific contact request.
     *
     * @param offset the number of items to skip (pagination)
     * @param limit the maximum number of items to return (pagination)
     * @param response the HTTP response to set headers, such as the total count of contact information
     * @param contactRequestId the ID of the contact request for which contact information is to be retrieved
     * @return a list of contact info DTOs associated with the specified contact request
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContactInfoDto> getContactInfos(
        @DefaultValue("0") @QueryParam(ContactRestConstants.QUERY_PARAM_OFFSET) int offset,
        @DefaultValue("128") @QueryParam(ContactRestConstants.QUERY_PARAM_LIMIT) int limit,
        @Context HttpServletResponse response,
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId) {
        final List<ContactInfoDto> contactInfos
            = ContactRequestFactory.getContactRequestService().getContactInfosById(contactRequestId);
        response.setHeader(ContactRestConstants.HEADER_TOTAL_COUNT, Integer.toString(contactInfos.size()));
        return contactInfos.stream().skip(offset).limit(limit).toList();
    }

    /**
     * Retrieves a specific contact info in {@link ContactInfoDto} representation by its ID.
     *
     * @param contactRequestId the ID of the contact request containing the contact information
     * @param contactInfoId the ID of the contact information to retrieve
     * @return a DTO representing the requested contact information
     */
    @GET
    @Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_INFO_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public ContactInfoDto getContactInfoById(
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId) {
        return ContactRequestFactory.getContactInfoService().getContactInfoById(contactInfoId);
    }

    /**
     * Updates a specific contact info associated with a specific contact request by its ID.
     *
     * @param contactRequestId the ID of the contact request containing the contact information
     * @param contactInfoId the ID of the contact information to update
     * @param contactInfoDto the updated contact information details
     * @return a response indicating the outcome of the update operation
     * @throws BadRequestException if the provided contact info DTO is null
     */
    @PUT
    @Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_INFO_ID + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response updateContactInfoById(
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId, ContactInfoDto contactInfoDto) {
        if (contactInfoDto == null) {
            throw new BadRequestException();
        }
        contactInfoDto.setId(contactInfoId);
        ContactRequestFactory.getContactInfoService().updateContactInfo(contactInfoDto);
        return Response.noContent().build();
    }

    /**
     * Partially updates a specific contact info associated with a specific contact request by its ID.
     *
     * @param contactRequestId the ID of the contact request containing the contact information
     * @param contactInfoId the ID of the contact information to partially update
     * @param contactInfoDto the partial update data
     * @return a response indicating the outcome of the partial update operation
     * @throws BadRequestException if the provided partial update DTO is null
     */
    @PATCH
    @Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_INFO_ID + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response partialUpdateContactInfoById(
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId,
        ContactInfoPartialUpdateDto contactInfoDto) {
        if (contactInfoDto == null) {
            throw new BadRequestException();
        }
        ContactRequestFactory.getContactInfoService().partialUpdateContactInfo(contactInfoId, contactInfoDto);
        return Response.noContent().build();
    }

    /**
     * Deletes a contact info of a contact request by its ID.
     *
     * @param contactRequestId the contact request id
     * @param contactInfoId the contact info id
     * @return a response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/{" + ContactRestConstants.PATH_PARAM_CONTACT_INFO_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response removeContactInfoById(
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @PathParam(ContactRestConstants.PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId) {
        ContactRequestFactory.getContactInfoService().deleteContactInfoById(contactInfoId);
        return Response.noContent().build();
    }

}
