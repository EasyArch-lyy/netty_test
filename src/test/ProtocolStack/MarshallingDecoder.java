package netty.ProtocolStack;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
/**
* 消息解码工具类
* */
public class MarshallingDecoder {

    private final Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException{
        unmarshaller=MarshallingCodecFactory.buildUnMarshalling();
    }

    protected Object decode(ByteBuf in)throws Exception{
        int objectSize=in.readInt();
        ByteBuf buf=in.slice(in.readerIndex(),objectSize);
        ByteInput input=new ChannelBufferByteInput(buf);
        try {
            unmarshaller.start(input);
            Object obj=unmarshaller.readObject();
            unmarshaller.finish();
            in.readerIndex(in.readerIndex()+objectSize);
            return obj;
        }finally {
            unmarshaller.close();
        }
    }
}
