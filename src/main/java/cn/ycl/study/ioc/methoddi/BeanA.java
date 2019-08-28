package cn.ycl.study.ioc.methoddi;

public abstract class BeanA{
    private String desc;

    //定义为抽象方法
    public abstract BeanB getBeanB();
}
