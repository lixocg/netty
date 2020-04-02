package io.netty.example.mystudy.reactor.singleReactorSingleThread;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class SingleThreadReactor {
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

            //注册channel
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Acceptor());

        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    //等待事件发生
                    this.selector.select();

                    //发生事件集合
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();

                    Iterator<SelectionKey> iterator = selectedKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selected = iterator.next();
                        //事件分发
                        dispatch(selected);
                    }
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

            @Override
            public void run() {
                try {
                    //接受连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("接受客户端连接:"+socketChannel.);
                    if (socketChannel != null) {
                        new Handler(selector, socketChannel);
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

        private final static int READING = 0;
        private final static int SENDING = 1;

        int state = READING;

        public Handler(Selector selector, SocketChannel socketChannel) throws Exception {
            this.socketChannel = socketChannel;
            this.socketChannel.configureBlocking(false);
            //注册通道
            this.sk = this.socketChannel.register(selector, 0);
            this.sk.attach(this);
            //注册read事件
            this.sk.interestOps(SelectionKey.OP_READ);

            //唤醒等待事件
            selector.wakeup();
        }

        @Override
        public void run() {
            try {
                if (state == READING) {
                    read();
                } else if (state == SENDING) {
                    send();
                }
            } catch (Exception e) {

            }
        }

        public void read() throws Exception {
            this.socketChannel.read(input);
            if (inputIsComplete()) {
                process();
                state = SENDING;
                //注册写事件
                this.sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        public void send() throws Exception {
            output.put(UUID.randomUUID().toString().getBytes());
            output.flip();
            this.socketChannel.write(output);
            if (outputIsComplete()) {
                sk.cancel();
            }
        }

        public boolean inputIsComplete() {
            return true;
        }

        public boolean outputIsComplete() {
            return true;
        }

        void process() {
            System.out.println("业务正在处理.....");
        }
    }

}
