package edu.duke.ece.fantasy.net;

import edu.duke.ece.fantasy.net.codec.JsonProtocolDecoder;
import edu.duke.ece.fantasy.net.codec.JsonProtocolEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class SocketServer {
    private Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private int maxReceiveBytes;

    public SocketServer(int maxReceiveBytes) {
        this.maxReceiveBytes = maxReceiveBytes;
    }

    public SocketServer() {
    }

    public void start() throws Exception {
//        int serverPort = ServerConfig.getInstance().getServerPort();
        int serverPort = 1234;

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            b.bind(new InetSocketAddress(serverPort))
                    .sync()
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            logger.info("netty server start, listen to user's request @port:" + serverPort + "......");
                        } else {
                            logger.info("netty server start failed");
                        }
                    });
//			f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("", e);
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            throw e;
        }
    }

    public void shutdown() {

    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            // decode
            pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
            pipeline.addLast(new JsonProtocolDecoder());
            // encode
            pipeline.addLast(new LengthFieldPrepender(2));
            pipeline.addLast(new JsonProtocolEncoder());
            // IdleEventHandler
            pipeline.addLast(new IdleStateHandler(0, 0, 300));
            pipeline.addLast(new MessageEventHandler(new MessageDispatcher()));
        }
    }
}


