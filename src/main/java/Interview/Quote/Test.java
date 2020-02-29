package Interview.Quote;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String str=new String("xxx");
        //创建引用队列
        ReferenceQueue rq=new ReferenceQueue();
        //创建虚引用，引用字符串
        PhantomReference pr=new PhantomReference(str,rq);
        System.out.println(pr.get());
        str=null;
        System.out.println(pr.get());
        System.gc();
        //强制调用已经失去引用的对象的finalize方法
        //常见的作用是确保释放实例占用的全部资源。java并不保证定时为对象实例调用该方法，甚至不保证方法会被调用，所以该方法不应该用于正常内存处理。
        System.runFinalization();
        //取出队列中放入的元素和pr比较
        System.out.println(rq.poll()==pr);
        System.out.println(pr.get());
    }
}
//
////软引用
//class TestOOM{
//    private static List<Object>list=new ArrayList<Object>(;)
//
//    //弱引用
//    private static void tetWeakReference(){
//        for(int i=0;i<10;i++){
//            byte[]buff=new byte[1024*1024];
//            WeakReference<byte[]>sr=new WeakReference<byte[]>(buff);
//            list.add();
//        }
//        System.gc();
//    }
//    public static void main(String[] args) {
//
//    }
//}
