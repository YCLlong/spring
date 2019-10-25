package cn.ycl.study.aop;

import cn.ycl.study.aop.bean.AopBean;
import cn.ycl.study.aop.bean.TestAopTarget;
import cn.ycl.study.ioc.annotation.comonentscanfilter.ConfigApp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan
@EnableAspectJAutoProxy

public class AnnotationAop {
    public static AnnotationConfigApplicationContext getAnnotationContext(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AnnotationAop.class);
        return context;
    }

    public static void main(String[] args) {
        ApplicationContext context = getAnnotationContext();
        TestAopTarget target = context.getBean(TestAopTarget.class);
        target.sayHello();
    }
}
