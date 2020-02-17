package netty.DelimiterBaseFrameDecoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {

    private int counter;
    static final String ECHO_REQ="Hi.Welcome to Netty. $_";

    public EchoClientHandler(){

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        //建立连接后循环发送消息请求给客户端
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        //打印接受到服务端应答消息同时计数
        System.out.println("This is "+ ++counter+ "times receive server: ["+msg+"]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
