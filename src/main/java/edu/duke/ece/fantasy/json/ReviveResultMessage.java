package edu.duke.ece.fantasy.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviveResultMessage {
    private String result;

    public ReviveResultMessage() {
    }

    public ReviveResultMessage(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
