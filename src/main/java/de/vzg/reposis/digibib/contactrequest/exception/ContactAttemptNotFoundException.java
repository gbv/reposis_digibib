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

package de.vzg.reposis.digibib.contactrequest.exception;

/**
 * Exception thrown when a contact attempt cannot be found.
 */
public class ContactAttemptNotFoundException extends ContactRequestException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code ContactAttemptNotFoundException} with a default message and error code.
     * <p>
     * The default message is "contact attempt not found" and the error code is "contactAttemptNotFound".
     * </p>
     */
    public ContactAttemptNotFoundException() {
        super("contact attempt not found", "contactAttemptNotFound");
    }
}
