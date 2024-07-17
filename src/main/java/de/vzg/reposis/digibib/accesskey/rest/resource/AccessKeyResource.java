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

package de.vzg.reposis.digibib.accesskey.rest.resource;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mycore.access.MCRAccessException;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.frontend.jersey.MCRJerseyUtil;
import org.mycore.frontend.jersey.access.MCRRequireLogin;
import org.mycore.frontend.jersey.filter.access.MCRRestrictedAccess;
import org.mycore.restapi.annotations.MCRRequireTransaction;

import de.vzg.reposis.digibib.accesskey.dto.AccessKeyDto;
import de.vzg.reposis.digibib.accesskey.dto.AccessKeyPartialUpdateDto;
import de.vzg.reposis.digibib.accesskey.service.AccessKeyServiceImpl;
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
 * A RESTful API for managing access keys. This API provides methods for creating, retrieving, updating,
 * partially updating, and deleting access keys.
 */
@MCRRestrictedAccess(MCRRequireLogin.class)
@Path("access-keys")
public class AccessKeyResource {

    private static final String PATH_PARAM_OBJECT_ID = "objectId";

    private static final String PATH_PARAM_VALUE = "value";

    private static final String QUERY_PARAM_OBJECT_ID = "object_id";

    private static final String QUERY_PARAM_OFFSET = "offset";

    private static final String QUERY_PARAM_LIMIT = "limit";

    private static final String QUERY_PARAM_VALUE_ENCODING = "value_encoding";

    private static final String HEADER_TOTAL_COUNT = "X-Total-Count";

    @Context
    UriInfo uriInfo;

    /**
     * Creates a new access key.
     *
     * @param accessKeyDto the DTO containing the details of the access key to be created
     * @return A response indicating the outcome of the create operation
     * @throws MCRAccessException if there is an access error during the creation of the access key
     */
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @MCRRequireTransaction
    public Response createAccessKey(AccessKeyDto accessKeyDto) throws MCRAccessException {
        final AccessKeyDto createdAccessKeyDto = AccessKeyServiceImpl.getInstance().createAccessKey(accessKeyDto);
        final String encodedValue = URLEncoder.encode(createdAccessKeyDto.getValue(), UTF_8);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(encodedValue).build()).build();
    }

    /**
     * Retrieves a list of access keys, with optional pagination.
     *
     * @param offset the offset of the first access key to be retrieved
     * @param limit the maximum number of access keys to be retrieved
     * @param response the response object, used to set the total count header
     * @param objectIdString the object ID to filter access keys by
     * @return a list of AccessKeyDto objects
     * @throws MCRAccessException If there is an access error during the retrieval of the access keys
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<AccessKeyDto> getAccessKeys(@DefaultValue("0") @QueryParam(QUERY_PARAM_OFFSET) int offset,
        @DefaultValue("128") @QueryParam(QUERY_PARAM_LIMIT) int limit, @Context HttpServletResponse response,
        @QueryParam(QUERY_PARAM_OBJECT_ID) String objectIdString) throws MCRAccessException {
        final MCRObjectID objectId = Optional.ofNullable(objectIdString).map(MCRJerseyUtil::getID)
            .orElseThrow(() -> new BadRequestException());
        final List<AccessKeyDto> accessKeyDtos
            = AccessKeyServiceImpl.getInstance().getAccessKeysByObjectId(objectId);
        response.setHeader(HEADER_TOTAL_COUNT, Integer.toString(accessKeyDtos.size()));
        return accessKeyDtos.stream().skip(offset).limit(limit)
            .sorted((a1, a2) -> a1.getCreated().compareTo(a2.getCreated())).toList();
    }

    /**
     * Retrieves a specific access key.
     *
     * @param objectIdString the object ID associated with the access key
     * @param value the value of the access key
     * @param valueEncoding the encoding of the value, if any
     * @return the AccessKeyDto object representing the access key
     * @throws MCRAccessException If there is an access error during the retrieval of the access key
     */
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/{" + PATH_PARAM_OBJECT_ID + "}/{" + PATH_PARAM_VALUE + "}")
    public AccessKeyDto getAccessKey(@PathParam(PATH_PARAM_OBJECT_ID) String objectIdString,
        @PathParam(PATH_PARAM_VALUE) String value, @QueryParam(QUERY_PARAM_VALUE_ENCODING) String valueEncoding)
        throws MCRAccessException {
        final MCRObjectID objectId = MCRJerseyUtil.getID(objectIdString);
        if (valueEncoding != null) {
            return AccessKeyServiceImpl.getInstance().getAccessKey(objectId, decode(value, valueEncoding));
        } else {
            return AccessKeyServiceImpl.getInstance().getAccessKey(objectId, value);
        }
    }

    /**
     * Updates an existing access key.
     *
     * @param objectIdString The object ID associated with the access key
     * @param value the value of the access key
     * @param accessKeyDto the DTO containing the updated details of the access key
     * @param valueEncoding the encoding of the value, if any
     * @return a Response indicating the outcome of the update operation
     * @throws MCRAccessException if there is an access error during the update of the access key
     */
    @MCRRequireTransaction
    @PUT
    @Path("/{" + PATH_PARAM_OBJECT_ID + "}/{" + PATH_PARAM_VALUE + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAccessKey(@PathParam(PATH_PARAM_OBJECT_ID) String objectIdString,
        @PathParam(PATH_PARAM_VALUE) String value, AccessKeyDto accessKeyDto,
        @QueryParam(QUERY_PARAM_VALUE_ENCODING) String valueEncoding) throws MCRAccessException {
        final MCRObjectID objectId = MCRJerseyUtil.getID(objectIdString);
        if (valueEncoding != null) {
            AccessKeyServiceImpl.getInstance().updateAccessKey(objectId, decode(value, valueEncoding),
                accessKeyDto);
        } else {
            AccessKeyServiceImpl.getInstance().updateAccessKey(objectId, value, accessKeyDto);
        }
        return Response.noContent().build();
    }

    /**
     * Partially updates an existing access key.
     *
     * @param objectIdString the object ID associated with the access key
     * @param value the value of the access key
     * @param accessKeyDto the data transfer object containing the partial updates for the access key
     * @param valueEncoding the encoding of the value, if any
     * @return a Response indicating the outcome of the partial update operation
     * @throws MCRAccessException if there is an access error during the partial update of the access key
     */
    @MCRRequireTransaction
    @PATCH
    @Path("/{" + PATH_PARAM_OBJECT_ID + "}/{" + PATH_PARAM_VALUE + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response partialUpdateAccessKey(@PathParam(PATH_PARAM_OBJECT_ID) String objectIdString,
        @PathParam(PATH_PARAM_VALUE) String value, AccessKeyPartialUpdateDto accessKeyDto,
        @QueryParam(QUERY_PARAM_VALUE_ENCODING) String valueEncoding) throws MCRAccessException {
        final MCRObjectID objectId = MCRJerseyUtil.getID(objectIdString);
        if (valueEncoding != null) {
            AccessKeyServiceImpl.getInstance().partialUpdateAccessKey(objectId, decode(value, valueEncoding),
                accessKeyDto);
        } else {
            AccessKeyServiceImpl.getInstance().partialUpdateAccessKey(objectId, value, accessKeyDto);
        }
        return Response.noContent().build();
    }

    /**
     * Removes an existing access key.
     *
     * @param objectIdString the object ID associated with the access key
     * @param value the value of the access key
     * @param valueEncoding the encoding of the value, if any
     * @return a Response indicating the outcome of the remove operation
     * @throws MCRAccessException if there is an access error during the removal of the access key
     */
    @MCRRequireTransaction
    @DELETE
    @Path("/{" + PATH_PARAM_OBJECT_ID + "}/{" + PATH_PARAM_VALUE + "}")
    public Response removeAccessKey(@PathParam(PATH_PARAM_OBJECT_ID) String objectIdString,
        @PathParam(PATH_PARAM_VALUE) String value, @QueryParam(QUERY_PARAM_VALUE_ENCODING) String valueEncoding)
        throws MCRAccessException {
        final MCRObjectID objectId = MCRJerseyUtil.getID(objectIdString);
        if (valueEncoding != null) {
            AccessKeyServiceImpl.getInstance().removeAccessKey(objectId, decode(value, valueEncoding));
        } else {
            AccessKeyServiceImpl.getInstance().removeAccessKey(objectId, value);
        }
        return Response.noContent().build();
    }

    private static String decode(String text, String encoding) {
        if (Objects.equals(encoding, "base64url")) {
            return new String(Base64.getUrlDecoder().decode(text.getBytes(UTF_8)), UTF_8);
        }
        throw new IllegalArgumentException("Cannot decode, unknown encoding");
    }

}
