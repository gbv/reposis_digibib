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

package de.vzg.reposis.digibib.captcha.cage.rsc;

import java.io.IOException;
import java.io.OutputStream;

import org.mycore.common.MCRException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.cage.Cage;

import de.vzg.reposis.digibib.captcha.cage.CageCaptchaServiceImpl;
import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.StreamingOutput;

/**
 * Cage captcha resource.
 */
@Singleton
@Path("/captchaCage")
public class CageCaptchaResource {

    private static final CageCaptchaServiceImpl CAPTCHA_SERVICE = CageCaptchaServiceImpl.getInstance();

    /**
     * Generates and returns captcha.
     *
     * @param resp response context
     * @return response
     */
    @GET
    @Produces("image/" + Cage.DEFAULT_FORMAT)
    public StreamingOutput getCaptcha(@Context HttpServletResponse resp) {
        final String challenge = CAPTCHA_SERVICE.createChallenge();
        setResponseHeaders(resp);
        return new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                CAPTCHA_SERVICE.getCage().draw(challenge, os);
            }
        };
    }

    /**
     * Verifies secret and returns {@link TokenResponse} if valid.
     *
     * @param secret secret
     * @return token response
     */
    @POST
    @Path("/userverify")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public TokenResponse verifySecret(String secret) {
        String encryptedTokenString = null;
        try {
            encryptedTokenString = CAPTCHA_SERVICE.verifyAndCreateToken(secret);
        } catch (MCRException e) {
            throw new BadRequestException();
        }
        return new TokenResponse(encryptedTokenString, true);
    }

    private void setResponseHeaders(HttpServletResponse resp) {
        resp.setHeader("Cache-Control", "no-cache, no-store");
        resp.setHeader("Pragma", "no-cache");
        final long time = System.currentTimeMillis();
        resp.setDateHeader("Last-Modified", time);
        resp.setDateHeader("Date", time);
        resp.setDateHeader("Expires", time);
    }

    private static record TokenResponse(@JsonProperty("verifiedToken") String verifiedTokenString,
        @JsonProperty("verified") boolean verified) {
    }

}
