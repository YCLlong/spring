package cn.ycl.study.ioc.annotation;

import cn.ycl.study.ioc.annotation.bean.ComponentBean;
import cn.ycl.study.ioc.annotation.bean.ConfigurationBean;
import cn.ycl.study.ioc.annotation.comonentscanfilter.ConfigApp;
import cn.ycl.study.ioc.annotation.comonentscanfilter.ExcludeBean;
import cn.ycl.study.ioc.annotation.qulifier.QulifierTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public void testAnnotation(ApplicationContext getContext) {
        XmlToAnnotationBean bean = getContext.getBean(XmlToAnnotationBean.class);
        bean.show();
    }

    /**
     * 测试自动注入
     */
    public void autowried() {
        System.out.println("开始执行");
        ApplicationContext context = getContext();
        AutowiredBean bean = context.getBean(AutowiredBean.class);
        bean.normalMethod(null);
    }

    public void primaryTest() {
        ApplicationContext context = getContext();
        PrimaryTest bean = context.getBean(PrimaryTest.class);
        System.out.println(bean.person.getName());
    }

    public void selfQulifier() {
        ApplicationContext context = getContext();
        //如果没有这句，不会触发自动装配
        Object bean = context.getBean(QulifierTest.class);
        System.out.println("11");
    }

    public void other() {
        ApplicationContext context = getContext();
        Object bean = context.getBean(ResourceTest.class);
        System.out.println("11");
    }

    public void componentScanTest() {
        ApplicationContext context = getAnnotationContext();
        context.getBean(ExcludeBean.class);
        System.out.println("结束");
    }

    public void testBean() {
        ApplicationContext context = getContext();

        //测试Component和Configuration中注册bean的区别
        Person person1 = (Person) context.getBean("configurationPeron");
        Person person11 = (Person) context.getBean("configurationPeron");
        System.out.println("@Configuration中注册的bean:" + (person1 == person11));

        Person person2 = (Person) context.getBean("componentPeron");
        Person person22 = (Person) context.getBean("componentPeron");
        System.out.println("@Component中注册的bean:" + (person2 == person22));

        //测试调用方法的区别

        ConfigurationBean configurationBean = context.getBean(ConfigurationBean.class);
        Person person4 = configurationBean.getPersion();
        Person person44 = configurationBean.getPersion();
        System.out.println("@Configuration调用方法的区别：" + (person4 == person44));

        ComponentBean componentBean = context.getBean(ComponentBean.class);
        Person person3 = componentBean.getPersion();
        Person person33 = componentBean.getPersion();
        System.out.println("@Component中调用方法的区别：" + (person3 == person33));
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.testBean();

    }

    public static ApplicationContext getContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-annotation.xml");
        //注册回调钩子，可以保证程序关闭时，能够回调bean的destory方法，从而释放资源
        context.registerShutdownHook();
        return context;
    }

    public static AnnotationConfigApplicationContext getAnnotationContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigApp.class);
        return context;
    }
}
