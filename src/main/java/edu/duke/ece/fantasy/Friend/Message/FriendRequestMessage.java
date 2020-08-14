package edu.duke.ece.fantasy.Friend.Message;

import edu.duke.ece.fantasy.Friend.CmdFriend;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

@MessageMeta(module = Modules.FRIEND, cmd = CmdFriend.REQ_FRIEND)
public class FriendRequestMessage extends Message {
    public enum ActionType {
        search, apply, check;
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
