package com.yejh.jcode.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class XmlDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        int len = byteBuf.readableBytes();
        log.info("len: {}", len);
        byte[] byteArr = new byte[len];
        byteBuf.readBytes(byteArr);
        String str = new String(byteArr);
        log.info("str: \n{}", str);
        list.add(str);
    }
}
