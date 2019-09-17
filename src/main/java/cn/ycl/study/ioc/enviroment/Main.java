package cn.ycl.study.ioc.enviroment;

import cn.ycl.study.ioc.javaconfig.ApplicationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.Map;


public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = getAnnotationContext();
        Environment environment = context.getEnvironment();
        MutablePropertySources resource = ((ConfigurableEnvironment) environment).getPropertySources();
        Map<String, Object> sysEnviroment = ((ConfigurableEnvironment) environment).getSystemEnvironment();
        Map<String, Object> sysProperties = ((ConfigurableEnvironment) environment).getSystemProperties();
        System.out.println("end");
    }


    /**
     * 通过java配置创建ioc容器实例
     * @return
     */
    public static AnnotationConfigApplicationContext getAnnotationContext(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        return context;
    }
}
