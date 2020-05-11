package io.netty.example.mystudy.rpc.remoting;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class RemotingClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;

    private String result;

    private String param;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(this.param);

        wait();

        return this.result;
    }

    void setParam(String param) {
        this.param = param;
    }
}
