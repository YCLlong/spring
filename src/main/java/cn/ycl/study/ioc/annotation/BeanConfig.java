package cn.ycl.study.ioc.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public cn.ycl.study.annotation.qulifier.QulifierTest q1(){
        return new cn.ycl.study.annotation.qulifier.QulifierTest();
    }

    @Bean
    public cn.ycl.study.annotation.Person person1(){
        return new cn.ycl.study.annotation.Person("龙哥",24);
    }

    @Bean
    public cn.ycl.study.annotation.Person person2(){
        return new cn.ycl.study.annotation.Person("焦妹",24);
    }

    @Bean
    public PrimaryTest primaryTest(){
        return new PrimaryTest();
    }

    @Bean
    public ResourceTest resource(){
        return new ResourceTest();
    }

}
