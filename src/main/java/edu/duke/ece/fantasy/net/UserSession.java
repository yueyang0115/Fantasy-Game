package edu.duke.ece.fantasy.net;

import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.MessagesS2C;
import io.netty.channel.Channel;

public class UserSession {
    private Channel channel;

    private Player player;

    public UserSession(Channel channel) {
        this.channel = channel;
    }

    public void sendMsg(MessagesS2C msg) {
        channel.writeAndFlush(msg);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
