package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClienthandler implements Runnable{

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    //初始化NIO多路复用器和SocketChannel对象
    public TimeClienthandler(String host,int port){
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try {
            selector=Selector.open();
            socketChannel=SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    @Override
    public void run() {
        try {
            //发送连接请求
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //轮寻多路复用器，有就绪的Channel时执行handleInput（）
        while (!stop){
            try {
                selector.select(10000);
                Set<SelectionKey>selectionKeys=selector.selectedKeys();
                Iterator<SelectionKey>it=selectionKeys.iterator();
                SelectionKey key=null;
                while (it.hasNext()){
                    key=it.next();
                    it.remove();
                    try {
                        handlerInput(key);
                    }catch (Exception e){
                        if (key!=null){
                            key.cancel();
                            if(key.channel()!=null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
//        多路复用器关闭，注册在上面的Channel和Pipe会自动去注册关闭
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //判断SelectionKey状态
    private void handlerInput(SelectionKey key) throws IOException {
        //判断连接是否成功
        if(key.isValid()){
            SocketChannel sc=(SocketChannel) key.channel();
            if(key.isConnectable()){
                //对连接结果判断
                if (sc.finishConnect()) {
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc);
                }else{
                    System.exit(1);
                }
                //
                if(key.isReadable()){
                    ByteBuffer readBuffer=ByteBuffer.allocate(1024);
                    //异步读取请求码流，获得字节数
                    int readBytes=sc.read(readBuffer);
                    if(readBytes>0){
                        //解码
                        readBuffer.flip();
                        byte[]bytes=new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body=new String(bytes,"UTF-8");
                        System.out.println("Now is: "+body);
                        this.stop=true;
                     //返回-1,链路关闭
                    }else if(readBytes<0){
                        key.cancel();
                        sc.close();
                    }

                }
            }
        }
    }

    private void doConnect() throws IOException {
        //如果直接连接成功，注册到多路复用器，发送请求消息，读应答
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        //如果没有连接成功，将SocketChannel注册到多路复用器上，注册。OP_CONNECTION状态，
     //服务端返回TCP  使用syn-ack后。Selector就能轮寻这个SocketChannel
        }else {
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    //构造消息体，编码写到发送缓冲区，调用write（）发送
    private void doWrite(SocketChannel sc) throws IOException {
        byte[]req="QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer=ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        sc.write(writeBuffer);
        //对发送结果判断，是否缓冲区内消息全部发送
        if(!writeBuffer.hasRemaining()){
            System.out.println("Send order 2 server succeed.");
        }
    }
}
