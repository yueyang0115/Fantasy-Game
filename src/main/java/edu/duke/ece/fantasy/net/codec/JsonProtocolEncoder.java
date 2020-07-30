package edu.duke.ece.fantasy.net.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.ObjectMapperFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class JsonProtocolEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        byte[] raw = objectMapper.writeValueAsBytes(msg);
        out.writeBytes(raw);
    }
}
