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

import de.vzg.reposis.digibib.contact.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contact.mapper.ContactAttemptMapper;
import de.vzg.reposis.digibib.contact.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import de.vzg.reposis.digibib.contact.service.ContactAttemptServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

/**
 * Provides methods to manage {@link ContactAttempt} of {@link ContactInfo}.
 */
@Path("contact-tickets/{" + ContactTicketResource.PATH_PARAM_CONTACT_TICKET_ID + "}/attempts")
public class ContactAttemptResource {

    /**
     * Returns list of {@link ContactAttempt} elements from {@link ContactTicket} by its id.
     *
     * @param contactTicketId contact ticket id
     * @param contactInfoId contact info id
     * @param offset list offset
     * @param limit list limit
     * @param response response
     * @return response
     */
    @Operation(summary = "Get contact attempt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(implementation = ContactAttemptDto.class)))),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }),
        @ApiResponse(responseCode = "404", description = "Contact attempt not found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON) }), })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ContactAttemptDto> getContactAttempts(
        @PathParam(ContactTicketResource.PATH_PARAM_CONTACT_TICKET_ID) UUID contactTicketId,
        @QueryParam(ContactRestConstants.QUERY_PARAM_CONTACT_INFO_ID) UUID contactInfoId,
        @DefaultValue("0") @QueryParam(ContactRestConstants.QUERY_PARAM_OFFSET) int offset,
        @DefaultValue("128") @QueryParam(ContactRestConstants.QUERY_PARAM_LIMIT) int limit,
        @Context HttpServletResponse response) {
        List<ContactAttempt> contactAttempts = null;
        if (contactInfoId == null) {
            contactAttempts
                = ContactAttemptServiceImpl.getInstance().getContactAttemptsForContactTicket(contactTicketId);
        } else {
            contactAttempts
                = ContactAttemptServiceImpl.getInstance()
                    .getContactAttemptsForContactTicketAndContactInfo(contactTicketId, contactInfoId);
        }
        response.setHeader(ContactRestConstants.HEADER_TOTAL_COUNT, Integer.toString(contactAttempts.size()));
        return contactAttempts.stream().skip(offset).limit(limit).map(ContactAttemptMapper::toDto).toList();
    }

}
