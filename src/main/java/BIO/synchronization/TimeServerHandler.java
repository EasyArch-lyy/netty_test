package BIO.synchronization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;

//TimeServerHandler，为构造函数的参数创建新客户端线程处理Socket链路
public class TimeServerHandler implements Runnable{
    private Socket socket;

    public TimeServerHandler(Socket socket){
        this.socket=socket;
    }

    public void run() {
        BufferedReader in =null;
        PrintWriter out=null;
        try {
            in=new BufferedReader(new InputStreamReader(
                    this.socket.getInputStream()));
            out=new PrintWriter(this.socket.getOutputStream(),true);
            String currentTime=null;
            String body=null;
            while (true){
                body=in.readLine();
                if(body==null){
                    break;
                }
                System.out.println("The time server receive order: "+body);
                currentTime="QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
                        System.currentTimeMillis()).toString():"BAD ORFER";
                        out.println(currentTime);
            }
        } catch (IOException e) {
            if (in!=null){
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (out!=null){
                out.close();
                out=null;
            }
            if(this.socket!=null){
                try {
                    this.socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                this.socket=null;
            }
            e.printStackTrace();
        }
    }

}
