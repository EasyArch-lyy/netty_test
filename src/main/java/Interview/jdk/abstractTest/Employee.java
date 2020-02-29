package Interview.jdk.abstractTest;

public abstract class Employee {
    //3个成员变量
    private String name;
    private String address;
    private int number;
    //1个构造方法
    public Employee(String name, String address, int number) {
        System.out.println("Constructing an Employee");
        this.name = name;
        this.address = address;
        this.number = number;
    }
    //7个成员方法
    public double computePay() {
        System.out.println("Inside Employee computePay");
        return 0.0;
    }
    public void mailCheck() {
        System.out.println("Mailing a check to " + this.name
                + " " + this.address);
    }
    @Override
    public String toString() {
        return name + " " + address + " " + number;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String newAddress) {
        address = newAddress;
    }
    public int getNumber() {
        return number;
    }
}
