package cn.ycl.study.aop.bean;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopBean {
    private long t;

    @Pointcut("execution(public void cn.ycl.study.aop.bean.TestAopTarget.sayHello())")
    public void say(){
    }

    @Before("say()")
    public void before(){
        t = System.currentTimeMillis();
        System.out.println("before:" + t);
    }

    @After("say()")
    public void after(){
        Long end = System.currentTimeMillis();
        t = end - t;
        System.out.println("after,end =" + end + "间隔：" + t);
    }

    @AfterThrowing("say()")
    public void  afterError(){

    }
}
