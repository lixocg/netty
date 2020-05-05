package io.netty.example.mystudy.tcp.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<MyMessageProtocol> {
    private int cnt;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessageProtocol msg) throws Exception {
        System.out.println("收到服务器消息=" + new String(msg.getContent(), CharsetUtil.UTF_8) + ",len=" + msg.getLen());
        System.out.println("收到服务器消息数=" + (++cnt));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for (int i = 0; i < 10; i++) {
            String data = "天气冷,吃火锅 " + i;
            byte[] content = data.getBytes(CharsetUtil.UTF_8);
            int length = content.length;

            MyMessageProtocol protocol = new MyMessageProtocol();
            protocol.setContent(content);
            protocol.setLen(length);

            ctx.writeAndFlush(protocol);
        }
    }
}
