package netty.DelimiterBaseFrameDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {

    int counter=0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        String body= (String) msg;
        //打印接受到的消息，DelimiterBaseFrameDecoder自动解码消息，msg对象时完整的消息包
        //第二个ChannelHandler是StringDecoder，将ByteBuf解码成字符串对象
        //第三个EchoServerHandler接受到msg消息是解码后的字符串对象
        System.out.println("This is"+ ++counter+" times receive client:["+body+" ]");
        body+="$_";
        ByteBuf echo= Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
