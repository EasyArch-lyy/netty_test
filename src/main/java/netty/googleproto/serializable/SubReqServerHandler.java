package netty.googleproto.serializable;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import netty.googleproto.protobuf.SubscribeReqProto;
import netty.googleproto.protobuf.SubscribeRespProto;

/**
* 订购服务处理类
* */
@ChannelHandler.Sharable
public class SubReqServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        if ("Gracey".equalsIgnoreCase(req.getUserName())) {
            System.out.println("Service accept client subscribe req: ["+req.toString()+"]");
            ctx.writeAndFlush(resp(req.getSubReqID()));

        }
    }

    private SubscribeRespProto.SubscribeResp resp(int subReqID){
        SubscribeRespProto.SubscribeResp.Builder builder=SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqID(subReqID);
        builder.setRespCode(0);
        builder.setDesc("Netty book order succeed, 3 day later, sent to the designated address");
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }

}
