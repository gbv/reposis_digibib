package de.vzg.reposis.digibib.accesskey.dto;

import java.util.Date;
import java.util.Objects;

import org.mycore.datamodel.metadata.MCRObjectID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.vzg.reposis.digibib.accesskey.dto.util.Nullable;
import de.vzg.reposis.digibib.accesskey.dto.util.NullableDeserializer;
import de.vzg.reposis.digibib.accesskey.model.PermissionType;

/**
 * A DTO for partial updates of access keys.
 * This class allows for the partial updating of various fields of an access key,
 * with each field wrapped in a {@link Nullable} to indicate if the field should be updated.
 */
public class AccessKeyPartialUpdateDto {

    @JsonDeserialize(using = NullableDeserializer.class)
    private Nullable<String> value = new Nullable<>();

    @JsonDeserialize(using = NullableDeserializer.class)
    private Nullable<PermissionType> permission = new Nullable<>();

    @JsonDeserialize(using = NullableDeserializer.class)
    private Nullable<MCRObjectID> objectId = new Nullable<>();

    @JsonDeserialize(using = NullableDeserializer.class)
    private Nullable<Boolean> active = new Nullable<>();

    @JsonDeserialize(using = NullableDeserializer.class)
    private Nullable<String> comment = new Nullable<>();

    @JsonDeserialize(using = NullableDeserializer.class)
    private Nullable<Date> expiration = new Nullable<>();

    /**
     * Gets the value of the access key.
     *
     * @return a Nullable containing the value of the access key, or an empty Nullable if not set
     */
    @JsonProperty("value")
    public Nullable<String> getValue() {
        return value;
    }

    /**
     * Sets the value of the access key.
     *
     * @param value a Nullable containing the new value of the access key
     */
    public void setValue(Nullable<String> value) {
        this.value = value;
    }

    /**
     * Gets the permission type of the access key.
     *
     * @return a Nullable containing the permission type of the access key
     */
    @JsonProperty("permission")
    public Nullable<PermissionType> getPermission() {
        return permission;
    }

    /**
     * Sets the permission type of the access key.
     *
     * @param permission a Nullable containing the new permission type of the access key
     */
    public void setPermission(Nullable<PermissionType> permission) {
        this.permission = permission;
    }

    /**
     * Gets the object ID associated with the access key.
     *
     * @return a Nullable containing the object ID associated with the access key
     */
    @JsonProperty("objectId")
    public Nullable<MCRObjectID> getObjectId() {
        return objectId;
    }

    /**
     * Sets the object ID associated with the access key.
     *
     * @param objectId a Nullable containing the new object ID associated with the access key
     */
    public void setObjectId(Nullable<MCRObjectID> objectId) {
        this.objectId = objectId;
    }

    /**
     * Gets the active status of the access key.
     *
     * @return a Nullable containing the active status of the access key
     */
    @JsonProperty("active")
    public Nullable<Boolean> getActive() {
        return active;
    }

    /**
     * Sets the active status of the access key.
     *
     * @param active a Nullable containing the new active status of the access key
     */
    public void setActive(Nullable<Boolean> active) {
        this.active = active;
    }

    /**
     * Gets the comment associated with the access key.
     *
     * @return a Nullable containing the comment associated with the access key
     */
    @JsonProperty("comment")
    public Nullable<String> getComment() {
        return comment;
    }

    /**
     * Sets the comment associated with the access key.
     *
     * @param comment a Nullable containing the new comment associated with the access key
     */
    public void setComment(Nullable<String> comment) {
        this.comment = comment;
    }

    /**
     * Gets the expiration date of the access key.
     *
     * @return a Nullable containing the expiration date of the access key
     */
    @JsonProperty("expiration")
    public Nullable<Date> getExpiration() {
        return expiration;
    }

    /**
     * Sets the expiration date of the access key.
     *
     * @param expiration a Nullable containing the new expiration date of the access key
     */
    public void setExpiration(Nullable<Date> expiration) {
        this.expiration = expiration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, comment, expiration, objectId, permission, value);
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
        AccessKeyPartialUpdateDto other = (AccessKeyPartialUpdateDto) obj;
        return Objects.equals(active, other.active) && Objects.equals(comment, other.comment)
            && Objects.equals(expiration, other.expiration) && Objects.equals(objectId, other.objectId)
            && Objects.equals(permission, other.permission) && Objects.equals(value, other.value);
    }
}
