package edu.duke.ece.fantasy.Friend.Message;

import edu.duke.ece.fantasy.dto.PlayerInfo;
import edu.duke.ece.fantasy.net.Message;

import java.util.ArrayList;
import java.util.List;

public class FriendResultMessage extends Message {
    List<PlayerInfo> playerInfoList;
//    String

    public List<PlayerInfo> getPlayerInfoList() {
        return playerInfoList;
    }

    public void setPlayerInfoList(List<PlayerInfo> playerInfoList) {
        this.playerInfoList = playerInfoList;
    }
}
