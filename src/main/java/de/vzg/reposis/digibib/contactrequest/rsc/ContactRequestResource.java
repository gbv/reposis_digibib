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

package de.vzg.reposis.digibib.contactrequest.rsc;

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

import de.vzg.reposis.digibib.captcha.rsc.filter.CheckValidCaptcha;
import de.vzg.reposis.digibib.contactrequest.ContactRequestConstants;
import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestBodyDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestSummaryDto;
import de.vzg.reposis.digibib.contactrequest.dto.util.Nullable;
import de.vzg.reposis.digibib.contactrequest.exception.ContactAttemptNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestNotFoundException;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestValidationException;
import de.vzg.reposis.digibib.contactrequest.service.ContactRequestFactory;
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
 * Provides methods to handle {@link ContactRequestBodyDto}.
 */
@Path("/contact-request")
public class ContactRequestResource {

    private static final String QUERY_PARAM_OBJECT_ID = "objId";

    private static final String QUERY_PARAM_REQUEST_ID = "rid";

    private static final String QUERY_PARAM_ATTEMPT_ID = "aid";

    private static final Set<String> ALLOWED_GENRES
        = MCRConfiguration2.getString(ContactRequestConstants.CONF_PREFIX + "RequestForm.EnabledGenres").stream()
            .flatMap(MCRConfiguration2::splitValue).collect(Collectors.toSet());

    /**
     * Creates a new contact request based on the provided {@link ContactRequestBodyDto} and object ID.
     *
     * @param objectIdString the object ID as a string
     * @param contactRequestBodyDto the DTO containing the details for the new contact request
     * @return a response indicating the result of the operation
     * @throws BadRequestException if the genre is not enabled or if there are validation issues with the request
     */
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MCRRequireTransaction
    @CheckValidCaptcha
    public Response createContactRequest(@QueryParam(QUERY_PARAM_OBJECT_ID) String objectIdString,
        ContactRequestBodyDto contactRequestBodyDto) {
        final MCRObjectID objectId = MCRJerseyUtil.getID(objectIdString);
        ensureGenreIsEnabled(objectId);
        final ContactRequestDto contactRequestDto = new ContactRequestDto();
        contactRequestDto.setObjectId(objectId);
        contactRequestDto.setBody(contactRequestBodyDto);
        try {
            ContactRequestFactory.getContactRequestService().createContactRequest(contactRequestDto);
        } catch (ContactRequestValidationException e) {
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
     * Confirms a the receive of a contact attempt based on the provided contact attempt ID.
     *
     * @param contactAttemptId the ID of the contact attempt to confirm
     * @return a response indicating the result of the confirmation
     * @throws BadRequestException if the contact attempt ID is null or the contact attempt is not found
     */
    @POST
    @MCRRequireTransaction
    @Path("confirmAttempt")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confirmContactAttempt(@QueryParam(QUERY_PARAM_ATTEMPT_ID) UUID contactAttemptId) {
        if (contactAttemptId == null) {
            throw new BadRequestException();
        }
        final ContactAttemptPartialUpdateDto contactAttemptDto = new ContactAttemptPartialUpdateDto();
        contactAttemptDto.setSuccessDate(new Nullable<Date>(new Date()));
        try {
            ContactRequestFactory.getContactAttemptService().partialUpdateContactAttempt(contactAttemptId,
                contactAttemptDto);
        } catch (ContactAttemptNotFoundException e) {
            throw new BadRequestException();
        }
        return Response.ok().build();
    }

    /**
     * Retrieves the status of a contact request based on the provided contact request ID.
     *
     * @param contactRequestId the ID of the contact request to retrieve status for
     * @return a summary DTO containing the status summary of the contact request
     * @throws BadRequestException if the contact request ID is null or the contact request is not found
     */
    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    public ContactRequestSummaryDto getRequestStatus(@QueryParam(QUERY_PARAM_REQUEST_ID) UUID contactRequestId) {
        if (contactRequestId == null) {
            throw new BadRequestException();
        }
        try {
            final ContactRequestSummaryDto contactRequestSummaryDto
                = ContactRequestFactory.getContactRequestService().getStatusSummaryById(contactRequestId);
            final List<String> emails
                = contactRequestSummaryDto.getEmails().stream().map(ContactRequestResource::maskEmailAddress).toList();
            contactRequestSummaryDto.setEmails(emails);
            return contactRequestSummaryDto;
        } catch (ContactRequestNotFoundException e) {
            throw new BadRequestException();
        }
    }

    // https://stackoverflow.com/questions/43003138/regular-expression-for-email-masking
    private static String maskEmailAddress(String email) {
        return email.replaceAll("(?<=.)[^@](?=[^@]*[^@]@)|(?:(?<=@.)|(?!^)\\G(?=[^@]*$)).(?!$)", "*");
    }

}
