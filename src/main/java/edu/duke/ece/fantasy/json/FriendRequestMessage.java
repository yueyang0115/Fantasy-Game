package edu.duke.ece.fantasy.json;

public class FriendRequestMessage {
    public enum ActionType {
        search, apply;
    }

    String username;

    ActionType action;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }
}
