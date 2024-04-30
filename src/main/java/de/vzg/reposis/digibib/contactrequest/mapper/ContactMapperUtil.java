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

package de.vzg.reposis.digibib.contactrequest.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Utility class for mapping stuff.
 */
public class ContactMapperUtil {

    /**
     * Converts a LocalDateTime to a Date.
     *
     * @param localDateTime the LocalDateTime to convert
     * @return the corresponding Date object
     */
    public static Date localDateToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts a Date to a LocalDateTime.
     *
     * @param date the Date to convert
     * @return the corresponding LocalDateTime object
     */
    public static LocalDateTime dateToLocalDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
