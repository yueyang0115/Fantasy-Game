package edu.duke.ece.fantasy.Soldier.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.net.Message;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeRequestMessage extends Message {
    private String type;

    public AttributeRequestMessage() {
    }

    public AttributeRequestMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
