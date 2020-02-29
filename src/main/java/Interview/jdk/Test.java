package Interview.jdk;

public class Test {

    public  void a(){
        final int b = 1;
        System.out.println("---------");
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("xxxx");
                System.out.println(b);
                System.out.println("1111111");
            }
        }).start();
        System.out.println("aaaa");
    }
    public static void main(String[] args) {
        new Test().a();
    }
}
