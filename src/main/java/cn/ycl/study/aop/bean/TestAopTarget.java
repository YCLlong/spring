package cn.ycl.study.aop.bean;

import org.springframework.stereotype.Component;

@Component
public class TestAopTarget {
    //现有的类的方法，需要增强这个方法，但是不修改这个源码
    public void sayHello(){
        System.out.println("hello world");
    }
}
