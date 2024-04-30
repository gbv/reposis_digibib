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

package de.vzg.reposis.digibib.contactrequest.restapi.v2;

import java.util.List;
import java.util.UUID;

import org.mycore.restapi.annotations.MCRRequireTransaction;

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactRequest;
import de.vzg.reposis.digibib.contactrequest.service.ContactInfoServiceImpl;
import de.vzg.reposis.digibib.contactrequest.service.ContactRequestServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

/**
 * Provides methods to manage {@link ContactInfo} of {@link ContactRequest}.
 */
@Path("contact-requests/{" + ContactRequestResource.PATH_PARAM_CONTACT_REQUEST_ID + "}/contacts")
public class ContactInfoResource {

    /**
     * Contact info id path parameter.
     */
    public static final String PATH_PARAM_CONTACT_INFO_ID = "contact_info_id";

    @Context
    private UriInfo info;

    /**
     * Adds {@link ContactInfo} to {@link ContactRequest} by id.
     *
     * @param contactRequestId the contact request id
     * @param contactInfoDto the contact info DTO
     * @return response
     */
    @Operation(summary = "Create contact info")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Contact info created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact request not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response createContactInfo(
        @PathParam(ContactRequestResource.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        ContactInfoDto contactInfoDto) {
        final ContactInfoDto contactInfo
            = ContactRequestServiceImpl.getInstance().createContactInfo(contactRequestId, contactInfoDto);
        UriBuilder uriBuilder = info.getAbsolutePathBuilder();
        uriBuilder.path(contactInfo.getId().toString());
        return Response.created(uriBuilder.build()).build();
    }

    /**
     * Returns list over {@link ContactInfo} of {@link ContactRequest} by id.
     *
     * @param offset the list offset
     * @param limit the list limit
     * @param response the response
     * @param contactRequestId the contact request id
     * @return response
     */
    @Operation(summary = "Get contact infos of contact request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON,
                array = @ArraySchema(schema = @Schema(implementation = ContactInfoDto.class))), }),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact request not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContactInfoDto> getContactInfos(
        @DefaultValue("0") @QueryParam(ContactRestConstants.QUERY_PARAM_OFFSET) int offset,
        @DefaultValue("128") @QueryParam(ContactRestConstants.QUERY_PARAM_LIMIT) int limit,
        @Context HttpServletResponse response,
        @PathParam(ContactRequestResource.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId) {
        final List<ContactInfoDto> contactInfos
            = ContactRequestServiceImpl.getInstance().getContactInfosById(contactRequestId);
        response.setHeader(ContactRestConstants.HEADER_TOTAL_COUNT, Integer.toString(contactInfos.size()));
        return contactInfos.stream().skip(offset).limit(limit).toList();
    }

    /**
     * Returns {@link ContactInfo} of {@link ContactRequest} by id.
     *
     * @param contactRequestId the contact request id
     * @param contactInfoId the contact info id
     * @return response
     */
    @Operation(summary = "Get contact info by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            content = {
                @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ContactInfoDto.class)), }),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact request/info not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @GET
    @Path("/{" + PATH_PARAM_CONTACT_INFO_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public ContactInfoDto getContactInfoById(
        @PathParam(ContactRequestResource.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @PathParam(PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId) {
        return ContactInfoServiceImpl.getInstance().getContactInfoById(contactInfoId);
    }

    /**
     * Updates {@link ContactInfo} of {@link ContactRequest} by id.
     *
     * @param contactRequestId the contact request id
     * @param contactInfoId the contact info id
     * @param contactInfoDto the updated contact info DTO
     * @return response
     */
    @Operation(summary = "Update a contact info by id")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Contact info update sucessfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact request/info not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @PUT
    @Path("/{" + PATH_PARAM_CONTACT_INFO_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response updateContactInfoById(
        @PathParam(ContactRequestResource.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @PathParam(PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId, ContactInfoDto contactInfoDto) {
        if (contactInfoDto == null) {
            throw new BadRequestException();
        }
        contactInfoDto.setId(contactInfoId);
        ContactInfoServiceImpl.getInstance().updateContactInfo(contactInfoDto);
        return Response.noContent().build();
    }

    /**
     * Partial updates {@link ContactInfo} of {@link ContactRequest} by id.
     *
     * @param contactRequestId the contact request id
     * @param contactInfoId the contact info id
     * @param contactInfoDto the updated contact info DTO
     * @return response
     */
    @Operation(summary = "Partial update a contact info by id")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Contact info update sucessfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact request/info not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @PATCH
    @Path("/{" + PATH_PARAM_CONTACT_INFO_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response partialUpdateContactInfoById(
        @PathParam(ContactRequestResource.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @PathParam(PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId, ContactInfoPartialUpdateDto contactInfoDto) {
        if (contactInfoDto == null) {
            throw new BadRequestException();
        }
        ContactInfoServiceImpl.getInstance().partialUpdateContactInfo(contactInfoId, contactInfoDto);
        return Response.noContent().build();
    }

    /**
     * Deletes {@link ContactInfo} of {@link ContactRequest} by id.
     *
     * @param contactRequestId the contact request id
     * @param contactInfoId the contact info id
     * @return response
     */
    @Operation(summary = "Delete a contact by id")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Contact info deleted sucessfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact request/info does not exist",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @DELETE
    @Path("/{" + PATH_PARAM_CONTACT_INFO_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response removeContactInfoById(
        @PathParam(ContactRequestResource.PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @PathParam(PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId) {
        ContactInfoServiceImpl.getInstance().deleteContactInfoById(contactInfoId);
        return Response.noContent().build();
    }

}
