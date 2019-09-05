package cn.ycl.study.annotation;

import cn.ycl.study.annotation.qulifier.QulifierTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

    public static void main(String[] args) {
        Main main = new Main();

        main.selfQulifier();

    }

    public static ApplicationContext getContext(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-annotation.xml");
        //注册回调钩子，可以保证程序关闭时，能够回调bean的destory方法，从而释放资源
        context.registerShutdownHook();
        return context;
    }
}
