package edu.duke.ece.fantasy.net;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class ChannelUtils {
    public static AttributeKey<UserSession> SESSION_KEY = AttributeKey.valueOf("session");
    private static AtomicInteger distributeKeyGenerator = new AtomicInteger();

    public static boolean addChannelSession(Channel channel, UserSession userSession) {
        Attribute<UserSession> sessionAttr = channel.attr(SESSION_KEY);
        return sessionAttr.compareAndSet(null, userSession);
    }

    public static UserSession getSessionBy(Channel channel) {
        Attribute<UserSession> sessionAttr = channel.attr(SESSION_KEY);
        return sessionAttr.get();
    }

    public static int getNextDistributeKey() {
        return distributeKeyGenerator.getAndIncrement();
    }

    public static String getIp(Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().toString().substring(1);
    }
}
