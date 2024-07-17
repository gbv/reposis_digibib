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

package de.vzg.reposis.digibib.accesskey.dto;

import java.util.Date;
import java.util.Objects;

import org.mycore.datamodel.metadata.MCRObjectID;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.accesskey.model.PermissionType;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for access key.
 *
 * The {@code AccessKeyDto} class represents the data structure used to transfer access key information.
 */
public class AccessKeyDto {

    private String value;

    private MCRObjectID objectId;

    private PermissionType permission;

    private String comment;

    private Boolean active;

    private Date expiration;

    private Date created;

    private String createdBy;

    private Date lastModified;

    private String lastModifiedBy;

    /**
     * Gets the value of the access key.
     *
     * @return the value of the access key
     */
    @JsonProperty(value = "value")
    @NotNull
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the access key.
     *
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the object ID associated with the access key.
     *
     * @return the object ID
     */
    @JsonProperty(value = "objectId")
    @NotNull
    public MCRObjectID getObjectId() {
        return objectId;
    }

    /**
     * Sets the object ID associated with the access key.
     *
     * @param objectId the object ID to set
     */
    public void setObjectId(MCRObjectID objectId) {
        this.objectId = objectId;
    }

    /**
     * Gets the permission of the access key.
     *
     * @return the permission of the access key
     */
    @JsonProperty(value = "permission")
    @NotNull
    public PermissionType getPermission() {
        return permission;
    }

    /**
     * Sets the permission of the access key.
     *
     * @param permission the permission to set
     */
    public void setPermission(PermissionType permission) {
        this.permission = permission;
    }

    /**
     * Gets the comment associated with the access key.
     *
     * @return the comment
     */
    @JsonProperty(value = "comment")
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment associated with the access key.
     *
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the active status of the access key.
     *
     * @return the active status
     */
    @JsonProperty(value = "active")
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the active status of the access key.
     *
     * @param active the active status to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Gets the expiration date of the access key.
     *
     * @return the expiration date
     */
    @JsonProperty(value = "expiration")
    public Date getExpiration() {
        return expiration;
    }

    /**
     * Sets the expiration date of the access key.
     *
     * @param expiration the expiration date to set
     */
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    /**
     * Gets the creation date of the access key.
     *
     * @return the creation date
     */
    @JsonProperty(value = "created")
    public Date getCreated() {
        return created;
    }

    /**
     * Sets the creation date of the access key.
     *
     * @param created the creation date to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Gets the user who created the access key.
     *
     * @return the creator's user ID
     */
    @JsonProperty(value = "createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created the access key.
     *
     * @param createdBy the creator's user ID to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the last modification date of the access key.
     *
     * @return the last modification date
     */
    @JsonProperty(value = "lastModified")
    public Date getLastModified() {
        return lastModified;
    }

    /**
     * Sets the last modification date of the access key.
     *
     * @param lastModified the last modification date to set
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Gets the user who last modified the access key.
     *
     * @return the last modifier's user ID
     */
    @JsonProperty(value = "lastModifiedBy")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Sets the user who last modified the access key.
     *
     * @param lastModifiedBy the last modifier's user ID to set
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, comment, created, createdBy, expiration, lastModified, lastModifiedBy, objectId,
            value, permission);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccessKeyDto other = (AccessKeyDto) obj;
        return Objects.equals(active, other.active) && Objects.equals(comment, other.comment)
            && Objects.equals(created, other.created) && Objects.equals(createdBy, other.createdBy)
            && Objects.equals(expiration, other.expiration) && Objects.equals(lastModified, other.lastModified)
            && Objects.equals(lastModifiedBy, other.lastModifiedBy) && Objects.equals(objectId, other.objectId)
            && Objects.equals(value, other.value) && permission == other.permission;
    }
}
