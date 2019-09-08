package cn.ycl.study.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class AutowiredBean {
    private String desc;
    /**
     * 注入到字段中
     */
    @Autowired
    private XmlToAnnotationBean bean;

    @Autowired
    private ApplicationContext context;

   // @Autowired() 这样会报错，因为desc
    public AutowiredBean(XmlToAnnotationBean bean,String desc){
        this.bean = bean;
        this.desc = desc;
    }


    /**
     * 注入到构造函数的参数中
     * @param bean
     */
    @Autowired()
    public AutowiredBean(XmlToAnnotationBean bean){
        this.bean = bean;
    }


    /**
     * 自动注入到普通的方法的参数中
     * @param bean
     */
    @Order(2)
    @Autowired
    public void normalMethod(XmlToAnnotationBean bean){
        System.out.println("调用");
    }

    /**
     * 我们发现，调用 context.getBean时， @Autowired 注解的方法都会被提前调用一遍
     * 方法中的参数已经被自动注入了值。我尝试者加个@Order 注解，控制这个调用的顺序，但是实际上没有起到作用。
     * 调用顺序是从上到下来的
     * @param bean
     */
    @Order(1)
    @Autowired(required = false)
    public void normalMethod1(XmlToAnnotationBean bean){
        System.out.println("调用");
    }

    /**
     * 注解用到参数上，但是没有反应
     * @param bean
     * @param desc
     */
    public void diParam(@Autowired XmlToAnnotationBean bean,String desc){
        System.out.println(bean.getClass());
    }
}
