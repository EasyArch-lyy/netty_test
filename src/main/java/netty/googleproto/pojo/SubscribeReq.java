package netty.googleproto.pojo;

import java.io.Serializable;

/**
* 订购请求pojo类
* */
public class SubscribeReq implements Serializable {

    private static final long serialVersionUID=1L;
    private int subReqID;
    private String userName;
    private String productName;
    private String phoneNumber;
    private String address;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toStirng(){
        return "netty.protobuf.SubscribeReq[subReqID="+subReqID+",userName="+userName+",productName="+productName+",phoneNumber="+phoneNumber+",address="+address+"]";
    }

}
