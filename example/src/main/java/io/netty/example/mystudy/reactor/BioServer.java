package io.netty.example.mystudy.reactor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class BioServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(2537);
        while (!Thread.interrupted()) {
            new Thread(new Handler(serverSocket.accept())).start();
        }
    }

    static class Handler implements Runnable {
        final Socket socket;

        public Handler(Socket socket) {
            System.out.println("建立连接："+socket.getLocalSocketAddress());
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                String content;
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                while (true) {
                    content = bufferedReader.readLine();
                    if (content == null) {
                        break;
                    }

                    byte[] output = process(content);
                    write(output);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        private void write(byte[] output) throws Exception {
            PrintWriter printWriter = new PrintWriter(this.socket.getOutputStream(), true);
            printWriter.println(new String(output));
        }

        private byte[] process(String content) {
            System.out.println("业务处理中....." + Thread.currentThread().getName());
            byte[] output = (UUID.randomUUID() + "---" + content).getBytes();
            return output;
        }
    }
}
