package netty.TimeServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.NewTimeServer.TimeServerHandler;

public class TimeServer {
    public void bind(int port)throws Exception{
        //配置服务端NIO线程组
        //一个用于服务端接受客户端连接，一个进行SocketChannel网络读写
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();
        try {
            //Netty启动nio的辅助启动类。降低复杂度
            ServerBootstrap b = new ServerBootstrap();
            //将两个NIO线程组当参数传递到ServerBootstrap
            b.group(bossGroup, workGroup)
                    //设置创建的Channel为NioServerSocketChannel，功能对应ServerSocketChannel类
                    .channel(NioServerSocketChannel.class)
                    //设置TCP参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //绑定io事件处理类，类似Reactor模式中handler类，处理网络io
                    .childHandler(new ChildChannelHandler());
            //绑定端口监听，同步等待成功
            //.sync调用同步阻塞等待操作成功，完成返回ChannelFuture。异步操作通知回调
            ChannelFuture f = b.bind(port).sync();

            //阻塞等待服务端监听端口关闭，等待服务端链路关闭再退出main函数
            f.channel().closeFuture().sync();
        } finally {
            //优雅退出
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel arg0) throws Exception {
            arg0.pipeline().addLast(new TimeServerHandler());
        }
    }

    public static void main(String[] args) throws Exception {
        int port =8080;
        if(args!=null&&args.length>0){
            port=Integer.valueOf(args[0]);
        }
        new TimeServer().bind(port);
    }
}
