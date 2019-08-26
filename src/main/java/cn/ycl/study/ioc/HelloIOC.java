package cn.ycl.study.ioc;

public class HelloIOC {
    private String desc;
    /**
     * ioc容器实例化bean时，没有特殊声明的情况下是通过默认的无参构造函数创建实例。
     * 显示申明一下，不然可能被有参构造函数替代
     */
   // public HelloIOC(){}

    public HelloIOC(String desc ) {
        this.desc = desc;
    }

    public void hello(){
        System.out.println("hello ioc");
    }
}
