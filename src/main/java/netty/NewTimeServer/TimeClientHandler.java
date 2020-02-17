package netty.NewTimeServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class TimeClientHandler extends ChannelHandlerAdapter {

    private int counter;
    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
    private byte[] req;

    public TimeClientHandler(){
        req=("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ByteBuf message=null;
        for (int i=0;i<100;i++){
            message=Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读取应答消息
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        //客户端每收到一条应答，打印一次计数器
        System.out.println("Now is:  "+body+"; the counter is : "+ ++counter);
    }

    //异常打印日志释放客户端
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        //释放资源
        logger.warning("Unexpected exception from downstream: "+cause.getMessage());
        ctx.close();
    }

}
