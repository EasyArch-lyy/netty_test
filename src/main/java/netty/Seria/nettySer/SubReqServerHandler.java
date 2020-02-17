package netty.Seria.nettySer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import netty.Seria.nettySer.SubscribeReq;
import io.netty.channel.ChannelHandlerContext;
/**
* 订购服务处理类
* */
@ChannelHandler.Sharable
public class SubReqServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        SubscribeReq req = (SubscribeReq) msg;
        if ("Gracey".equalsIgnoreCase(req.getUserName())) {
            System.out.println("Service accept client subscribe req: ["+req.toStirng()+"]");
            ctx.writeAndFlush(resp(req.getSubReqID()));

        }
    }

    private SubscribeResp resp(int subReqID){
        SubscribeResp resp=new SubscribeResp();
        resp.setSubReqID(subReqID);
        resp.setRespCode(0);
        resp.setDesc("Netty book order succeed, 3 day later, sent to the designated address");
        return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }

}
