package io.netty.example.mytest;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;

public class MyTest {
    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new DefaultEventLoopGroup();
        eventLoopGroup.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("oooooo");
            }
        });
    }
}
