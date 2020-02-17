package netty.UDP;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;

/*
*                  UDP服务端处理流程
* UDP Client1 -->                          Pope Line
* UDP Client2 -->  DatagramPackage  ----> Server Handler
* UDP Client3 -->
*
* */

public class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final String[]DICTIONARY={"只要功夫深，铁棒磨成针。","旧时王谢堂前燕，飞入寻常百姓家。","洛阳亲友如相问，一片冰心在玉壶。"};

    private String nextQuote(){
        int quoteId= ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        //将packet内容转换为字符串
        String req=packet.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);
        //对消息判断成功则构造应答消息返回
        if("谚语字典查询？".equals(req)){
            //DatagramPacket参数（需要发送的内容ByteBuf，目的地址IP和端口）
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    "谚语查询结果："+nextQuote(),CharsetUtil.UTF_8),packet.sender()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception{
        ctx.close();
        cause.printStackTrace();

    }
}
