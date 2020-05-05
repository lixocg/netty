package io.netty.example.mystudy.tcp.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder#decode invoke ......");

        //将二进制字节码转成 MyMessageProtocol数据包
        int len = in.readInt();

        byte[] content = new byte[len];
        in.readBytes(content);

        MyMessageProtocol protocol = new MyMessageProtocol();
        protocol.setLen(len);
        protocol.setContent(content);

        out.add(protocol);
    }
}
