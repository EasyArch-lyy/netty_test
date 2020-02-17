package netty.NewTimeServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
/**
* 继承ChannelHandlerAdapter，用于对网络事件读写操作
* */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf buf= (ByteBuf) msg;
        byte[]req=new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body=new String(req,"UTF-8").substring(0,req.length-System.getProperty("line.separator").length());
        System.out.println("The time server receive order: "+body+" ; the counter is: "+ ++counter);
        String currentTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new java.util.Date(
                System.currentTimeMillis()).toString():"BAD ORDER";
        ByteBuf resp= Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        //将消息发送队列中的消息写入SocketChannel发送给对方
        ctx.flush();
    }
    //为了避免频繁唤醒Selector进行消息发送，Netty的write方法把待发送的消息放到发送缓冲数组，调用flush将缓冲区内消息写入SocketChannel

    @Override
    //发生异常，关闭ChannelHandlerContext，释放相关句柄
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.close();
    }
}
