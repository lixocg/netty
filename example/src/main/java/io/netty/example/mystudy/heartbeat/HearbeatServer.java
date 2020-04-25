package io.netty.example.mystudy.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


public class HearbeatServer {
    public static void main(String[] args)throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /**
                             * IdleStateHandler:netty提供的处理空间状态的处理器
                             * 参数：
                             * long readerIdleTime：多长时间没有读，就会发送一个心跳检测包检测是否连接
                             * long writerIdleTime：多长时间没有写，就会发送一个心跳检测包检测是否连接
                             * long allIdleTime：多长时间没有读写，就会发送一个心跳检测包检测是否连接
                             * IdleStateHandler说明：当一个连接在一段时间内没有发生读、写或读写会触发一个IdleStateEvent事件，该事件会传递到管道的
                             * 下一个handler去处理，即在下一个handler的userEventTriggered方法中处理IdleStateEvent事件
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            pipeline.addLast(new HeartBeatServerHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
