package cn.ycl.study.ioc.annotation.qulifier;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
//加上这个注解Spring的@Qualifier注解解析器也能解析我们的注解
@Qualifier
public @interface Offline {
    //我们可以自定义属性,不加上 default，就是必须的参数
    String name();
    int level() default 0;
}
