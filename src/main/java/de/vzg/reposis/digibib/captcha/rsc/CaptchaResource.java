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

package de.vzg.reposis.digibib.captcha.rsc;

import java.util.Objects;

import de.vzg.reposis.digibib.captcha.CaptchaConstants;
import de.vzg.reposis.digibib.captcha.CaptchaService;
import de.vzg.reposis.digibib.captcha.CaptchaServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * CAPTCHA resource.
 */
@Path("/captcha")
public class CaptchaResource {

    @Context
    private HttpServletRequest httpRequest;

    /**
     * Generates and sets CAPTCHA text to current {@link HttpSession} and returns it as image.
     *
     * @param response response context
     * @return CAPTCHA image
     */
    @GET
    @Path("/generate")
    public byte[] generateCaptcha(@Context HttpServletResponse response) {
        final CaptchaService captchaService = CaptchaServiceFactory.getDefaultInstance();
        final String captchaText = captchaService.generateCaptchaText();
        final byte[] captchaImage = captchaService.generateCaptchaImage(captchaText);
        httpRequest.getSession().setAttribute(CaptchaConstants.CAPTCHA_SESSION_ATTRIBUTE, captchaText);
        setResponseHeaders(response);
        return captchaImage;
    }

    /**
     * Validates CAPTCHA text in current {@link HttpSession}.
     *
     * @param captchaText the CAPTCHA text
     * @return response
     */
    @POST
    @Path("/validate")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response validateCaptcha(String captchaText) {
        final HttpSession session = httpRequest.getSession();
        final String storedCaptcha = (String) session.getAttribute(CaptchaConstants.CAPTCHA_SESSION_ATTRIBUTE);
        if (storedCaptcha != null) {
            session.removeAttribute(CaptchaConstants.CAPTCHA_SESSION_ATTRIBUTE);
            if (Objects.equals(storedCaptcha, captchaText)) {
                session.setAttribute(CaptchaConstants.CAPTCHA_VALID_SESSION_ATTRIBUTE, true);
                return Response.ok().build();
            }
        }
        throw new BadRequestException();
    }

    private void setResponseHeaders(HttpServletResponse response) {
        response.addHeader("Content-Type", "image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        final long time = System.currentTimeMillis();
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
    }

}
