package netty.Seria.nettySer;

import com.sun.security.ntlm.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
/**
* 订购主函数
* */
public class SubReqServer {

    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();
        ServerBootstrap b=new ServerBootstrap();
        b.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        //创建ObjectDecoder，实现Serizlizable的pojo对象解码，使用weakCachingConcurrentResolver创建线程安全的WeakReferenceMap对类加载器缓存，支持多线程并发访问
                        channel.pipeline().addLast(new ObjectDecoder(1024*1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        //在消息发送时自动将序列化对象编码
                        channel.pipeline().addLast(new ObjectEncoder());
                        //处理业务代码
                        channel.pipeline().addLast(new SubReqServerHandler());
                    }
                });
        ChannelFuture f=b.bind(port).sync();
        f.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        int port =8080;
        if (args!=null&&args.length>0) {
            port=Integer.valueOf(args[0]);
        }
        new SubReqServer().bind(port);
    }
}
