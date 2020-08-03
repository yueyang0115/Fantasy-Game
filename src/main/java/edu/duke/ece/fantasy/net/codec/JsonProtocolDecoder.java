package edu.duke.ece.fantasy.net.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.ObjectMapperFactory;
import edu.duke.ece.fantasy.json.MessagesC2S;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class JsonProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readableBytes();

        final int metaSize = 3;
        short module = in.readShort();
        byte cmd = in.readByte();

        byte[] body = new byte[len - metaSize];
        in.readBytes(body);
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        Message msg = (Message) objectMapper.readValue(body, MessageUtil.INSTANCE.getMessage(module,cmd));
        out.add(msg);
    }
}
