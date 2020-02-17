package netty.ProtocolStack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.awt.*;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelHandlerAdapter {
    private Map<String,Boolean> npdeCheck=new ConcurrentHashMap<String,Boolean>();
    private String[]whiteList={"127.0.0.1","192.168.1.104"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == TrayIcon.MessageType.LOGON_REQ.value()) {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp = null;
            //判断重复登录
            if (npdeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for (String WIP : whiteList) {
                    if (WIP.equals(ip)) {
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);
                if (isOK) {
                    npdeCheck.put(nodeIndex, true);
                }
            }
                System.out.println("The login response is: " + loginResp + "body[" + loginResp.getBody() + "]");
                ctx.writeAndFlush(loginResp);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildResponse(byte result){
        NettyMessage message=new NettyMessage();
        Header heade=new Header();
        heade.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(heade);
        message.setBody(result);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        npdeCheck.remove(ctx.channel().remoteAddress().toString());//删除缓存
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
