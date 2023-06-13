package com.yejh.jcode.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@ChannelHandler.Sharable
public class XmlEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) {
        String resp = String.valueOf(o);
        byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);
        String prefix = String.format("%06d", bytes.length);

        byteBuf.writeBytes("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
        byteBuf.writeBytes(String.format("Content-Length: %d\r\n", prefix.length() + bytes.length).getBytes(StandardCharsets.UTF_8));
        byteBuf.writeBytes(String.format("Content-Type: application/xml; charset=%s\r\n", StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8));
        byteBuf.writeBytes("\r\n".getBytes(StandardCharsets.UTF_8));
        // 响应报文前追加长度
        byteBuf.writeBytes(prefix.getBytes(StandardCharsets.UTF_8))
                .writeBytes(bytes);
    }
}
