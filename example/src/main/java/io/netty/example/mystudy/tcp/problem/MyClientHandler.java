package io.netty.example.mystudy.tcp.problem;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int cnt;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buf = new byte[msg.readableBytes()];
        msg.readBytes(buf);

        String data = new String(buf, CharsetUtil.UTF_8);

        System.out.println("收到服务器发来的数据=" + data);
        System.out.println("收到服务器发来的数据量=" + (++cnt));

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for (int i = 0; i < 10; i++) {
            ByteBuf buf = Unpooled.copiedBuffer("hello server " + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(buf);
        }
    }
}
