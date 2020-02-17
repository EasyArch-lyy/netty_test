package netty.googleproto;

import com.google.protobuf.InvalidProtocolBufferException;
import netty.googleproto.protobuf.SubscribeReqProto;

import java.util.ArrayList;
import java.util.List;

public class TestSubscribeReqProto {
    private static byte[]encode(SubscribeReqProto.SubscribeReq req){
        //将SubscribeReq编码转化为byte数组
        return req.toByteArray();
    }
    private static SubscribeReqProto.SubscribeReq decode(byte[]body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);

    }
    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
        //通过SubbcribeReqProto.SubscribeReq静态方法newBuilder
        SubscribeReqProto.SubscribeReq.Builder builder=SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("Gracy");
        builder.setProduceName("Netty Book");
        List<String>address=new ArrayList<String>();
        address.add("NanJing YuHua");
        address.add("BeiJing LiuLiChang");
        address.add("ShenZhen HongShuLin");
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req=createSubscribeReq();
        System.out.println("Before encode:" +req.toString());
        SubscribeReqProto.SubscribeReq req2=decode(encode(req));
        System.out.println("After decode:"+req.toString());
        System.out.println("Assert equal:-->"+req2.equals(req));
    }
}
