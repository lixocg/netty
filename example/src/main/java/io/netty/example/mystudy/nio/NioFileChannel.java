package io.netty.example.mystudy.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class NioFileChannel {
    public static void main(String[] args) throws Exception {
        channelWrite();
//        channelRead();
//        channelCopy();
//        trasferFromCopy();

        transferToCopy();
    }


    public static void transferToCopy() throws Exception{
        FileInputStream fileInputStream = new FileInputStream("/Users/lixiongcheng/a.txt");
        FileOutputStream outputStream = new FileOutputStream("/Users/lixiongcheng/d.txt");

        FileChannel srcChannel = fileInputStream.getChannel();
        FileChannel destChannel = outputStream.getChannel();

//        destChannel.transferFrom(srcChannel,0,srcChannel.size());
        srcChannel.transferTo(0,srcChannel.size(),destChannel);

        fileInputStream.close();
        outputStream.close();

        srcChannel.close();
        destChannel.close();
    }

    public static void trasferFromCopy() throws Exception{
        FileInputStream fileInputStream = new FileInputStream("/Users/lixiongcheng/a.txt");
        FileOutputStream outputStream = new FileOutputStream("/Users/lixiongcheng/c.txt");

        FileChannel srcChannel = fileInputStream.getChannel();
        FileChannel destChannel = outputStream.getChannel();

        destChannel.transferFrom(srcChannel,0,srcChannel.size());

        fileInputStream.close();
        outputStream.close();

        srcChannel.close();
        destChannel.close();
    }

    public static void channelCopy() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/Users/lixiongcheng/a.txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("/Users/lixiongcheng/b.txt");
        FileChannel outputStreamChannel = outputStream.getChannel();

        //读取数据到buffer中
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        while (true){
            //重要操作：需要复位一下,如果不复位：当分配的buffer小于属于长度时，第一次将读满buffer，
            // 导致后续不能在读到数据，readBytes == 0，将会一直将第一次读到数据往outputStreamChannel中写
            //clear() limit = position,position = 0,并不清空buffer数据
            byteBuffer.clear();

            int readBytes = inputStreamChannel.read(byteBuffer);

            //数据读取完毕
            if(readBytes == -1){
                break;
            }

            //将buffer中数据写到outpuStreamChannel中
            byteBuffer.flip();

            outputStreamChannel.write(byteBuffer);
        }

        fileInputStream.close();
        outputStream.close();
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
        byteBuffer.put("张三的歌深V和是时候GV的深V的划分深V回复是和深V好地方".getBytes(Charset.defaultCharset()));

        //读写切换
        byteBuffer.flip();

        //将数据从Buffer中读出来，并写入到Channel中
        fileChannel.write(byteBuffer);

        outputStream.close();
    }
}
