package io.netty.example.mystudy.nio;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

public class NioNetServer {
    public static void main(String[] args) throws Exception {
        //创建ServerSocketChannel实例，配置非阻塞
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //创建Selector实例
        Selector selector = Selector.open();

        //将ServerSocketChannel注册到Selector上,并设置初始关注事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //事件轮询选择
        while (true) {
            if (selector.select(5000) == 0) {
                System.out.println("等待5s，无事件发生");
                continue;
            }

            //发生事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历事件并处理
            selectionKeys.forEach(selectionKey -> {
                try {
                    //处理连接事件
                    if (selectionKey.isAcceptable()) {
                        //获取当前ServerSocketChannel的客户端连接通道SocketChannel
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);

                        System.out.println("客户端连接成功:"+socketChannel.getRemoteAddress());
                        //向当前Selector注册读事件，并关联一个Buffer
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(512));

                    }

                    //处理read事件
                    if (selectionKey.isReadable()) {
                        //获取该事件关联的通道
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        //获取事件在注册OP_READ事件时关联的buffer
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        //将客户端发送到SocketChannel中的数据读到buffer中
                        int bytesRead = socketChannel.read(byteBuffer);
                        //打印一下读到的数据
                        if(bytesRead > 0) {
                            System.out.println("from client ：" + new String(byteBuffer.array()));
                            byteBuffer.clear();
                        }
                    }

                    //移除当前事件，防止重复处理
                    selectionKeys.remove(selectionKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
