package netty.Seria.nettySer;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqClientHandler extends ChannelHandlerAdapter {

    public SubReqClientHandler(){

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        //链路激活时构造十条订购消息，一次性发给服务端
        for (int i = 0; i < 10; i++) {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    private SubscribeReq subReq(int i){
        SubscribeReq req=new SubscribeReq();
        req.setAddress("天津象博豪庭");
        req.setPhoneNumber("18835151581");
        req.setProductName("Netty权威指南");
        req.setSubReqID(i);
        req.setUserName("Gracey");
        return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        System.out.println("Receive server response:["+msg+ "]");

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
