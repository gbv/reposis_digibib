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

package de.vzg.reposis.digibib.contact.rsc;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jdom2.Attribute;
import org.mycore.common.MCRException;
import org.mycore.common.config.MCRConfiguration2;
import org.mycore.datamodel.metadata.MCRMetadataManager;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.frontend.jersey.MCRJerseyUtil;
import org.mycore.mods.MCRMODSWrapper;
import org.mycore.restapi.annotations.MCRRequireTransaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.captcha.cage.rsc.filter.ContactCheckCageCaptcha;
import de.vzg.reposis.digibib.contact.ContactConstants;
import de.vzg.reposis.digibib.contact.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contact.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contact.dto.ContactTicketDto;
import de.vzg.reposis.digibib.contact.exception.ContactAttemptNotFoundException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketInvalidException;
import de.vzg.reposis.digibib.contact.exception.ContactTicketNotFoundException;
import de.vzg.reposis.digibib.contact.persistence.model.ContactAttempt;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactRequest;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import de.vzg.reposis.digibib.contact.service.ContactAttemptServiceImpl;
import de.vzg.reposis.digibib.contact.service.ContactTicketServiceImpl;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Provides methods to handle {@link ContactRequest}.
 */
@Path("/contact-request")
public class ContactResource {

    private static final String QUERY_PARAM_OBJECT_ID = "objId";

    private static final String QUERY_PARAM_REQUEST_ID = "rid";

    private static final String QUERY_PARAM_ATTEMPT_ID = "aid";

    private static final Set<String> ALLOWED_GENRES
        = MCRConfiguration2.getString(ContactConstants.CONF_PREFIX + "RequestForm.EnabledGenres").stream()
            .flatMap(MCRConfiguration2::splitValue).collect(Collectors.toSet());

    /**
     * Creates new {@link ContactTicket} by {@link ContactRequestDto} for object id.
     *
     * @param objectIdString the object id
     * @param contactRequestDto the contact request DTO
     * @return response
     */
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    @ContactCheckCageCaptcha
    public Response createContactTicket(@QueryParam(QUERY_PARAM_OBJECT_ID) String objectIdString,
        ContactRequestDto contactRequestDto) {
        final MCRObjectID objectId = MCRJerseyUtil.getID(objectIdString);
        ensureGenreIsEnabled(objectId);
        final ContactTicketDto contactTicketDto = new ContactTicketDto();
        contactTicketDto.setObjectId(objectId);
        contactTicketDto.setContactRequest(contactRequestDto);
        try {
            ContactTicketServiceImpl.getInstance().createContactTicket(contactTicketDto);
        } catch (ContactTicketInvalidException e) {
            throw new BadRequestException("Invalid request");
        }
        return Response.ok().build();
    }

    private void ensureGenreIsEnabled(MCRObjectID objectId) {
        String genre = null;
        try {
            genre = getGenre(objectId);
        } catch (MCRException e) {
            throw new BadRequestException("No genre for: " + objectId.toString());
        }
        if (!ALLOWED_GENRES.contains(genre)) {
            throw new BadRequestException("Not activated for genre: " + genre);
        }
    }

    private static String getGenre(MCRObjectID objectId) {
        return Optional.of(MCRMetadataManager.retrieveMCRObject(objectId)).map(o -> new MCRMODSWrapper(o))
            .map(w -> w.getElement("mods:genre")).map(e -> e.getAttribute("valueURI"))
            .map(Attribute::getValue).map(v -> v.substring(v.lastIndexOf("#") + 1))
            .orElseThrow(() -> new MCRException("Object has no genre."));
    }

    /**
     * Confirms {@link ContactAttempt}.
     *
     * @param contactAttemptId the contact attempt id
     * @return response
     */
    @POST
    @MCRRequireTransaction
    @Path("confirmReceipt")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confirmContactAttempt(@QueryParam(QUERY_PARAM_ATTEMPT_ID) UUID contactAttemptId) {
        if (contactAttemptId == null) {
            throw new BadRequestException();
        }
        final ContactAttemptDto contactAttemptDto = new ContactAttemptDto();
        contactAttemptDto.setId(contactAttemptId);
        contactAttemptDto.setSuccessDate(new Date());
        try {
            ContactAttemptServiceImpl.getInstance().partialUpdateContactAttempt(contactAttemptDto);
        } catch (ContactAttemptNotFoundException e) {
            throw new BadRequestException();
        }
        return Response.ok().build();
    }

    /**
     * Returns status of {@link ContactTicket}.
     *
     * @param contactTicketId contact ticket id
     * @return response
     */
    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    public ContactRequestStatusDto getTicketStatus(@QueryParam(QUERY_PARAM_REQUEST_ID) UUID contactTicketId) {
        if (contactTicketId == null) {
            throw new BadRequestException();
        }
        try {
            final ContactTicket contactTicket
                = ContactTicketServiceImpl.getInstance().getContactTicketById(contactTicketId);
            return toContactTicketStatus(contactTicket);
        } catch (ContactTicketNotFoundException e) {
            throw new BadRequestException();
        }
    }

    private ContactRequestStatusDto toContactTicketStatus(ContactTicket contactTicket) {
        final List<String> emails
            = contactTicket.getContactInfos().stream().filter(c -> c.getContactAttempts().size() > 0)
                .map(ContactInfo::getEmail).distinct().map(ContactResource::maskEmailAddress).toList();
        final String status = contactTicket.getStatus().toString().toLowerCase();
        return new ContactRequestStatusDto(status, emails);
    }

    // https://stackoverflow.com/questions/43003138/regular-expression-for-email-masking
    private static String maskEmailAddress(String email) {
        return email.replaceAll("(?<=.)[^@](?=[^@]*[^@]@)|(?:(?<=@.)|(?!^)\\G(?=[^@]*$)).(?!$)", "*");
    }

    private record ContactRequestStatusDto(@JsonProperty("status") String status,
        @JsonProperty("emails") List<String> emails) {
    }

}
