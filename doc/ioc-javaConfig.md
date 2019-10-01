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


# @Bean和@Configuration注解的使用
java配置的方式这两个注解非常的常用。@Bean是方法级别的注释，它注释的方法返回的对象会被注册到IOC容器中去，@Configuration注解用在类的级别上，被注册的类表示是一个配置类。@Confifuration注解行有@Component注解，会被扫描到。
一般@Bean注解的方法都是在用@Configuration注解的类中或者@COmpoment注解的类中，才能被识别成Bean.
> 用@COmponent或者@Configuration注解的类中的Bean在构建初始化，di时都完全一致，区别在于通过类对象直接执行对象中的方法会不同。@
> @Configuration注解的类对象调用方法时会有一些处理，这个对象是代理对象，在代理的时候就加入特殊处理了。

# @Import

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Import {
    
        /**
         * {@link Configuration}, {@link ImportSelector}, {@link ImportBeanDefinitionRegistrar}
         * or regular component classes to import.
         */
        Class<?>[] value();
    
    }

它的功能是导入某个配置类，有点像/<import />标签

# @ImportResource

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Documented
    public @interface ImportResource {

        /**
         * Alias for {@link #locations}.
         * @see #locations
         * @see #reader
         */
        @AliasFor("locations")
        String[] value() default {};
    
        /**
         * Resource locations from which to import.
         * <p>Supports resource-loading prefixes such as {@code classpath:},
         * {@code file:}, etc.
         * <p>Consult the Javadoc for {@link #reader} for details on how resources
         * will be processed.
         * @since 4.2
         * @see #value
         * @see #reader
         */
        @AliasFor("value")
        String[] locations() default {};
    
        /**
         * {@link BeanDefinitionReader} implementation to use when processing
         * resources specified via the {@link #value} attribute.
         * <p>By default, the reader will be adapted to the resource path specified:
         * {@code ".groovy"} files will be processed with a
         * {@link org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader GroovyBeanDefinitionReader};
         * whereas, all other resources will be processed with an
         * {@link org.springframework.beans.factory.xml.XmlBeanDefinitionReader XmlBeanDefinitionReader}.
         * @see #value
         */
        Class<? extends BeanDefinitionReader> reader() default BeanDefinitionReader.class;
    }

**当我们使用主要使用java配置时，可能也需要使用xml配置文件**，这样的话就可以使用@ImportResource注解了。
该注解的值是配置文件路径。配置文件的内容可以是bean的描述，也可以是属性的描述，总之都会加入ioc容器。例如
**如果我们主要使用的是配置文件时，却想混合使用java配置** 可以用
<context:component-scan base-package="com.acme"/>


    jdbc.properties
    jdbc.url = JDBC：HSQLDB：HSQL：//本地主机/ XDB
    jdbc.username = SA
    jdbc.password =
    
    properties-config.xml
    <beans>
        <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>
    </beans>
    
    @Configuration
    @ImportResource("classpath:/com/acme/properties-config.xml")
    public class AppConfig {
    
        @Value("${jdbc.url}")
        private String url;
    
        @Value("${jdbc.username}")
        private String username;
            @Value("${jdbc.password}")
        private String password;
    
        @Bean
        public DataSource dataSource() {
            return new DriverManagerDataSource(url, username, password);
        }
    }
    
# @Profile

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Conditional(ProfileCondition.class)
    public @interface Profile {
    
        /**
         * The set of profiles for which the annotated component should be registered.
         */
        String[] value();
    
    }

当我们在不同的环境需要进行不同的配置是，可以用到这个注解。比如我想组件想只在product环境中注册
   
    @Component
    @Profile("product")
    public class Comm{
    
    }
    

    
## Profile的逻辑表达式
    
    !：配置文件的逻辑“不”
    &：配置文件的逻辑“和”
    |：配置文件的逻辑“或”
    
    @Component
    //不是 product 环境中才装配
    @Profile("!product")
    public class Comm{
    
    }

## 激活Profile
在SPringBoot中，可以通过spring. profiles.active
属性就可以指定。那么在Spring中如何指定呢？

    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.getEnvironment().setActiveProfiles("development");
    ctx.register(SomeConfig.class, StandaloneDataConfig.class, JndiDataConfig.class);
    ctx.refresh();

可以通过环境对象，设置activeProfile.其实SpringBoot
中的配置会被读取到环境变量中，然后接下来的过程和上述的代码差不多，去设置Profile


# @Conditional

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Conditional {
    
        /**
         * All {@link Condition Conditions} that must {@linkplain Condition#matches match}
         * in order for the component to be registered.
         */
        Class<? extends Condition>[] value();
    
    }
    
    @FunctionalInterface
    public interface Condition {
    
        /**
         * Determine if the condition matches.
         * @param context the condition context
         * @param metadata metadata of the {@link org.springframework.core.type.AnnotationMetadata class}
         * or {@link org.springframework.core.type.MethodMetadata method} being checked
         * @return {@code true} if the condition matches and the component can be registered,
         * or {@code false} to veto the annotated component's registration
         */
        boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);
    
    }

    
更加灵活的条件配置，在SpringBoot中，实现了很多Condition接口，以及提供了对应的注解。
这些扩展的注解都是基于@Conditional这个元注解的。

比如@Profile，我们就发现这个注解上有     @Conditional(ProfileCondition.class)

    class ProfileCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
            if (attrs != null) {
                for (Object value : attrs.get("value")) {
                    if (context.getEnvironment().acceptsProfiles(Profiles.of((String[]) value))) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
    }

所以当我们想实现自定义的条件注解时，先实现Condition接口的matches方法的自定义（比如类的民资叫A），然后在我们自定义注解上加上
@Conditional(A.class)

# Environment
这个是一个接口，它抽象了环境，简单的说。我们定义的很多配置，或者是Spring默认的一些配置，还有系统的一些属性配置，通过这个对象都能找得到。

获取Environment对象的方式很多
    
    1.通过IOC容器对象获得，ApplicationContext#getEnviroment
    2.实现EnvironmentAware接口
    3.通过@Autowired注解直接注入
    
    
    AnnotationConfigApplicationContext context = getAnnotationContext();
    Environment environment = context.getEnvironment();
    MutablePropertySources resource = ((ConfigurableEnvironment) environment).getPropertySources();
    Map<String, Object> sysEnviroment = ((ConfigurableEnvironment) environment).getSystemEnvironment();
    Map<String, Object> sysProperties = ((ConfigurableEnvironment) environment).getSystemProperties();
    System.out.println("end");
    
# 加载properties配置文件

在定义bean的xml配置文件中，我们如果想加入别的配置，可以使用/<import>注解。但是有很多这样的场景，我们需要加载自定义的一些配置信息
比如jdbc.properties。
> 在xml配置文件中可以使用/<context:property-placeholder location="classpath:jdbc.properties" ignore-unresolvable="true"/>

> 在java配置类中，可以使用

    @PropertySource(value = {"classpath:person.properties"})//加载person.properties配置文件
    @Component
    @ConfigurationProperties(prefix = "person")
    //@Validated
    
    public  class Person {
    }

# 占位符
Spring 容器环境中，都可以使用 ${} 类似于el表达式的方式去引用别的对象。
除了 JVM系统属性 之外用户自定义的属性或者对象一样可以使用占位符表示。Spring会自动解析


# 国际化（i18n）
国际化也称作i18n，其来源是英文单词 internationalization的首末字符i和n，18为中间的字符数。

我们编写的软件可能有要求在多个国家中运行，需要对应不同的国家显示对应的语言。国际化的出现就是满足于这种需求，将需要显示的文字写在配置文件中
系统自动检测当前的语言环境，比如js可以获取当前浏览器的语言 **navigator.language** 然后网页上显示对应语言的文字了。

## java国际化
了解Spring的国际化之前我们先了解一下jdk中支持的国际化,因为Spring的国际化支持是对JDK国际化的进一步封装
### Locale
local英文单词有地点现场的意思，Locale对象封装了地区信息
Local对象提供了静态的成员属性封装了各个国家的信息，同时提供了构造函数让我们可以快速的创建对象。
>   Locale locale = Locale.CHINA;

### ResourceBundle
从英文的字面意思翻译就是资源捆绑对象。它封装了配置的资源文件。提供了静态方法 **getBundle**

    public static final ResourceBundle getBundle(String baseName,Locale locale){
        return getBundleImpl(baseName, locale,
                             getLoader(Reflection.getCallerClass()),
                             getDefaultControl(baseName));
    }
 从源码中我们可以看到，需要一个baseName，还有Local对象
 
 > baseName 是对配置文件的一个大的分类， Local对象是不同的国家的标识。还需要在classPath下创建对应的配置文件。配置文件的命名要求basename_language_country.properties
 
 
### 示例
我们在classPath下创建两个配置文件，命名如下
base_en_US.properties 
base_zh_CN.properties

然后在配置文件中创建一个属性。
msg=I love china
msg=我爱中国

    public static void main(String[] args) {
       Locale locale = Locale.CHINA;
       /**
        * 配置文件的命名要求是，位置放在classpath下
        * basename_language_country.properties
        * 找不到配置文件就会报错 java.util.MissingResourceException
        */
       ResourceBundle bundle = ResourceBundle.getBundle("base", locale);
       //在配置文件中找key对应的字符
       String msg = bundle.getString("msg");
       System.out.println(msg);
    }
    
#### 运行结果
当环境是中国的时候，msg就是 我爱中国
当运行环境是美国的时候，msg就是I love china

#### 代码解析
Locale指定了当前是什么区域
ResourceBundle bundle = ResourceBundle.getBundle("base", locale); 找到指定的配置文件对象，
String msg = bundle.getString("msg");然后读取配置文件中的配置项目，比如读取到了msg




