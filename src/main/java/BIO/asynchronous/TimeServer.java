package BIO.asynchronous;

import BIO.synchronization.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//伪异步
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
            TimeServerHandlerExcutePool singleExcutor=new TimeServerHandlerExcutePool(
                    50,10000
            );
            while (true){
                socket=server.accept();
                singleExcutor.excute(new TimeServerHandler(socket));
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
