package io.netty.example.mystudy.codec.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) throws Exception {
        System.out.println("收到客户端数据:"+msg.getId()+","+msg.getName());
    }
}
