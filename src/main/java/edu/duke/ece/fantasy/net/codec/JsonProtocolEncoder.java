package edu.duke.ece.fantasy.net.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.ObjectMapperFactory;
import edu.duke.ece.fantasy.net.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonProtocolEncoder extends MessageToByteEncoder<Message> {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        out.writeShort(msg.getModule());
        out.writeByte(msg.getCmd());
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        byte[] raw = objectMapper.writeValueAsBytes(msg);
        log.info("send msg:"+objectMapper.writeValueAsString(msg));
        out.writeBytes(raw);
    }

}
