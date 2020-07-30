package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.dto.PlayerInfo;

import java.util.ArrayList;
import java.util.List;

public class FriendResultMessage {
    List<PlayerInfo> playerInfoList;
//    String

    public List<PlayerInfo> getPlayerInfoList() {
        return playerInfoList;
    }

    public void setPlayerInfoList(List<PlayerInfo> playerInfoList) {
        this.playerInfoList = playerInfoList;
    }
}
