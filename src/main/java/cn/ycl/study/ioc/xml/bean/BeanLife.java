package cn.ycl.study.ioc.xml.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


public class BeanLife implements BeanPostProcessor {

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


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("==============每一个对象在初始化之前调用这个================" + beanName + "===" + bean.getClass());
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("==============每一个对象初始化之后调用这个================" + beanName + "===" + bean.getClass());
        return bean;
    }
}
