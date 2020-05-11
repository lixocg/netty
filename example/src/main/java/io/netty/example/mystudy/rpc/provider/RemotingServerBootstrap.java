package io.netty.example.mystudy.rpc.provider;

import io.netty.example.mystudy.rpc.remoting.RemotingServer;

public class RemotingServerBootstrap {
    public static void main(String[] args) {
        RemotingServer.start("127.0.0.1",7000);
    }
}
