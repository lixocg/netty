package io.netty.example.mystudy.rpc.remoting;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.example.mystudy.rpc.provider.HelloServiceImpl;

public class RemotingServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        System.out.println("msg = " + msg);

        //客户端在调用服务器api时，需要定义一个具体的协议
        //比如 要求每次发送消息都是以某个字符串开头 "HelloService#hello#你好"

        if(msg.toString().startsWith("HelloService#hello")){
            //开始调用
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
