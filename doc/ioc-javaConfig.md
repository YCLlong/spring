# IOC容器之java配置
Spring的配置支持主要的目标是@Configuration 注解的类 和 @Bean注解的方法。

# java配置实例化ioc容器
使用xml配置文件描述bean的信息时，实例化Ioc容器需要通过XmlClassPathApplicationContext
加载xml;配置文件的路径实例化。那么通过java类如何实例化容器呢？

创建配置类
    
    @Configuration
    @ComponentScan
    public class ApplicationConfig {
    }

通过AnnotationConfigApplicationContext 指定配置类实例化ioc容器

    public static AnnotationConfigApplicationContext getAnnotationContext(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigApp.class);
        return context;
    }
    
由于创建ioc容器时指定了ApplicationConfig这个类，程序会解析这个类上的注解，解析到@ComponentScan注解时就会去扫描其他配置，将那些配置加载到ioc容器中。

## AnnotationConfigApplicationContext 对象编程式的方法配置
    
    private void testContextMethod(){
        AnnotationConfigApplicationContext context = getAnnotationContext();
        //register方法，可以指定配置类注册
        context.register();

        //scan方法可以配置包路径进行扫描
        context.scan();

        //刷新ioc容器，更新配置
        context.refresh();
    }

# Web应用程序支持
web.xml中配置

    <web-app>
        <!-- Configure ContextLoaderListener to use AnnotationConfigWebApplicationContext
            instead of the default XmlWebApplicationContext -->
        <context-param>
            <param-name>contextClass</param-name>
            <param-value>
                org.springframework.web.context.support.AnnotationConfigWebApplicationContext
            </param-value>
        </context-param>
    
        <!-- Configuration locations must consist of one or more comma- or space-delimited
            fully-qualified @Configuration classes. Fully-qualified packages may also be
            specified for component-scanning -->
        <context-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>com.acme.AppConfig</param-value>
        </context-param>
    
        <!-- Bootstrap the root application context as usual using ContextLoaderListener -->
        <listener>
            <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>
    
        <!-- Declare a Spring MVC DispatcherServlet as usual -->
        <servlet>
            <servlet-name>dispatcher</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <!-- Configure DispatcherServlet to use AnnotationConfigWebApplicationContext
                instead of the default XmlWebApplicationContext -->
            <init-param>
                <param-name>contextClass</param-name>
                <param-value>
                    org.springframework.web.context.support.AnnotationConfigWebApplicationContext
                </param-value>
            </init-param>
            <!-- Again, config locations must consist of one or more comma- or space-delimited
                and fully-qualified @Configuration classes -->
            <init-param>
                <param-name>contextConfigLocation</param-name>
                <param-value>com.acme.web.MvcConfig</param-value>
            </init-param>
        </servlet>
    
        <!-- map all requests for /app/* to the dispatcher servlet -->
        <servlet-mapping>
            <servlet-name>dispatcher</servlet-name>
            <url-pattern>/app/*</url-pattern>
        </servlet-mapping>
    </web-app>

在配置文件中已经指定了实例化ioc容器的相关java配置类，我们可以通过ApplicationAware接口获取到ioc容器对象。
但是对于spring提供的这个配置，有部分还没有理解。

> 在/<context-param>标签中指定了属性contextConfigLocation为一个java配置类，但是在/<servlet>标签中的/<init-param>标签中需要再次配置contextConfigLocation
