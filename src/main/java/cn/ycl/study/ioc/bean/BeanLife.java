package cn.ycl.study.ioc.bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


public class BeanLife {

    @PostConstruct
    public void test(){
        System.out.println("BeanLife test");
    }

    @PreDestroy
    private void initBean(){
        System.out.println("默认初始化bean的方法被调用");
    }

    private void destroyBean(){
        System.out.println("默认初始化销毁bean的方法被调用");
    }
}
