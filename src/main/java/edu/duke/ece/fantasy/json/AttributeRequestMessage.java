package edu.duke.ece.fantasy.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeRequestMessage {
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
