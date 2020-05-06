package io.netty.example.mytest;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class DefaultPromiseTest {
    public static void main(String[] args) {

        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultChannelPromise channelFuture = new DefaultChannelPromise(
                new NioServerSocketChannel(),eventExecutors);
        eventExecutors.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("run.........");
                try {
                    Thread.sleep(3000L);
                    channelFuture.setSuccess(null);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        channelFuture.addListeners(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("====="+future);
            }
        });

        System.out.println("no waiter.......");

    }

    public void test01(){
        EventExecutor evtExecutor = ImmediateEventExecutor.INSTANCE;

        DefaultPromise<Integer> defaultPromise = new DefaultPromise<>(evtExecutor);
        defaultPromise.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                System.out.println("===="+future);
            }
        });

        evtExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.println("执行完成......");
                    defaultPromise.setSuccess(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
