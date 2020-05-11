package io.netty.example.mystudy.rpc.consumer;

import io.netty.example.mystudy.rpc.publicinterface.HelloService;
import io.netty.example.mystudy.rpc.remoting.RemotingClient;

public class RemotingClientBootStrap {

    private static final String providerName = "HelloService#hello#";

    public static void main(String[] args) {
        RemotingClient remotingClient = new RemotingClient();

        HelloService helloService = (HelloService) remotingClient.getBean(HelloService.class, providerName);

        String res = helloService.hello("卧槽...");

        System.out.println("服务端返回结果:" + res);
    }
}
