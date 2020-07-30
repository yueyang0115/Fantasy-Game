package edu.duke.ece.fantasy.net.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.ObjectMapperFactory;
import edu.duke.ece.fantasy.json.MessagesC2S;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class JsonProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readableBytes();
        byte[] body = new byte[len];
        in.readBytes(body);
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        MessagesC2S msg = objectMapper.readValue(body, MessagesC2S.class);
        out.add(msg);
    }
}
