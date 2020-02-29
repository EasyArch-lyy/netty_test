package Interview.jdk.Innerclass;
/*
* 匿名内部类的基本实现
* */
abstract class Person {
    public abstract void eat();
}

public class Demo2 {
    public static void main(String[] args) {
        Person p = new Person() {
            @Override
            public void eat() {
                System.out.println("eat something");
            }
        };
        p.eat();
    }
}