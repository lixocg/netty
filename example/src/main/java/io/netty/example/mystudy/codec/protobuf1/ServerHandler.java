package io.netty.example.mystudy.codec.protobuf1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.mystudy.codec.protobuf0.StudentPOJO;

public class ServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("student,id=" + student.getId() + ",name=" + student.getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.workerType) {
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("worker,name=" + worker.getName() + ",age=" + worker.getAge());
        } else {
            System.out.println("传输类型不对....");
        }
    }
}
