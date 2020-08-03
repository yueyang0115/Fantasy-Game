package edu.duke.ece.fantasy.Soldier.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.net.Message;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviveResultMessage extends Message {
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
