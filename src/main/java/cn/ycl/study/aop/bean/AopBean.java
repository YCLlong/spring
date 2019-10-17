package cn.ycl.study.aop.bean;

import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AopBean {
    private String desc;

    public void sayHello(){
        System.out.println("hello world");
    }
}
