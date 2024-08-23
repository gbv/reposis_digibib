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

package de.vzg.reposis.digibib.contactrequest.validation;

import de.vzg.reposis.digibib.contactrequest.dto.util.Nullable;
import de.vzg.reposis.digibib.contactrequest.validation.annotation.IfPresentNotNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link IfPresentNotNull} constraint annotation.
 * <p>
 * This validator ensures that if a value is present, it must not be null.
 * </p>
 */
public class IfPresentNotNullValidator implements ConstraintValidator<IfPresentNotNull, Nullable<?>> {

    /**
     * Checks if the given value is valid.
     * <p>
     * If the value is not present, it is considered valid. If the value is present,
     * it must not be null.
     * </p>
     *
     * @param value   the value to validate, wrapped in a {@code Nullable<?>}
     * @param context context in which the constraint is evaluated
     * @return {@code true} if the value is valid, {@code false} otherwise
     */
    @Override
    public boolean isValid(Nullable<?> value, ConstraintValidatorContext context) {
        if (!value.isPresent()) {
            return true;
        }
        return value.get() != null;
    }
}
