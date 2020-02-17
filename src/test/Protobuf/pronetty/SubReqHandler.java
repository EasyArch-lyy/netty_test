package Protobuf.pronetty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import netty.Seria.nettySer.SubscribeResp;

@ChannelHandler.Sharable
public class SubReqHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        SubscribeReqProto.SubscribeReq req=(SubscribeReqProto.SubscribeReq)msg;
        if("Gracey".equalsIgnoreCase(req.getUserName())){
            System.out.println("Service accept client sunscribe req: ["+req.toString()+"]");
            ctx.writeAndFlush(resp(req.getSubReqUD()));
        }
    }
    private SubscribeRespProto.SubscribeResp resp(int subReqID){
        SubscribeRespProto.SubscribeResp.Builder builder SubscribeResp
    }
}
