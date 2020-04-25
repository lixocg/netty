package io.netty.example.mystudy.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest)msg;
            String uri = request.uri();
            if("/favicon.ico".equalsIgnoreCase(uri)){
                System.out.println("过滤/favicon资源");
                return;
            }

            System.out.println("msg 类型:" + msg.getClass());
            System.out.println("客户端地址:" + ctx.channel().remoteAddress());

            //回复浏览器信息
            ByteBuf content = Unpooled.copiedBuffer("hello this is server", CharsetUtil.UTF_8);

            //构造HttpResponse
            HttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,content);

            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain")
                    .set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            ctx.writeAndFlush(httpResponse);
        }
    }
}
