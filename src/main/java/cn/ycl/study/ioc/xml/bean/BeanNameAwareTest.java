package cn.ycl.study.ioc.xml.bean;

import org.springframework.beans.factory.BeanNameAware;

public class BeanNameAwareTest implements BeanNameAware {
    public void setBeanName(String name) {
        System.out.println(name);
    }
}
