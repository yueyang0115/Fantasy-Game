package edu.duke.ece.fantasy.net;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class ChannelUtils {
    public static AttributeKey<UserSession> SESSION_KEY = AttributeKey.valueOf("session");

    public static boolean addChannelSession(Channel channel, UserSession userSession) {
        Attribute<UserSession> sessionAttr = channel.attr(SESSION_KEY);
        return sessionAttr.compareAndSet(null, userSession);
    }

    public static UserSession getSessionBy(Channel channel) {
        Attribute<UserSession> sessionAttr = channel.attr(SESSION_KEY);
        return sessionAttr.get() ;
    }

    public static String getIp(Channel channel) {
        return ((InetSocketAddress)channel.remoteAddress()).getAddress().toString().substring(1);
    }
}
