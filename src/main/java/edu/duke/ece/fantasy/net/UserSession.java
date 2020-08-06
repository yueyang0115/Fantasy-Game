package edu.duke.ece.fantasy.net;

import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.MessagesS2C;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserSession {
    private Channel channel;
    private int distributeKey;
    private Player player;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public UserSession(Channel channel) {
        this.channel = channel;
    }

    public void sendMsg(Message msg) {
        System.out.println("in user session");
        ChannelFuture future = channel.writeAndFlush(msg);
        if (!future.isSuccess()) {
            logger.error("fail:"+future.cause());
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setDistributeKey(int distributeKey) {
        this.distributeKey = distributeKey;
    }

    public int getDistributeKey() {
        return distributeKey;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
