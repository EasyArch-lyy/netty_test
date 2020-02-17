package sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Test1 {
    public static void main(String[] args) {

        //Iterator使用
        Collection coll = new ArrayList();
        coll.add("rrr");
        coll.add("eee");
        coll.add("qqq");
        coll.add("www");
        Iterator iter = coll.iterator();

        while(iter.hasNext()){

//            Object obj= iter.next();
//            System.out.println("obj.toString"+obj.toString());
            String str =(String)iter.next();
            System.out.println(str);

        }
    }
}
