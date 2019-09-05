package cn.ycl.study.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public void testAnnotation( ApplicationContext getContext ){
        XmlToAnnotationBean bean = getContext.getBean(XmlToAnnotationBean.class);
        bean.show();
    }
    public static void main(String[] args) {
        Main main = new Main();
        ApplicationContext getContext = getContext();
        main.testAnnotation(getContext);
        //新建注解分支

    }

    public static ApplicationContext getContext(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sping-context.xml");
        //注册回调钩子，可以保证程序关闭时，能够回调bean的destory方法，从而释放资源
        context.registerShutdownHook();
        return context;
    }
}
