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


# @Autowired
这个注解的英文意思是【自动装配】,它的功能就和名字一样，提供了自动装配的功能，这个注解功能非常的强大，注解的源码如下。
通过源码，我们发现这个注解可以用到构造函数、方法、属性、参数之上。

    @Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Autowired {
    
    	/**
    	 * Declares whether the annotated dependency is required.
    	 * <p>Defaults to {@code true}.
    	 */
    	boolean required() default true;
    
    }

## @Autowired的值required
我们发现required默认值是true，表示强制需要的意思，如果自动装配的过程中ioc容器中不存在这个类型的值就会直接抛出异常。
当这个值是false是标表示并不是强制需要的，如果ioc容器中不存在值就会忽略，也就是并不会装配。

## 实例

注解直接用于字段，直接注入属性
用于方法，注解解析器会利用反射技术调用这个方法，并将参数注入进入。前提是所有的参数都能在ioc容器中找到合适类型

 
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
        @Autowired
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
    ｝
    
    
    //调用入口
    public void autowried(){
        System.out.println("开始执行");
        ApplicationContext context = getContext();
        AutowiredBean bean = context.getBean(AutowiredBean.class);
        bean.normalMethod(null);
    }
    
    
## 发现
我们惊奇的发现，当执行到getContext();这行代码时，实例中的normalMethod和normalMethod1这两个方法就被调用了。

调用时，参数已经被成功注入了。我猜想，
>当Spring IOC容器实例化时，就会读取类中的注解的值，其实就是在读取配置元数据。当碰到@Autowired这个注解时，就交给注解解析器AutowiredAnnotationBeanPostProcessor去解析。注解解析器利用反射技术，直接调用类中的方法，并注入参数。
