package edu.duke.ece.fantasy.net;

import edu.duke.ece.fantasy.database.Player;
import io.netty.channel.Channel;

public class UserSession {
    private Channel channel;

    private Player player;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
