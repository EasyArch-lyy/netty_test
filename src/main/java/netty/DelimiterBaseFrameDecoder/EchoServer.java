package netty.DelimiterBaseFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
/**
* DelimiterBaseFrameDecoder
* 自动以分隔符作为码流结束标识的消息解码
* */
public class EchoServer {
    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        ServerBootstrap b=new ServerBootstrap();
        b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //创建分隔符缓冲对象ByteBuf
                        ByteBuf delimiter= Unpooled.copiedBuffer("$_".getBytes());
                        //1024表示单条消息的最大长度，到达长度未出现分隔符抛TooLongFrameException
                        //第二个参数是分隔符缓冲对象
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new EchoServerHandler());
                    }
                });
        ChannelFuture f=b.bind(port).sync();
        f.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        int port=8080;
        if(args!=null&&args.length>0){
            port=Integer.valueOf(args[0]);
        }
        new EchoServer().bind(port);
    }
}
