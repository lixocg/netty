package io.netty.example.mystudy.reactor;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

public class MultiReactorMultiThread {
    public static void main(String[] args) throws Exception {
    }

    static class MainReactor implements Runnable{

        final Selector selector;

        final ServerSocketChannel serverSocketChannel;

        public MainReactor(int port) throws Exception{
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));

            selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }

        @Override
        public void run(){
            try {
                while (!Thread.interrupted()) {
                    selector.select();

                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    selectionKeys.forEach(selectionKey -> {

                    });

                    selectionKeys.clear();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        static class Acceptor implements Runnable{
             SelectionKey sk;

            @Override
            public void run() {
                try {
                    if (sk.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) sk.channel();
                        serverSocketChannel.accept();

                        sk.interestOps(SelectionKey.OP_READ);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }




}
