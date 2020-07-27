package edu.duke.ece.fantasy.json;

public class ReviveRequestMessage {
    private String action;

    public ReviveRequestMessage() {
    }

    public ReviveRequestMessage(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
