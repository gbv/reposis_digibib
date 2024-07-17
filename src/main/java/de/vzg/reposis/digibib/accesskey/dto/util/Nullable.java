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

package de.vzg.reposis.digibib.accesskey.dto.util;

import java.util.Objects;

/**
 * A wrapper class that represents a value that may or may not be present.
 * This class is useful for differentiating between the absence of a value and the presence of a null value.
 *
 * @param <T> The type of the wrapped value.
 */
public class Nullable<T> {

    private final T value;
    private final boolean isPresent;

    /**
     * Constructs a new {@link Nullable} instance with the given value.
     * The {@code isPresent} flag is set to {@code true}, indicating that a value is present.
     *
     * @param value The value to be wrapped. This may be {@code null}.
     */
    public Nullable(T value) {
        this.value = value;
        this.isPresent = true;
    }

    /**
     * Constructs a new {@link Nullable}  instance with no value.
     * The {@code isPresent} flag is set to {@code false}, indicating that no value is present.
     */
    public Nullable() {
        this.value = null;
        this.isPresent = false;
    }

    /**
     * Returns the wrapped value.
     * If no value is present, this method returns {@code null}.
     *
     * @return The wrapped value, or {@code null} if no value is present.
     */
    public T get() {
        return value;
    }

    /**
     * Indicates whether a value is present.
     *
     * @return {@code true} if a value is present, {@code false} otherwise.
     */
    public boolean isPresent() {
        return isPresent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPresent, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (obj instanceof Nullable<?>) {
            Nullable<?> other = (Nullable<?>) obj;
            return isPresent == other.isPresent && Objects.equals(value, other.value);
        }
        return false;
    }
}
