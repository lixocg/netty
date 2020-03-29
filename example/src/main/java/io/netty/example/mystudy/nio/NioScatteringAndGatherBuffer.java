package io.netty.example.mystudy.nio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering:将数据写入到 buffer 时，可以采用 buffer 数组，依次写入 [分散]
 * Gathering: 从 buffer 读取数据时，可以采用 buffer 数组，依次读
 */
public class NioScatteringAndGatherBuffer {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetAddress = new InetSocketAddress(7000);
        serverSocketChannel.socket().bind(inetAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(3);
        byteBuffers[1] = ByteBuffer.allocate(5);

        SocketChannel socketChannel = serverSocketChannel.accept();


        int msgLength = 8;

        while (true) {
            int bytesRead = 0;

            while (bytesRead < msgLength) {
                //阻塞等待客户端写入数据
                long read = socketChannel.read(byteBuffers);
                bytesRead += read;
                System.out.println("bytesRead = " + bytesRead);

                Arrays.asList(byteBuffers).stream().map(buffer ->
                        "postion=" + buffer.position() + ", " +
                                "limit=" + buffer.limit()
                ).forEach(System.out::println);
            }

            //将所有的 buffer 进行 flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            //将数据读出显示到客户端
            long byteWirte = 0;
            while (byteWirte < msgLength) {
                long l = socketChannel.write(byteBuffers); //
                byteWirte += l;
            }


            //将所有的 buffer 进行 clear
            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.clear();
            });

            System.out.println("byteRead:=" + bytesRead + " byteWrite=" + byteWirte + ", msgLength" + msgLength);
        }
    }
}
