package io.netty.example.mystudy.rpc.provider;

import io.netty.example.mystudy.rpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息:" + msg);

        if(msg != null){
            return  "你好，客户端，我收到你的消息:"+msg;
        }
        return "你好，客户端，我收到你的消息";
    }
}
