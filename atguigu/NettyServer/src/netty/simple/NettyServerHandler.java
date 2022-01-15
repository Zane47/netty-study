package netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;


// import java.nio.ByteBuffer;

/**说明
 * 1.我们自定义一个Handler，需要继承netty规定好的某个HandlerAdapter，遵守他的规范
 * 2.这是我们自定义一个Handler，才能成为一个Handler
 *
 *
 */

public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**读取数据实际，这里我们可以读取客户端发送的消息
     * 有数据读取的时候，channelRead就会被触发
     * @param ctx 上下文对象, 含有许多信息：管道pipeline , 通道channel, 地址
     * @param msg 就是客户端发送的数据 默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // super.channelRead(ctx, msg);

        // 1: 用户程序自定义的普通任务



        /*
        // 这里模拟一个耗时很长的任务
        // 任务 -> 异步执行 -> 提交channel对应的NioEventLoop的taskQueue中
        // 解决方案:1. 2.
        Thread.sleep(10 * 1000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, Client, this is Server test1 for task queue",
                CharsetUtil.UTF_8));
        System.out.println("go on...");
        */
        /*
        System.out.println("服务器读取线程: " + Thread.currentThread().getName());
        System.out.println("server ctx= " + ctx);
        System.out.println("查看pipeline和Channel的关系");
        Channel channel = ctx.channel();
        // pipeline本质是一个双向链表，涉及到出栈入栈问题
        ChannelPipeline pipeline = ctx.pipeline();


        // 将msg转成bytebuf
        // 注意这里的ByteBuf是netty提供的，而不是NIO提供的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是: " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址是: " + ctx.channel().remoteAddress());
        */

    }

    /** 数据读取完毕后要send回client
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 数据写入缓存并刷新：Write + flush
        // 如果单纯写是不够的，ctx.write()
        // 一般来讲，需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, Client, this is Server wang", CharsetUtil.UTF_8));

    }

    /**处理异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // super.exceptionCaught(ctx, cause);
        // 需要关闭通道
        ctx.close();
    }
}
