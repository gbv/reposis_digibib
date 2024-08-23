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

import com.github.cage.GCage;

import de.vzg.reposis.digibib.captcha.cage.CageCaptchaService;

/**
 * Factory class for creating and managing the default instance of {@link CaptchaService}.
 */
public class CaptchaServiceFactory {

    private static volatile CaptchaService defaultService = null;

    private CaptchaServiceFactory() {

    }

    /**
     * Returns the default instance of {@link CaptchaService}.
     *
     * @return the default CAPTCHA service instance
     */
    public static CaptchaService getDefaultInstance() {
        if (defaultService == null) {
            synchronized (CaptchaServiceFactory.class) {
                if (defaultService == null) {
                    defaultService = new CageCaptchaService(new GCage());
                }
            }
        }
        return defaultService;
    }

}
