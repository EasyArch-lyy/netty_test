package BIO.synchronization;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
* 同步阻塞io TimeServer源码
* */

//TimeServer根据传入参数设置监听端口，没入参默认8080。29行通过构造函数创建ServerSocket端口未占用服务端监听成功。没有客户端接入时阻塞在accept
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port=8080;
        if(args!=null&&args.length>0){
            port=Integer.valueOf(args[0]);
        }
        ServerSocket server=null;
        try {
            server=new ServerSocket(port);
            System.out.println("The time server is start in port: "+port);
            Socket socket=null;
            while (true){
                socket=server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(server!=null){
                System.out.println("The time server close");
                server.close();
                server=null;
            }
        }
    }
}
