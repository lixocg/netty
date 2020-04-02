package io.netty.example.mystudy.reactor;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.UUID;

public class SingleReactorSingleThread {
    public static void main(String[] args) throws Exception {
        new Thread(new Reactor(2537)).start();
    }

    static class Reactor implements Runnable {
        final Selector selector;
        final ServerSocketChannel serverSocketChannel;

        public Reactor(int port) throws Exception {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            //绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(port));

            //注册channel，并注册accept事件
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Acceptor(selectionKey));

        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    //等待事件发生
                    this.selector.select();

                    //发生事件集合
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();

                    selectedKeys.forEach(selectionKey -> {
                        //read:1,write:4,accept:16
                        System.out.println("当前事件:" + selectionKey.interestOps());
                        //事件分发
                        dispatch(selectionKey);
                    });
                    //清空当前时间
                    selectedKeys.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void dispatch(SelectionKey selectionKey) {
            Runnable r = (Runnable) selectionKey.attachment();
            if (r != null) {
                r.run();
            }
        }

        /**
         * inner class
         */
        class Acceptor implements Runnable {
            final SelectionKey sk;

            public Acceptor(SelectionKey sk) {
                this.sk = sk;
            }

            @Override
            public void run() {
                try {
                    if (sk.isAcceptable()) {
                        //接受连接
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        System.out.println("接受客户端连接:" + socketChannel.getRemoteAddress());
                        if (socketChannel != null) {
                            new Handler(selector, socketChannel);
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    static class Handler implements Runnable {
        final SocketChannel socketChannel;
        final SelectionKey sk;

        ByteBuffer input = ByteBuffer.allocate(512);
        ByteBuffer output = ByteBuffer.allocate(512);


        public Handler(Selector selector, SocketChannel socketChannel) throws Exception {
            this.socketChannel = socketChannel;
            this.socketChannel.configureBlocking(false);
            //注册通道,注册read事件
            this.sk = this.socketChannel.register(selector, SelectionKey.OP_READ, this);
            //唤醒等待事件
            selector.wakeup();
        }

        @Override
        public void run() {
            try {
                if (sk.isReadable()) {
                    read();
                } else if (sk.isWritable()) {
                    send();
                }
            } catch (Exception e) {

            }
        }

        public void read() throws Exception {
            this.socketChannel.read(input);
            if (inputIsComplete()) {
                process(input);
                //注册写事件
                this.sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        public void send() throws Exception {
            output.put(UUID.randomUUID().toString().getBytes());
            output.flip();
            this.socketChannel.write(output);
            output.clear();
            if (outputIsComplete()) {
//                sk.cancel();
                this.sk.interestOps(SelectionKey.OP_READ);
            }

        }

        public boolean inputIsComplete() {
            return true;
        }

        public boolean outputIsComplete() {
            return true;
        }

        void process(ByteBuffer input) {
            input.flip();
            System.out.println("业务正在处理数据....." + new String(input.array()));
            input.clear();
        }
    }

}
