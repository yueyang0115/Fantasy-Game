package edu.duke.ece.fantasy.Friend.Message;

import edu.duke.ece.fantasy.Friend.CmdFriend;
import edu.duke.ece.fantasy.Soldier.Message.CmdSoldier;
import edu.duke.ece.fantasy.dto.PlayerInfo;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = Modules.FRIEND, cmd = CmdFriend.RES_FRIEND)
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
