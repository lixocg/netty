package io.netty.example.mystudy.tcp.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MyMessageProtocol> {
    private int count;


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessageProtocol msg) throws Exception {
        byte[] content = msg.getContent();
        int len = msg.getLen();

        String data = new String(content, CharsetUtil.UTF_8);
        System.out.println("服务器接受到消息:" + data + ",长度=" + len);
        System.out.println("服务器接受到消息量:" + (++count));


        //回送消息给客户端
        String replyData = "好的，走起 ";
        byte[] replyDataBytes = replyData.getBytes(CharsetUtil.UTF_8);
        int length = replyDataBytes.length;
        MyMessageProtocol protocol = new MyMessageProtocol();
        protocol.setLen(length);
        protocol.setContent(replyDataBytes);
        ctx.writeAndFlush(protocol);

    }
}
