package netty.Seria;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class UserInfo implements Serializable {
    private String userName;
    private static final long serialVersionUID=1L;
    private int userID;

    public UserInfo buildUserName(String userName){
        this.userName=userName;
        return this;
    }

    public UserInfo buildUserID(int userID){
        this.userID=userID;
        return this;
    }

    public final String getUserName(){
        return userName;
    }

    public final int userID(){
        return userID;
    }

    public final void setUserID(int userID){
        this.userID=userID;
    }

    public byte[]codeC(){
        //二进制解码对对象编码，编码结果是byte数组
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        byte[]value=this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.flip();
        value=null;
        byte[]result=new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }

    public byte[]codeC(ByteBuffer buffer){
        buffer.clear();
        byte[]value=this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userID);
        buffer.flip();
        value=null;
        byte[]result=new byte[buffer.remaining()];
        return result;
    }
}
