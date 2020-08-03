package edu.duke.ece.fantasy.net;

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

    MessageDispatcher messageDispatcher;

    public MessageEventHandler(MessageDispatcher messageDispatcher){
        super();
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        logger.info("Establish connection,IP=[{}]",ChannelUtils.getIp(channel));
        if (!ChannelUtils.addChannelSession(ctx.channel(), new UserSession(channel))) {
            ctx.channel().close();
            logger.error("Duplicate session,IP=[{}]", ChannelUtils.getIp(channel));
        }
        UserSession userSession = ChannelUtils.getSessionBy(channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        Message packet = (Message) msg;
        logger.info("receive pact, content is {}", ObjectMapperFactory.getObjectMapper().writeValueAsString(packet));

        final Channel channel = context.channel();
        UserSession session = ChannelUtils.getSessionBy(channel);
        messageDispatcher.dispatch(session, packet);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        UserSession userSession = ChannelUtils.getSessionBy(channel);
        logger.info("Close connection,IP=[{}]", ChannelUtils.getIp(channel));
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
