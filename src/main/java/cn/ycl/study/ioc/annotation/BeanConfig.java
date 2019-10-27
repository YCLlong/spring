package cn.ycl.study.ioc.annotation;

import cn.ycl.study.ioc.annotation.qulifier.QulifierTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public QulifierTest q1() {
        return new QulifierTest();
    }

    @Bean
    public Person person1() {
        return new Person("龙哥", 24);
    }

    @Bean
    public Person person2() {
        return new Person("焦妹", 24);
    }

    @Bean
    public PrimaryTest primaryTest() {
        return new PrimaryTest();
    }

    @Bean
    public ResourceTest resource() {
        return new ResourceTest();
    }

}
