package edu.duke.ece.fantasy.net;

import edu.duke.ece.fantasy.MessageHandler;
import edu.duke.ece.fantasy.ObjectMapperFactory;
import edu.duke.ece.fantasy.json.MessagesC2S;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageEventHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(MessageEventHandler.class);

    MessageHandler messageHandler;

    public MessageEventHandler(MessageHandler messageHandler){
        super();
        this.messageHandler = messageHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (!ChannelUtils.addChannelSession(ctx.channel(), new UserSession(channel))) {
            ctx.channel().close();
            logger.error("Duplicate session,IP=[{}]", ChannelUtils.getIp(channel));
        }
        UserSession userSession = ChannelUtils.getSessionBy(channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        MessagesC2S packet = (MessagesC2S) msg;
        logger.info("receive pact, content is {}", ObjectMapperFactory.getObjectMapper().writeValueAsString(packet));

        final Channel channel = context.channel();
        UserSession session = ChannelUtils.getSessionBy(channel);
//        messageDispatcher.dispatch(session, packet);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        UserSession userSession = ChannelUtils.getSessionBy(channel);
//        messageDispatcher.onSessionClosed(userSession);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if (channel.isActive() || channel.isOpen()) {
            ctx.close();
        }
        if (!(cause instanceof IOException)) {
            logger.error("remote:" + channel.remoteAddress(), cause);
        }
    }
}
