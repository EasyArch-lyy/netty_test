package netty.UDP;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class ChineseProverbServer {
    public void run(int port)throws Exception{
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            Bootstrap b=new Bootstrap();
            //创建Channel通过NioDatagramChannel创建
            b.group(group).channel(NioDatagramChannel.class)
                    //设置Socket参数支持广播
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new ChineseProverbServerHandler());
            b.bind(port).sync().channel().closeFuture().await();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port=8080;
        if(args!=null&&args.length>0){
            port=Integer.valueOf(args[0]);
        }
        new ChineseProverbServer().run(port);
    }
}
