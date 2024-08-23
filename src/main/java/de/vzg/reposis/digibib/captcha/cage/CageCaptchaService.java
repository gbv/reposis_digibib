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

package de.vzg.reposis.digibib.captcha.cage;

import com.github.cage.Cage;

import de.vzg.reposis.digibib.captcha.CaptchaService;

/**
 * This class implements a CAPTCHA service and uses Cage.
 */
public class CageCaptchaService implements CaptchaService {

    private final Cage cage;

    /**
     * Constructs a new {@code CageCaptchaService} with the specified {@link Cage} instance.
     *
     * @param cage the cage instance used to generate CAPTCHA images
     */
    public CageCaptchaService(Cage cage) {
        this.cage = cage;
    }

    @Override
    public String generateCaptchaText() {
        return cage.getTokenGenerator().next();
    }

    @Override
    public byte[] generateCaptchaImage(String captchaText) {
        return cage.draw(captchaText);
    }

}
