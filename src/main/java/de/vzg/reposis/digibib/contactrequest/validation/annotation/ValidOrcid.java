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

package de.vzg.reposis.digibib.contactrequest.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.vzg.reposis.digibib.contactrequest.validation.ValidOrcidValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Annotation to validate the format of an ORCiD (Open Researcher and Contributor ID) string.
 * Uses {@link ValidOrcidValidator} as the validator to enforce the validation rules.
 */
@Constraint(validatedBy = ValidOrcidValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidOrcid {

    /**
     * Error message to be shown if the ORCiD string does not match the validation rules.
     *
     * @return The error message.
     */
    String message() default "A valid ORCiD must be specified.";

    /**
     * Specifies the validation groups to which this constraint belongs.
     *
     * @return The validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload that can be attached to a given constraint declaration.
     *
     * @return The payload.
     */
    Class<? extends Payload>[] payload() default {};
}
