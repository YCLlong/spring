package cn.ycl.study.ioc.javaconfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private void testContextMethod() {
        AnnotationConfigApplicationContext context = getAnnotationContext();
        //register方法，可以指定配置类注册
        context.register();

        //scan方法可以配置包路径进行扫描
        context.scan();

        //刷新ioc容器，更新配置
        context.refresh();
    }

    public static void main(String[] args) {

    }

    /**
     * 通过java配置创建ioc容器实例
     *
     * @return
     */
    public static AnnotationConfigApplicationContext getAnnotationContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        return context;
    }
}
