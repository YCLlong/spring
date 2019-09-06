package cn.ycl.study.annotation;

import cn.ycl.study.annotation.comonentscanfilter.ConfigApp;
import cn.ycl.study.annotation.comonentscanfilter.ExcludeBean;
import cn.ycl.study.annotation.qulifier.QulifierTest;
import cn.ycl.study.ioc.bean.Config;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

public class Main {
    public void testAnnotation( ApplicationContext getContext ){
        XmlToAnnotationBean bean = getContext.getBean(XmlToAnnotationBean.class);
        bean.show();
    }

    /**
     * 测试自动注入
     */
    public void autowried(){
        System.out.println("开始执行");
        ApplicationContext context = getContext();
        AutowiredBean bean = context.getBean(AutowiredBean.class);
        bean.normalMethod(null);
    }

    public void primaryTest(){
        ApplicationContext context = getContext();
        PrimaryTest bean = context.getBean(PrimaryTest.class);
        System.out.println(bean.person.getName());
    }

    public void selfQulifier(){
        ApplicationContext context = getContext();
        //如果没有这句，不会触发自动装配
        Object bean = context.getBean(QulifierTest.class);
        System.out.println("11");
    }

    public void other(){
        ApplicationContext context = getContext();
        Object bean = context.getBean(ResourceTest.class);
        System.out.println("11");
    }

    public void componentScanTest(){
        ApplicationContext context = getAnnotationContext();
        context.getBean(ExcludeBean.class);
        System.out.println("结束");
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.componentScanTest();

    }

    public static ApplicationContext getContext(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-annotation.xml");
        //注册回调钩子，可以保证程序关闭时，能够回调bean的destory方法，从而释放资源
        context.registerShutdownHook();
        return context;
    }

    public static AnnotationConfigApplicationContext getAnnotationContext(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigApp.class);
        return context;
    }
}
