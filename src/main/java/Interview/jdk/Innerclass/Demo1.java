package Interview.jdk.Innerclass;

//匿名内部类也就是没有名字的内部类
//正因为没有名字，所以匿名内部类只能使用一次，它通常用来简化代码编写
//但使用匿名内部类还有个前提条件：必须继承一个父类或实现一个接口

/*
* 不使用匿名内部类来实现抽象方法
* */
abstract class Person1 {
    public abstract void eat();
}

class Child extends Person1 {
    @Override
    public void eat() {
        System.out.println("eat something");
    }
}

public class Demo1 {
    public static void main(String[] args) {
        Person1 p = new Child();
        p.eat();
    }
}