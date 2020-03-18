package io.netty.example.f02;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " , " + msg);

        //ctx.channel().writeAndFlush ==>> pipeline.writeAndFlush() 会从最后一个Handler依次调用直到到第一个，然后把数据写到peer
        //每个channel都关联一个channelPipeline
        ctx.channel().writeAndFlush("from Server: " + UUID.randomUUID());

        // 从当前Handler依次往前调用直到第一个，然后把数据写到peer
        // 每个Handler都关联一个HandlerContext
        //注意这两者区别
        //ctx.writeAndFlush("from Server: " + UUID.randomUUID());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
