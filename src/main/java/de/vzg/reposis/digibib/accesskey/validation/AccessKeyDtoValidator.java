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

package de.vzg.reposis.digibib.accesskey.validation;

import java.util.Set;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import de.vzg.reposis.digibib.accesskey.dto.AccessKeyDto;
import de.vzg.reposis.digibib.accesskey.exception.AccessKeyValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Utility class for validating {@link AccessKeyDto} objects.
 * 
 * The {@code AccessKeyValidator} class provides a method to validate access key DTOs using
 * the Bean Validation API. If validation fails, an {@link AccessKeyValidationException} is thrown.
 */
public class AccessKeyDtoValidator {

    private static Validator validator = Validation.byDefaultProvider().configure()
        .messageInterpolator(new ParameterMessageInterpolator())
        .buildValidatorFactory().getValidator();

    /**
     * Validates the given {@link AccessKeyDto}.
     *
     * @param accessKey the access key DTO to be validated
     * @throws AccessKeyValidationException if the access key DTO is invalid
     */
    public static void validate(AccessKeyDto accessKey) {
        Set<ConstraintViolation<AccessKeyDto>> violations = validator.validate(accessKey);
        if (!violations.isEmpty()) {
            throw new AccessKeyValidationException(violations.iterator().next().getMessage());
        }
    }

}
