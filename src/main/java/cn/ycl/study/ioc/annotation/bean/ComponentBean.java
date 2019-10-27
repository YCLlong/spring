package cn.ycl.study.ioc.annotation.bean;

import cn.ycl.study.ioc.annotation.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ComponentBean {
    @Bean("componentPeron")
    public Person getPersion() {
        return new Person("在component中注册的bean", 0);
    }
}
