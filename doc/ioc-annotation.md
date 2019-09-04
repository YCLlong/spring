# 基于注解配置简介
基于注释的配置的引入引发了这种方法是否比XML更好吗？这个问题取决于个人。Spring兼容这两种配置方式。
基于注释的配置提供了XML设置的替代方案，该配置依赖于字节码元数据来连接组件而不是角括号声明。开发人员不是使用XML来描述bean连接，而是通过在相关的类，方法或字段声明上使用注释将配置移动到组件类本身

# 如何在基于XML配置的项目中支持基于注解的配置
我们需要注册一些注解解析器，这些解析器会扫描类上的注解从而进行相关的配置。

需要加入 命名空间 xmlns:p="http://www.springframework.org/schema/p" xmlns:context="http://www.springframework.org/schema/context"

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
            https://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context
            https://www.springframework.org/schema/context/spring-context.xsd">
    
        <context:component-scan base-package="cn.ycl.study"/>
    </beans>

## \<context:annotation-config/>    
    < context:annotation-config> 是用于激活那些已经在spring容器里注册过的bean上面的注解，也就是显示的向Spring注册
    
    AutowiredAnnotationBeanPostProcessor
    CommonAnnotationBeanPostProcessor
    PersistenceAnnotationBeanPostProcessor
    RequiredAnnotationBeanPostProcessor
    这四个Processor，注册这4个BeanPostProcessor的作用，就是为了你的系统能够识别相应的注解。BeanPostProcessor就是处理注解的处理器。
    
    比如我们要使用@Autowired注解，那么就必须事先在 Spring 容器中声明 AutowiredAnnotationBeanPostProcessor Bean。传统声明方式如下
    <bean class="org.springframework.beans.factory.annotation. AutowiredAnnotationBeanPostProcessor "/>
    如果想使用@ Resource 、@ PostConstruct、@ PreDestroy等注解就必须声明CommonAnnotationBeanPostProcessor。传统声明方式如下
    <bean class="org.springframework.beans.factory.annotation. CommonAnnotationBeanPostProcessor"/> 
    如果想使用@PersistenceContext注解，就必须声明PersistenceAnnotationBeanPostProcessor的Bean。
    <bean class="org.springframework.beans.factory.annotation.PersistenceAnnotationBeanPostProcessor"/> 
    如果想使用 @Required的注解，就必须声明RequiredAnnotationBeanPostProcessor的Bean。
    同样，传统的声明方式如下：
    
    <bean class="org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor"/> 
    一般来说，像@ Resource 、@ PostConstruct、@Antowired这些注解在自动注入还是比较常用，所以如果总是需要按照传统的方式一条一条配置显得有些繁琐和没有必要，于是spring给我们提供< context:annotation-config/>的简化配置方式，自动帮你完成声明。
    
    思考1：假如我们要使用如@Component、@Controller、@Service等这些注解，使用能否激活这些注解呢?
    
    答案：单纯使用< context:annotation-config/>对上面这些注解无效，不能激活！
    

    
## \<context:component-scan base-package="cn.ycl.study"/>
    这个注解会导入更多的注解解析器，包括上面的4个解析器，加入这个注解之后 \<context:annotation-config/> 就不需要了。
    因为< context:annotation-config />和 < context:component-scan>同时存在的时候，前者会被忽略。

    
    
    

