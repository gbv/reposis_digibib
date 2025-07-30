package de.vzg.reposis.digibib.agreement.factory;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.vzg.reposis.digibib.agreement.exception.AgreementException;
import de.vzg.reposis.digibib.agreement.model.AgreementTransmissionInfo;

/**
 * Utility class for serializing and deserializing {@link AgreementTransmissionInfo} objects
 * to and from JSON using Jackson {@link ObjectMapper}.
 */
public class AgreementJsonMapper {

    private final ObjectMapper mapper;

    /**
     * Creates a new {@code AgreementJsonMapper} with the given {@link ObjectMapper}.
     *
     * @param mapper the {@link ObjectMapper} to use for JSON operations
     */
    public AgreementJsonMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Returns a shared singleton instance of the AgreementJsonMapper.
     *
     * @return the shared singleton instance of AgreementJsonMapper
     */
    public static AgreementJsonMapper obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    /**
     * Creates a new, independent instance of AgreementJsonMapper with default mapper.
     *
     * @return a new instance of AgreementService
     */
    public static AgreementJsonMapper createInstance() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return new AgreementJsonMapper(mapper);
    }

    /**
     * Serializes the given {@link AgreementTransmissionInfo} object to a JSON string.
     *
     * @param agreement the {@link AgreementTransmissionInfo} object to serialize
     * @return the JSON representation of the object
     * @throws AgreementException if serialization fails
     */
    public String toJson(AgreementTransmissionInfo agreement) {
        try {
            return mapper.writeValueAsString(agreement);
        } catch (JsonProcessingException e) {
            throw new AgreementException("Error serializing AgreementTransmissionInfo", e);
        }
    }

    /**
     * Deserializes a JSON string into an {@link AgreementTransmissionInfo} object.
     *
     * @param json the JSON string representing an {@link AgreementTransmissionInfo}
     * @return the deserialized {@link AgreementTransmissionInfo} object
     * @throws AgreementException if deserialization fails
     */
    public AgreementTransmissionInfo fromJson(String json) {
        try {
            return mapper.readValue(json, AgreementTransmissionInfo.class);
        } catch (IOException e) {
            throw new AgreementException("Fehler beim Deserialisieren von AgreementTransmissionInfo", e);
        }
    }

    private static final class InstanceHolder {
        private static final AgreementJsonMapper SHARED_INSTANCE = createInstance();
    }
}
