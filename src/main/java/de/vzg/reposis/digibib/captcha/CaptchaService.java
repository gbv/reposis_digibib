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

package de.vzg.reposis.digibib.captcha;

/**
 * Service interface for generating and managing CAPTCHA images and texts.
 */
public interface CaptchaService {

    /**
     * Generates a random CAPTCHA text.
     *
     * @return a randomly generated CAPTCHA text
     */
    String generateCaptchaText();

    /**
     * Generates a CAPTCHA image for the provided text.
     *
     * @param captchaText the text to be included in the CAPTCHA image
     * @return a byte array representing the generated CAPTCHA image
     */
    byte[] generateCaptchaImage(String captchaText);
}
