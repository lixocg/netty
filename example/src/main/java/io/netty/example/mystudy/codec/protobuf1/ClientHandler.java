package io.netty.example.mystudy.codec.protobuf1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.example.mystudy.codec.protobuf0.StudentPOJO;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道准备就绪触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //随机发送Student或者Worker对象
        int random = new Random().nextInt(3);

        MyDataInfo.MyMessage myMessage = null;

        //发送Student对象
        if(0 == random){
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(3).setName("老李").build()).build();
        }else {
            //发送worker对象
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.workerType)
                    .setWorker(MyDataInfo.Worker.newBuilder().setAge(30).setName("老张").build()).build();
        }

        ctx.writeAndFlush(myMessage);
    }

    /**
     * 读事件发生触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("from server:" + buf.toString(CharsetUtil.UTF_8) + ",adr=" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
