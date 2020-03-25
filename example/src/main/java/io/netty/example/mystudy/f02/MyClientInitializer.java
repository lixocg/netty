package io.netty.example.mystudy.f02;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 注释很重要:MyClientInitializer本身也是一个处理，作用是向channelPipeline中添加其他处理器，添加完成后该处理器会被移除。
     * This method will be called once the {@link Channel} was registered. After the method returns this instance
     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
     *
     * @param ch the {@link Channel} which was registered.
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));//入站处理器 ①
        pipeline.addLast(new LengthFieldPrepender(4));//入站处理器 ②
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));//入站处理器 ③
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));//出站处理器 ④
        pipeline.addLast(new MyClientHandler());//入站处理器 ⑤

        //所以在pipeline上执行，入站处理器数据流向：peer --> ① --> ② --> ③ --> ⑤ --> 程序
        //出站处理器  程序 --> ④ -->peer

        //客户端处理器类似
    }
}
