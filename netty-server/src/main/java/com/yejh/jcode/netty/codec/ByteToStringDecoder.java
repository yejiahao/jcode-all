package com.yejh.jcode.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2019-09-03
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ByteToStringDecoder extends ByteToMessageDecoder {

    private static final int HEAD_LENGTH = 8;

    private static final String BYTE_CHARSET = "UTF-8";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int dataLength = getDataLength(in);
        String content = dataLength == 0 ? null : getContent(in, dataLength);
        if (Objects.isNull(content)) {
            return;
        }
        out.add(content);
    }

    private int getDataLength(ByteBuf byteBuf) throws UnsupportedEncodingException {
        byteBuf.markReaderIndex();
        // 处理半包,如果还未达到头长度的话就返回 0
        if (byteBuf.readableBytes() < HEAD_LENGTH) {
            return 0;
        }
        // 获取数据长度
        byte[] headLength = new byte[HEAD_LENGTH];
        byteBuf.readBytes(headLength);
        // 标记一下 byteBuf 读索引的位置
        return Integer.parseInt(new String(headLength, BYTE_CHARSET));
    }

    private String getContent(ByteBuf byteBuf, int dataLength) throws UnsupportedEncodingException {
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return null;
        }
        byte[] contentBytes = new byte[dataLength];
        byteBuf.readBytes(contentBytes);
        return new String(contentBytes, BYTE_CHARSET);
    }
}
