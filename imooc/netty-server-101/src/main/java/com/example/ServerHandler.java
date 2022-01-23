package com.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("server channel active..");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 1. 读取客户端的数据(缓存中去取并打印到控制台)
        ByteBuf buf = (ByteBuf) msg;
        byte[] request = new byte[buf.readableBytes()];
        buf.readBytes(request);

        String requestBody = new String(request, "utf-8");
        System.err.println("Server: " + requestBody);

        // 2.返回响应数据
        String responseBody = "返回响应数据: " + requestBody;

        ctx.writeAndFlush(Unpooled.copiedBuffer(responseBody.getBytes()));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
