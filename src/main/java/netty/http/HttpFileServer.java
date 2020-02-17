package netty.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.awt.*;

public class HttpFileServer {
    private static final String DEFAULT_URL ="/home/lyy";

    public void run(final int port,final String url)throws Exception{
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //向ChannelPipeline添加HTTP请求消息解码器
                            channel.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            //添加HttpObjectAggregatior解码器，作用：将多个消息转换为单一的FullHttpRequest或FullHttpResponse,
                            channel.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            //HTTP响应编码器，对HTTP响应消息编码
                            channel.pipeline().addLast("http-encode",new HttpResponseEncoder());
                            //Chunked handler，支持异步发送大的码流，不占过多内存
                            channel.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            //业务处理
                            channel.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
                        }
                    });
            ChannelFuture future=b.bind("127.0.0.1",port).sync();
            System.out.println("HTTP文件目录服务器启动，网址是： "+"http://127.0.0.1:"+port+url);
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

        }
    }

    public static void main(String[] args) throws Exception {
        int port=8080;
        if(args!=null&&args.length>0){
            try {
                port = Integer.valueOf(args[0]);

            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        String url=DEFAULT_URL;
        if(args.length>1){
            url=args[1];
        }
        //url未配置时启动默认配置
        new HttpFileServer().run(port,url);

    }
}
