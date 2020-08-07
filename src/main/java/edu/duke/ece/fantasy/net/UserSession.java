package edu.duke.ece.fantasy.net;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.MessagesS2C;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.hibernate.Session;
import org.hibernate.boot.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserSession {
    private Channel channel;
    private int distributeKey;
    private Session dbSession;
    private Player player;
    MetaDAO metaDAO;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public UserSession(Channel channel) {
        this.channel = channel;
    }

    public void inactiveDbSession(){
        dbSession.close();
    }

    public void activeDbSession(){
        dbSession = HibernateUtil.getSessionFactory().openSession();
        metaDAO = new MetaDAO(dbSession);
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

    public MetaDAO getMetaDAO() {
        return metaDAO;
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
