package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;

public class ObjectMapperFactory {
    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
//            objectMapper.configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        }
        return objectMapper;
    }
}
