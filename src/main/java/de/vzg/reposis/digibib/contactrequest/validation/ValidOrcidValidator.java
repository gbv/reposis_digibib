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

import java.util.Optional;

import org.mycore.orcid2.validation.MCRORCIDValidationHelper;

import de.vzg.reposis.digibib.contactrequest.validation.annotation.ValidOrcid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for the {@link ValidOrcid} constraint annotation.
 * Validates if a given ORCiD string is in a valid format.
 */
public class ValidOrcidValidator implements ConstraintValidator<ValidOrcid, String> {

    @Override
    public boolean isValid(String orcid, ConstraintValidatorContext context) {
        return Optional.ofNullable(orcid).map(MCRORCIDValidationHelper::validateORCID).orElse(true);
    }
}
