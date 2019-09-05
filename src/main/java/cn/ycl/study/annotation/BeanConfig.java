package cn.ycl.study.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanConfig {
    @Bean
    public Person person1(){
        return new Person("龙哥",24);
    }

    @Bean
    public Person person2(){
        return new Person("焦妹",24);
    }

    @Bean
    public PrimaryTest primaryTest(){
        return new PrimaryTest();
    }
}
