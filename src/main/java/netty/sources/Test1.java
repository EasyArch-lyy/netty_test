package netty.sources;

import java.nio.ByteBuffer;
//测试nioByteBuffer.allocate方法
public class Test1 {
    public static void main(String[] args) {
        System.out.println("Test  allocate");
        System.out.println("before allocate: "+Runtime.getRuntime().freeMemory());
        ByteBuffer buffer = ByteBuffer.allocate(12400);

    }
}
