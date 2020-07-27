package edu.duke.ece.fantasy.json;

public class FriendRequestMessage {
    public enum ActionType {
        search, apply;
    }

    String username;
    int id;
    ActionType action;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }
}
