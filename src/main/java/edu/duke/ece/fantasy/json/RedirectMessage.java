package edu.duke.ece.fantasy.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.net.Message;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedirectMessage extends Message {
    String destination;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
