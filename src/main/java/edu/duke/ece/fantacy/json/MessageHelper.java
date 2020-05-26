package edu.duke.ece.fantacy.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import org.jboss.logging.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class MessageHelper {
    public static String serialize(MessagesS2C m) {
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = null;
        try {
            msg = objectMapper.writeValueAsString(m);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static MessagesC2S deserialize(String string){
        ObjectMapper objectMapper = new ObjectMapper();
        MessagesC2S m = null;
        try {
            m = objectMapper.readValue(string, MessagesC2S.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m;
    }
}
