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

import de.vzg.reposis.digibib.contact.dto.ContactTicketDto;
import de.vzg.reposis.digibib.contact.mapper.ContactTicketMapper;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import de.vzg.reposis.digibib.contact.service.ContactTicketServiceImpl;
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
 * Provides methods to manages {@link ContactTicket}.
 */
@Path("/contact-tickets")
public class ContactTicketResource {

    /**
     * Contact ticket id path parameter.
     */
    public static final String PATH_PARAM_CONTACT_TICKET_ID = "contact_ticket_id";

    /**
     * Retrieves list over all {@link ContactTicket} elements.
     *
     * @param offset list offset
     * @param limit list limit
     * @param response response
     * @return response
     */
    @Operation(summary = "Get all contact tickets")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = { @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = ContactTicketDto.class))) }),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContactTicketDto> getAllContactTickets(
        @DefaultValue("0") @QueryParam(ContactRestConstants.QUERY_PARAM_OFFSET) int offset,
        @DefaultValue("128") @QueryParam(ContactRestConstants.QUERY_PARAM_LIMIT) int limit,
        @Context HttpServletResponse response) {
        final List<ContactTicket> contactTickets = ContactTicketServiceImpl.getInstance().getAllContactTickets();
        response.setHeader(ContactRestConstants.HEADER_TOTAL_COUNT, Integer.toString(contactTickets.size()));
        return contactTickets.stream().skip(offset).limit(limit).map(ContactTicketMapper::toDto).toList();
    }

    /**
     * Retrieves {@link ContactTicket} by id.
     *
     * @param contactTicketId the contact ticket id
     * @return the contact ticket
     */
    @Path("/{" + PATH_PARAM_CONTACT_TICKET_ID + "}")
    @Operation(summary = "Get contact ticket")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ContactTicketDto.class)) }),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Ticket does not exist",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ContactTicketDto getContactTicket(@PathParam(PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId) {
        return ContactTicketMapper.toDto(ContactTicketServiceImpl.getInstance().getContactTicketById(contactTicketId));
    }

    /**
     * Updates {@link ContactTicket} by id.
     *
     * @param contactTicketId the contact ticket id
     * @param contactTicketDto the updated contact ticket DTO
     * @return response
     */
    @Operation(summary = "Update contact ticket")
    @ApiResponses(value = { @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "400", description = "Invalid contact ticket",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact ticket does not exist",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @PUT
    @Path("/{" + PATH_PARAM_CONTACT_TICKET_ID + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response updateTicket(@PathParam(PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId,
        ContactTicketDto contactTicketDto) {
        if (contactTicketDto == null) {
            throw new BadRequestException();
        }
        contactTicketDto.setId(contactTicketId);
        ContactTicketServiceImpl.getInstance().updateContactTicket(contactTicketDto);
        return Response.noContent().build();
    }

    /**
     * Partial updates {@link ContactTicket} by id.
     *
     * @param contactTicketId the contact ticket id
     * @param contactTicketDto the updated contact ticket DTO
     * @return response
     */
    @Operation(summary = "Partial update contact ticket")
    @ApiResponses(value = { @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "400", description = "Invalid contact ticket",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact ticket does not exist",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @PATCH
    @Path("/{" + PATH_PARAM_CONTACT_TICKET_ID + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    public Response partialUpdateTicket(@PathParam(PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId,
        ContactTicketDto contactTicketDto) {
        if (contactTicketDto == null) {
            throw new BadRequestException();
        }
        contactTicketDto.setId(contactTicketId);
        ContactTicketServiceImpl.getInstance().partialUpdateContactTicket(contactTicketDto);
        return Response.noContent().build();
    }

    /**
     * Removes {@link ContactTicket} by id.
     *
     * @param contactTicketId the contact ticket id
     * @return response
     */
    @Operation(summary = "Delete contact ticket")
    @ApiResponses(value = { @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact ticket not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @DELETE
    @Path("/{" + PATH_PARAM_CONTACT_TICKET_ID + "}")
    @MCRRequireTransaction
    public Response
        removeContactTicket(@PathParam(PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId) {
        ContactTicketServiceImpl.getInstance().deleteTicket(contactTicketId);
        return Response.noContent().build();
    }

    /**
     * Closes {@link ContactTicket} by id.
     *
     * @param contactTicketId the contact ticket id
     * @return response
     */
    @POST
    @Path("/{" + PATH_PARAM_CONTACT_TICKET_ID + "}/close")
    @MCRRequireTransaction
    public Response closeTicket(@QueryParam(PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId) {
        ContactTicketServiceImpl.getInstance().closeTicket(contactTicketId);
        return Response.noContent().build();
    }

    /**
     * Forwards request of {@link ContactTicket} to {@link ContactInfo}.
     *
     * @param contactTicketId the contact ticket id
     * @param contactInfoId the contact info id
     * @return response
     */
    @POST
    @Path("/{" + PATH_PARAM_CONTACT_TICKET_ID + "}/forwardRequest")
    @MCRRequireTransaction
    public Response forwardRequest(@PathParam(PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId,
        @QueryParam(ContactRestConstants.QUERY_PARAM_CONTACT_INFO_ID) UUID contactInfoId) {
        if (contactInfoId == null) {
            throw new BadRequestException("target contact info is required");
        }
        ContactTicketServiceImpl.getInstance().forwardContactRequest(contactTicketId, contactInfoId);
        return Response.noContent().build();
    }
}
