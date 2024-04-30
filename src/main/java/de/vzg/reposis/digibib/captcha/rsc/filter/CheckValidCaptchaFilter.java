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

package de.vzg.reposis.digibib.captcha.rsc.filter;

import java.util.Objects;

import de.vzg.reposis.digibib.captcha.CaptchaConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;

/**
 * Filter to check the validity of CAPTCHA before processing the request.
 * <p>
 * This filter is applied to resource methods annotated with {@link CheckValidCaptcha}.
 * It ensures that the CAPTCHA validation has been passed before allowing access to the resource.
 * </p>
 * <p>
 * If the CAPTCHA validation has not been passed, a {@link BadRequestException} is thrown.
 * After the check, the CAPTCHA validation attribute is removed from the session.
 * </p>
 */
@Provider
@CheckValidCaptcha
public class CheckValidCaptchaFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest httpRequest;

    /**
     * Filters incoming requests to check for CAPTCHA validation.
     * <p>
     * Retrieves the CAPTCHA validation attribute from the HTTP session and checks its validity.
     * If the CAPTCHA is not valid, a {@link BadRequestException} is thrown.
     * After the check, the CAPTCHA validation attribute is removed from the session.
     * </p>
     *
     * @param requestContext the request context
     * @throws BadRequestException if the CAPTCHA validation fails
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        final HttpSession session = httpRequest.getSession();
        final Boolean captchaValid = (Boolean) session.getAttribute(CaptchaConstants.CAPTCHA_VALID_SESSION_ATTRIBUTE);
        if (!Objects.equals(true, captchaValid)) {
            throw new BadRequestException();
        }
        session.removeAttribute(CaptchaConstants.CAPTCHA_VALID_SESSION_ATTRIBUTE);
    }
}
