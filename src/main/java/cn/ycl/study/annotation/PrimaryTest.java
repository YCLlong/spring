package cn.ycl.study.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PrimaryTest {
    @Autowired
    //指定person2装配
    @Qualifier("person2")
    public Person person;
}
