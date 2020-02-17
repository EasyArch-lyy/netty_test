package netty.NewTimeServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

    public void connect(int port,String host) throws Exception {
        //创建客户端处理io读写NioEventLoopGroup线程组
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            //创建客户端辅助启动类
            Bootstrap b=new Bootstrap();
            //设置NioSocketChannel，添加handler
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    //作用：创建NioSocketChannel成功后初始化时将ChannelHandler设置到ChannelPipeLine，处理网络io
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            //调用connect发起异步连接操作，同步方法等待连接成功
            ChannelFuture f=b.connect(host,port).sync();
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();

        }

    }

    public static void main(String[] args) throws Exception {
        int port=8080;
        if(args!=null&&args.length>0){
            port=Integer.valueOf(args[0]);
        }
        new TimeClient().connect(port,"127.0.0.1");
    }
}
