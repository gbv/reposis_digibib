package de.vzg.reposis.digibib.agreement.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.vzg.reposis.digibib.agreement.exceptions.AgreementException;
import de.vzg.reposis.digibib.agreement.model.TransmissionInfo;

/**
 * Utility class for serializing and deserializing {@link TransmissionInfo} objects
 * to and from JSON using Jackson {@link ObjectMapper}.
 */
public class TransmissionInfoJsonMapper {

    private final ObjectMapper mapper;

    /**
     * Creates a new {@code AgreementJsonMapper} with the given {@link ObjectMapper}.
     *
     * @param mapper the {@link ObjectMapper} to use for JSON operations
     */
    public TransmissionInfoJsonMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Returns a shared singleton instance of the AgreementJsonMapper.
     *
     * @return the shared singleton instance of AgreementJsonMapper
     */
    public static TransmissionInfoJsonMapper obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    /**
     * Creates a new, independent instance of AgreementJsonMapper with default mapper.
     *
     * @return a new instance of AgreementService
     */
    public static TransmissionInfoJsonMapper createInstance() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return new TransmissionInfoJsonMapper(mapper);
    }

    /**
     * Serializes the given {@link TransmissionInfo} object to a JSON string.
     *
     * @param agreement the {@link TransmissionInfo} object to serialize
     * @return the JSON representation of the object
     * @throws AgreementException if serialization fails
     */
    public String toJson(TransmissionInfo agreement) {
        try {
            return mapper.writeValueAsString(agreement);
        } catch (JsonProcessingException e) {
            throw new AgreementException("Error serializing AgreementTransmissionInfo", e);
        }
    }

    /**
     * Deserializes a JSON string into an {@link TransmissionInfo} object.
     *
     * @param json the JSON string representing an {@link TransmissionInfo}
     * @return the deserialized {@link TransmissionInfo} object
     * @throws AgreementException if deserialization fails
     */
    public TransmissionInfo fromJson(String json) {
        try {
            return mapper.readValue(json, TransmissionInfo.class);
        } catch (IOException e) {
            throw new AgreementException("Fehler beim Deserialisieren von AgreementTransmissionInfo", e);
        }
    }

    private static final class InstanceHolder {
        private static final TransmissionInfoJsonMapper SHARED_INSTANCE = createInstance();
    }
}
