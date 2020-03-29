package io.netty.example.mystudy.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class NioFileChannel {
    public static void main(String[] args) throws Exception {
        channelWrite();
        channelRead();
    }

    public static void channelCopy() throws Exception {

    }

    public static void channelRead() throws Exception {
        FileInputStream inputStream = new FileInputStream("/Users/lixiongcheng/a.txt");

        FileChannel fileChannel = inputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(inputStream.available());
        fileChannel.read(buffer);

        System.out.println(new String(buffer.array()));


    }

    public static void channelWrite() throws Exception {
        FileOutputStream outputStream = new FileOutputStream("/Users/lixiongcheng/a.txt");

        FileChannel fileChannel = outputStream.getChannel();

        //创建一个ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //向Buffer中写入数据
        byteBuffer.put("张三的歌".getBytes(Charset.defaultCharset()));

        //读写切换
        byteBuffer.flip();

        //将数据从Buffer中读出来，并写入到Channel中
        fileChannel.write(byteBuffer);

        outputStream.close();
    }
}
