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

import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactRequest;
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

/**
 * Provides methods to manages {@link ContactRequest}.
 */
@Path("/contact-requests")
public class ContactRequestResource {

    /**
     * Contact request id path parameter.
     */
    public static final String PATH_PARAM_CONTACT_REQUEST_ID = "contact_request_id";

    /**
     * Retrieves list over all {@link ContactRequest} elements.
     *
     * @param offset the list offset
     * @param limit the list limit
     * @param response the response
     * @return response
     */
    @Operation(summary = "Get all contact requests")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = { @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = ContactRequestDto.class))) }),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContactRequestDto> getAllContactRequests(
        @DefaultValue("0") @QueryParam(ContactRestConstants.QUERY_PARAM_OFFSET) int offset,
        @DefaultValue("128") @QueryParam(ContactRestConstants.QUERY_PARAM_LIMIT) int limit,
        @Context HttpServletResponse response) {
        final List<ContactRequestDto> contactRequestDtos
            = ContactRequestServiceImpl.getInstance().getAllContactRequests();
        response.setHeader(ContactRestConstants.HEADER_TOTAL_COUNT, Integer.toString(contactRequestDtos.size()));
        return contactRequestDtos.stream().skip(offset).limit(limit).toList();
    }

    /**
     * Retrieves {@link ContactRequest} by id.
     *
     * @param contactRequestId the contact request id
     * @return the contact request
     */
    @Path("/{" + PATH_PARAM_CONTACT_REQUEST_ID + "}")
    @Operation(summary = "Get contact request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ContactRequestDto.class)) }),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact request does not exist",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ContactRequestDto getContactRequestById(@PathParam(PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId) {
        return ContactRequestServiceImpl.getInstance().getContactRequestById(contactRequestId);
    }

    /**
     * Updates {@link ContactRequest} by id.
     *
     * @param contactRequestId the contact request id
     * @param contactRequestDto the updated contact request DTO
     * @return response
     */
    @Operation(summary = "Update contact request")
    @ApiResponses(value = { @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "400", description = "Invalid contact request",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact request does not exist",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @PUT
    @Path("/{" + PATH_PARAM_CONTACT_REQUEST_ID + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response updateRequestById(@PathParam(PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        ContactRequestDto contactRequestDto) {
        if (contactRequestDto == null) {
            throw new BadRequestException();
        }
        contactRequestDto.setId(contactRequestId);
        ContactRequestServiceImpl.getInstance().updateContactRequest(contactRequestDto);
        return Response.noContent().build();
    }

    /**
     * Partial updates {@link ContactRequest} by id.
     *
     * @param contactRequestId the contact request id
     * @param contactRequestDto the updated contact request DTO
     * @return response
     */
    @Operation(summary = "Partial update contact request")
    @ApiResponses(value = { @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "400", description = "Invalid contact request",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact request does not exist",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @PATCH
    @Path("/{" + PATH_PARAM_CONTACT_REQUEST_ID + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response partialUpdateRequestById(@PathParam(PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        ContactRequestPartialUpdateDto contactRequestDto) {
        if (contactRequestDto == null) {
            throw new BadRequestException();
        }
        ContactRequestServiceImpl.getInstance().partialUpdateContactRequest(contactRequestId, contactRequestDto);
        return Response.noContent().build();
    }

    /**
     * Removes {@link ContactRequest} by id.
     *
     * @param contactRequestId the contact request id
     * @return response
     */
    @Operation(summary = "Delete contact request")
    @ApiResponses(value = { @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact request not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @DELETE
    @Path("/{" + PATH_PARAM_CONTACT_REQUEST_ID + "}")
    @MCRRequireTransaction
    public Response removeContactRequestById(@PathParam(PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId) {
        ContactRequestServiceImpl.getInstance().deleteContactRequestById(contactRequestId);
        return Response.noContent().build();
    }

    /**
     * Forwards request of {@link ContactRequest} to {@link ContactInfo}.
     *
     * @param contactRequestId the contact request id
     * @param contactInfoId the contact info id
     * @return response
     */
    @POST
    @Path("/{" + PATH_PARAM_CONTACT_REQUEST_ID + "}/forwardRequest")
    @MCRRequireTransaction
    public Response forwardRequestBodyById(@PathParam(PATH_PARAM_CONTACT_REQUEST_ID) UUID contactRequestId,
        @QueryParam(ContactRestConstants.QUERY_PARAM_CONTACT_INFO_ID) UUID contactInfoId) {
        if (contactInfoId == null) {
            throw new BadRequestException("target contact info is required");
        }
        ContactRequestServiceImpl.getInstance().forwardContactRequestById(contactRequestId, contactInfoId);
        return Response.noContent().build();
    }
}
