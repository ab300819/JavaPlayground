package com.netty.exercise.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    private MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder(MarshallingEncoder marshallingEncoder) {
        this.marshallingEncoder = marshallingEncoder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("The encode message is null");
        }

        ByteBuf senBuf = Unpooled.buffer();
        senBuf.writeInt(msg.getHeader().getCrcCode());
        senBuf.writeInt(msg.getHeader().getLength());
        senBuf.writeLong(msg.getHeader().getSessionID());
        senBuf.writeByte(msg.getHeader().getType());
        senBuf.writeByte(msg.getHeader().getPriority());
        senBuf.writeInt(msg.getHeader().getAttachment().size());

        String key;
        byte[] keyArray;
        Object value;
        for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes(CharsetUtil.UTF_8);
            senBuf.writeInt(keyArray.length);
            senBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value, senBuf);
        }

        if (msg.getBody() != null) {
            marshallingEncoder.encode(msg.getBody(), senBuf);
        } else {
            senBuf.writeInt(0);
            senBuf.setInt(4, senBuf.readableBytes());
        }

    }
}