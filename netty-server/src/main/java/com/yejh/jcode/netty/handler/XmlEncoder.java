package com.yejh.jcode.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@ChannelHandler.Sharable
public class XmlEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        String resp = String.valueOf(o);
        byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);

        String len = String.format("%06d", bytes.length); // 响应报文前追加长度
        byteBuf.writeBytes(len.getBytes(StandardCharsets.UTF_8));
        byteBuf.writeBytes(bytes);
    }
}
