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

package de.vzg.reposis.digibib.contact.restapi.v2;

import java.util.List;
import java.util.UUID;

import org.mycore.restapi.annotations.MCRRequireTransaction;

import de.vzg.reposis.digibib.contact.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contact.mapper.ContactInfoMapper;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import de.vzg.reposis.digibib.contact.service.ContactInfoServiceImpl;
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
 * Provides methods to manage {@link ContactInfo} of {@link ContactTicket}.
 */
@Path("contact-tickets/{" + ContactTicketResource.PATH_PARAM_CONTACT_TICKET_ID + "}/contacts")
public class ContactInfoResource {

    /**
     * Contact info id path parameter.
     */
    public static final String PATH_PARAM_CONTACT_INFO_ID = "contact_info_id";

    @Context
    private UriInfo info;

    /**
     * Adds {@link ContactInfo} to {@link ContactTicket} by id.
     *
     * @param contactTicketId the contact ticket id
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
        @ApiResponse(responseCode = "404", description = "Contact ticket not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response createContactInfo(
        @PathParam(ContactTicketResource.PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId,
        ContactInfoDto contactInfoDto) {
        final ContactInfo contactInfo
            = ContactInfoServiceImpl.getInstance().createContactInfo(contactTicketId, contactInfoDto);
        UriBuilder uriBuilder = info.getAbsolutePathBuilder();
        uriBuilder.path(contactInfo.getId().toString());
        return Response.created(uriBuilder.build()).build();
    }

    /**
     * Returns list over {@link ContactInfo} of {@link ContactTicket} by id.
     *
     * @param offset list offset
     * @param limit list limit
     * @param response response
     * @param ticketId ticket id
     * @return response
     */
    @Operation(summary = "Get contact infos of ticket")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON,
                array = @ArraySchema(schema = @Schema(implementation = ContactInfoDto.class))), }),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Ticket not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContactInfoDto> getContactInfos(
        @DefaultValue("0") @QueryParam(ContactRestConstants.QUERY_PARAM_OFFSET) int offset,
        @DefaultValue("128") @QueryParam(ContactRestConstants.QUERY_PARAM_LIMIT) int limit,
        @Context HttpServletResponse response,
        @PathParam(ContactTicketResource.PATH_PARAM_CONTACT_TICKET_ID) UUID ticketId) {
        final List<ContactInfo> contactInfos
            = ContactInfoServiceImpl.getInstance().getContactInfosWithChildrenForContactTicket(ticketId);
        response.setHeader(ContactRestConstants.HEADER_TOTAL_COUNT, Integer.toString(contactInfos.size()));
        return contactInfos.stream().skip(offset).limit(limit).map(ContactInfoMapper::toDto).toList();
    }

    /**
     * Returns {@link ContactInfo} of {@link ContactTicket} by id.
     *
     * @param contactTicketId the contact ticket id
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
        @ApiResponse(responseCode = "404", description = "Contact info not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @GET
    @Path("/{" + PATH_PARAM_CONTACT_INFO_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public ContactInfoDto getContactInfo(
        @PathParam(ContactTicketResource.PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId,
        @PathParam(PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId) {
        return ContactInfoMapper.toDto(ContactInfoServiceImpl.getInstance().getContactInfoById(contactInfoId));
    }

    /**
     * Updates {@link ContactInfo} of {@link ContactTicket} by id.
     *
     * @param contactTicketId the contact ticket id
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
        @ApiResponse(responseCode = "404", description = "Contact info not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @PUT
    @Path("/{" + PATH_PARAM_CONTACT_INFO_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response updateContactInfo(
        @PathParam(ContactTicketResource.PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId,
        @PathParam(PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId, ContactInfoDto contactInfoDto) {
        if (contactInfoDto == null) {
            throw new BadRequestException();
        }
        contactInfoDto.setId(contactInfoId);
        ContactInfoServiceImpl.getInstance().updateContactInfo(contactInfoDto);
        return Response.noContent().build();
    }

    /**
     * Partial updates {@link ContactInfo} of {@link ContactTicket} by id.
     *
     * @param contactTicketId the contact ticket id
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
        @ApiResponse(responseCode = "404", description = "Contact info not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @PATCH
    @Path("/{" + PATH_PARAM_CONTACT_INFO_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response partialUpdateContactInfo(
        @PathParam(ContactTicketResource.PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId,
        @PathParam(PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId, ContactInfoDto contactInfoDto) {
        if (contactInfoDto == null) {
            throw new BadRequestException();
        }
        contactInfoDto.setId(contactInfoId);
        ContactInfoServiceImpl.getInstance().partialUpdateContactInfo(contactInfoDto);
        return Response.noContent().build();
    }

    /**
     * Deletes {@link ContactInfo} of {@link ContactTicket} by id.
     *
     * @param contactTicketId the contact ticket id
     * @param contactInfoId the contact info id
     * @return response
     */
    @Operation(summary = "Delete a contact by id")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Contact info deleted sucessfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact info does not exist",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @DELETE
    @Path("/{" + PATH_PARAM_CONTACT_INFO_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response removeContactInfo(
        @PathParam(ContactTicketResource.PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId,
        @PathParam(PATH_PARAM_CONTACT_INFO_ID) UUID contactInfoId) {
        ContactInfoServiceImpl.getInstance().deleteContactInfo(contactInfoId);
        return Response.noContent().build();
    }

}
