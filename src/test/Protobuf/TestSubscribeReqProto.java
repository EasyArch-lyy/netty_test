package Protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class TestSubscribeReqProto {
    private static byte[]encode(SubscribeReqProto.SubscribeReq req){
        //toByteArray将SubscribeReq编码为byte数组
        return req.toByteArray();
    }

    private static SubscribeReproto.SubscribeReq decode(byte[]body)throws InvalidProtocolBufferException {
        //解码
        return TestSubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubcribeReq createSubscribeReq(){
        //通过Builder构建器对SubscribeReq的属性设置
        SubscribeReqProto.SubscribeReq.Builder builder=SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubilder();
        builder.setUserName();
        builder.setProductName("Netty Book");
        List<String>address=new ArrayList<>();
        address.add("NanJing YuHuaTai");
        address.add("BeiJing LiuLiChang");
        address.add("ShenZhen HongShuLin");
        //设置集合类型
        builder.addAllAddress(address);
        return builder.build();
    }

    public static void main(String[] args) throws InvalidPropertiesFormatException {
        SubscribeReqProto.SubscribeReq req=createSubscribeReq();
        System.out.println("Before encode: "+req.toString());
        SubscribeProto.SubscribeReq req2=decode(encode(req));
        System.out.println("After decode : "+req.toString());
        System.out.println("Assert equal: -->"+req2.equals(req));
    }
}
