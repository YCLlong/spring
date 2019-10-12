package cn.ycl.study.ioc.annotation.bean;

import cn.ycl.study.ioc.annotation.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationBean {
    @Bean("configurationPeron")
    public Person getPersion(){
        return new Person("在configuration中注册bean",1);
    }
}
