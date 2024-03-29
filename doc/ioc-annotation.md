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

# 同时使用注解配置和配置文件遇到的坑

## 当配置文件的bean和注解配置的bean的标识相同时

配置文件的配置会直接覆盖掉注解的1配置

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

# @Primary
adj. 主要的；初级的；基本的

当我们按照类型自动注入时，如果出现了多个类型就会报错，但是，加上这个注解之后，当出现相同类型的bean，会优先使用有这个注解的bean进行注入。

    @Configuration
    public class BeanConfig {
        @Bean
        @Primary
        public Person person1(){
            return new Person("龙哥",24);
        }
    
        @Bean
        public Person person2(){
            return new Person("焦妹",24);
        }
    
        @Bean
        public PrimaryTest primaryTest(){
            return new PrimaryTest();
        }
    }
    
    public class PrimaryTest {
        @Autowired
        public Person person;
    }

如测试用例中 PrimaryTest 类中注入了Person,而我们配置了两个Person的Bean的实例，自动注入时直接报错。在其中一个Bean上加了 @Primary之后，就不会报错了。
ioc容器自动装配时，就会注入这个拥有@Primary 注解的bean.

> 当我为两个Person Bean 加上@Prmary 注解时，也会报错

> 配置文件中primary的配置  \<bean class="example.SimpleMovieCatalog" primary="true">

# @Qualifier
Qualifier n. 限定词，[语] 限定语；取得资格的人；修饰语

上面的@Primary注解是确定要装配相同类型中的具体的一个Bean时，可以使用这个注解指定，但是确实不太灵活。
> ioc容器中相同类型的bean有很多个，但是bean的名称是唯一的。使用@Qulifier注解可以更具相同类型的bean的名称指定装配

    public class PrimaryTest {
        @Autowired
        //指定person2装配
        @Qualifier("person2")
        public Person person;
    }

这个限定词同时可以用于参数上，当自动装配注入到参数上时依然会出现不知道选择哪个bean注入的情况。

    public class PrimaryTest {
        @Autowired
        //指定person2装配
        @Qualifier("person2")
        public Person person;
    
        private Person privatePerson;
    
        @Autowired
        private void init(@Qualifier("person1") Person person){
            this.privatePerson = privatePerson;
        }
    }
那在xml配置文件中怎么使用限定符注解呢？
    
    <bean class="example.SimpleMovieCatalog">
        <qualifier value="main"/> 
        <!-- inject any dependencies required by this bean -->
    </bean>
## 自定义限定符
Spring提供的@Qualifier 这个限定符注解，可以根据bean的名称按照限定的名称进行装配。同时这个注解解析器提供了扩展的功能。

### 自定义注解的应用过程
1. 自定义一个注解，在注解上加上@Qualifier
2. 在bean的配置元数据中指定注解的类型，如果有值需要配置指定的值
3. 和@Qualifier一样将注解用于参数，成员属性，或者方法上
4. Spring的注解解析器会解析我们的注解，然后根据bean的配置信息中我们定义的信息进行匹配

> 新建一个注解，再自定义的注解上加上@Qualifier，那么Spring的注解解析器就能解析我们自定义的注解。

我们新建一个注解，注解内没有任何的值
### 自定义注解
    @Target({ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    //加上这个注解Spring的@Qualifier注解解析器也能解析我们的注解
    @Qualifier
    public @interface Offline {
    
    }
### 在bean中配置限定

    <!--自定义限定符-->
    <bean name="qulifierSelf" class="cn.ycl.study.annotation.SelfQulifier">
        <!--加上了限定符，如果项目中有限定符的使用，这个信息就会起到匹配的作用，否则毫无用处-->
        <qualifier type="cn.ycl.study.annotation.Offline"/>
    </bean>

    <bean name="qulifierTest" class="cn.ycl.study.annotation.SelfQulifier">
    </bean> 

### 在代码中自动装配这个bean
    
    public class QulifierTest {
        public SelfQulifier qulifier;
    
        //由于在ioc容器中声明了两个类型一致的SelfQulifier bean,这样自动装配会直接报错
        @Autowired
        private void test(SelfQulifier qulifier){
            this.qulifier = qulifier;
        }
    }
    
调用
    
     public void selfQulifier(){
        ApplicationContext context = getContext();
        //如果没有这句，不会触发自动装配
        context.getBean(QulifierTest.class);
        System.out.println("11");
    }

### 使用限定符注解
    
    public class QulifierTest {
    
        public SelfQulifier qulifier;
    
        //由于在ioc容器中声明了两个类型一致的SelfQulifier bean,这样自动装配会直接报错
        @Autowired
        //使用自定义限定符注解，会从配置元数据中读取限定符信息。如果读取到多个相同类型具有相同的限定符信息时也会报错
        @Offline
        private void test(SelfQulifier qulifier){
            this.qulifier = qulifier;
        }
    }

加上Offline注解之后就OK了，就会装配在配置文件中指定了修饰限定符的那个bean

### 自定义注解之修饰限定符属性值。
如果仅仅只是自定义限定符的类型扩展度不够，Spring的注解解析器允许自定义的修饰限定符加上参数，根据多个参数的属性同时限定

#### 自定义注解中加入属性
    @Target({ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    //加上这个注解Spring的@Qualifier注解解析器也能解析我们的注解
    @Qualifier
    public @interface Offline {
        //我们可以自定义属性,不加上 default，就是必须的参数
        String name();
        int level() default 0;
    }
#### 配置文件中配置限定符
      <!--自定义限定符-->
        <bean name="qulifierSelf" class="cn.ycl.study.annotation.qulifier.SelfQulifier">
            <!--加上了限定符，如果项目中有限定符的使用，这个信息就会起到匹配的作用，否则毫无用处-->
            <qualifier type="cn.ycl.study.annotation.qulifier.Offline">
                <attribute key="name" value="test"></attribute>
                <attribute key="level" value="1"></attribute>
            </qualifier>
            <property name="desc" value="龙哥"/>
        </bean>
    
        <bean name="qulifierTest" class="cn.ycl.study.annotation.qulifier.SelfQulifier">
            <qualifier type="cn.ycl.study.annotation.qulifier.Offline">
                <attribute key="name" value="test"></attribute>
            </qualifier>
            <property name="desc" value="焦妹"/>
        </bean>
        
#### java代码中使用自定义注解，并配置属性

    public class QulifierTest {

        public SelfQulifier qulifier;
    
        //由于在ioc容器中声明了两个类型一致的SelfQulifier bean,这样自动装配会直接报错
        @Autowired
        //使用自定义限定符注解，会从配置元数据中读取限定符信息。如果读取到多个相同类型具有相同的限定符信息时也会报错
        @Offline(name = "test")
        private void test(SelfQulifier qulifier){
            //焦妹
            this.qulifier = qulifier;
        }
    
        @Autowired
        @Offline(name = "test",level = 1)
        private void test1(SelfQulifier qulifier){
            //龙哥
            this.qulifier = qulifier;
        }
    } 

#### \<bean>标签中的子标签 \<meta>
    
    <bean name="qulifierTest" class="cn.ycl.study.annotation.qulifier.SelfQulifier">
        <meta key="name" value="test"/>
        <meta key="level" value="0"/>
        <property name="desc" value="焦妹"/>
    </bean>
 
 我们发现 @Offline(name = "test") 这个自动注入也能注入焦妹。
 > \<qualifier/>元素及其属性优先,但如果没有匹配到，则匹配 \<meta>中的值
 
 > 配置限定符的信息目前只知道在配置文件中配置，使用注解目前还不知道能不能配置限定符信息
 
 
 ### 使用泛型作为隐式限定符
 
 假设前面的bean实现了一个通用接口（即Store<String>和， Store<Integer>），您可以@Autowire将Store接口和泛型用作限定符，如下例所示：
 
     @Autowired
     private Store<String> s1; // <String> qualifier, injects the stringStore bean
     
     @Autowired
     private Store<Integer> s2; // <Integer> qualifier, injects the integerStore bean
 
 ### CustomAutowireConfigurer
 CustomAutowireConfigurer 是一个BeanFactoryPostProcessor允许您注册自己的自定义限定符注释类型的，即使它们没有使用Spring的@Qualifier注释进行注释。
    
    //CustomAutowireConfigurer源码
    public class CustomAutowireConfigurer implements BeanFactoryPostProcessor, BeanClassLoaderAware, Ordered {
    
    	private int order = Ordered.LOWEST_PRECEDENCE;  // default: same as non-Ordered
    
    	@Nullable
    	private Set<?> customQualifierTypes;
    
    //配置自定义的限定符
    <bean id="customAutowireConfigurer"
            class="org.springframework.beans.factory.annotation.CustomAutowireConfigurer">
        <property name="customQualifierTypes">
            <set>
                <value>example.CustomQualifier</value>
            </set>
        </property>
    </bean>
    
# @Resource
这个注解是java提供的注解，但是Spring框架中的注解解析器可以解析这个注解。这个注解的用法和@Autowried用法差不多。

该注解可以用到方法上，属性上，可以自动注入。

# @PostConstruct和@PreDestroy
这两个注解也是java提供的注解。在bean的声明周期中介绍过。
> @PostConstruct 注解的方法 会在容器初始化时就会调用

# @Component
component 英文是组件的意思，它是Spring管理的组件通用构造类型，**@Component作用于类上**。

> @Component 可以将一个pojo类实例化到ioc容器中,相当于配置文件中的 \<bean id="" class=""/>

> @Component和@Bean都是用来注册Bean并装配到Spring容器中，但是Bean比Component的自定义性更强。可以实现一些Component实现不了的自定义加载类。
 
@Component是一个元注解，在这个注解之上Spring又扩展了

     @Service
     @Repository
     @Controller
     @Configuration
     
其中Service的源码,在@Service注解上有 @Component 注解

     @Target({ElementType.TYPE})
     @Retention(RetentionPolicy.RUNTIME)
     @Documented
     @Component
     public @interface Service {
     	@AliasFor(annotation = Component.class)
     	String value() default "";
     }
     
# @Scope
这个注解和\<bean> 标签中的scope 作用一致，描述bean的范围。
    @Scope("singleton")
    @Scope("prototype")

# @ComponentScan
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Documented
    @Repeatable(ComponentScans.class)
    public @interface ComponentScan {
    
    	@AliasFor("basePackages")
    	String[] value() default {};
    
    	@AliasFor("value")
    	String[] basePackages() default {};
    
    	Class<?>[] basePackageClasses() default {};
    
    	Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    	Class<? extends ScopeMetadataResolver> scopeResolver() default AnnotationScopeMetadataResolver.class;
    
    	ScopedProxyMode scopedProxy() default ScopedProxyMode.DEFAULT;
    
    	String resourcePattern() default ClassPathScanningCandidateComponentProvider.DEFAULT_RESOURCE_PATTERN;
    
    	boolean useDefaultFilters() default true;
    
    	Filter[] includeFilters() default {};
    
    	Filter[] excludeFilters() default {};
    
    	boolean lazyInit() default false;
    
    
    	/**
    	 * Declares the type filter to be used as an {@linkplain ComponentScan#includeFilters
    	 * include filter} or {@linkplain ComponentScan#excludeFilters exclude filter}.
    	 */
    	@Retention(RetentionPolicy.RUNTIME)
    	@Target({})
    	@interface Filter {

    		FilterType type() default FilterType.ANNOTATION;
    
    		@AliasFor("classes")
    		Class<?>[] value() default {};

    		@AliasFor("value")
    		Class<?>[] classes() default {};
    
    		String[] pattern() default {};
    
    	}
    
    }
这个注解可以告诉它的解析器，去扫描指定路径下的组件，并将其注入到ioc容器中。

@ComponentScan 注解提供了很多灵活的过滤器配置，比如过滤某些包或者路径不扫描，或者指定某些包或者路径去扫描，能使用表达式去匹配对应的路径。

> 在一个类上使用@ComponentScan 注解，不设置任何值，那么就会扫描这个类所在的包以及子包下的所有目录，去提取目录中存在@Component注解的类，并将它注册到ioc容器中

> 可以使用basePackage 属性指定扫描的根目录。我们要注意的是，如果根包下的不同子包下存在相同名字的类，只要其中的一个类需要自动装配或者要加入ioc容器中就会抱错，beanDefinition冲突。

## 过滤器 
@interface Filter 注解是 Component注解的子注解。

过滤器的类型 

    public enum FilterType {
    
        /**
         * Filter candidates marked with a given annotation.
         * @see org.springframework.core.type.filter.AnnotationTypeFilter
         */
        ANNOTATION,
    
        /**
         * Filter candidates assignable to a given type.
         * @see org.springframework.core.type.filter.AssignableTypeFilter
         */
        ASSIGNABLE_TYPE,
    
        /**
         * Filter candidates matching a given AspectJ type pattern expression.
         * @see org.springframework.core.type.filter.AspectJTypeFilter
         */
        ASPECTJ,
    
        /**
         * Filter candidates matching a given regex pattern.
         * @see org.springframework.core.type.filter.RegexPatternTypeFilter
         */
        REGEX,
    
        /** Filter candidates using a given custom
         * {@link org.springframework.core.type.filter.TypeFilter} implementation.
         */
        CUSTOM
    
    }
    
    ANNOTATION：注解类型
    ASSIGNABLE_TYPE：ANNOTATION：指定的类型
    ASPECTJ：按照Aspectj的表达式，基本上不会用到
    REGEX：按照正则表达式
    CUSTOM：自定义规则
    
### 自定义过滤器规则
实现 TypeFilter 接口的 boolean match 方法
    
    public class MyScanFilter implements TypeFilter {
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            if (metadataReader.getClassMetadata().getClassName().contains("ExcludeBean")){
                //获取当前类注解的信息
                AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                for (String s : annotationMetadata.getAnnotationTypes()) {
                    System.out.println("当前正在被扫描的类注解类型" + s);
                }
                //获取当前正在扫描类的信息
                ClassMetadata classMetadata = metadataReader.getClassMetadata();
                System.out.println("当前正在被扫描的类的类名" + classMetadata.getClassName());
                //获取当前类的资源信息（类存放的路径...）
                Resource resource = metadataReader.getResource();
                System.out.println("当前正在被扫描的类存放的地址" + resource.getURL());
                return true;
            }
            return false;
        }
    }
 
在@ComponentScan.Filter注解中将类型定义为Filter.CUSTOM,将值设置成自定义注解的class
    
    @ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM,value = MyScanFilter.class)})
    public class ConfigApp {
    }

创建ioc实例对象

    public static AnnotationConfigApplicationContext getAnnotationContext(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigApp.class);
        return context;
    }
    
    public void componentScanTest(){
        ApplicationContext context = getAnnotationContext();
        context.getBean(ExcludeBean.class);
        System.out.println("结束");
    }

运行结果

    当前正在被扫描的类注解类型org.springframework.stereotype.Component
    当前正在被扫描的类的类名cn.ycl.study.annotation.comonentscanfilter.ExcludeBean
    当前正在被扫描的类存放的地址file:/E:/spring/target/classes/cn/ycl/study/annotation/comonentscanfilter/ExcludeBean.class
    Exception in thread "main" org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'c
    
    因为过滤中通过我们自己的过滤逻辑匹配到返回了true，所以这个ExcludeBean就不会被扫描到，ioc容器中没有它的信息就会报错啦。
    

# @Bean
这个注解和\<bean>标签是一样的功能，在ioc容器中注册一个bean.

## 在Component 中注册bean和在Configuration中注册bean的区别

@Component 中使用@Bean注册bean

    @Component
    public class ComponentBean {
        @Bean("componentPeron")
        public Person getPersion(){
            return new Person("在component中注册的bean",0);
        }
    }
@Configuration中使用@Bean注册bean

    @Configuration
    public class ConfigurationBean {
        @Bean("configurationPeron")
        public Person getPersion(){
            return new Person("在configuration中注册bean",1);
        }
    }

测试区别

        public void testBean(){
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
运行结果

    @Configuration中注册的bean:true
    @Component中注册的bean:true
    @Configuration调用方法的区别：true
    @Component中调用方法的区别：false
        
### 结论
作为bean的注册或者是依赖注入的功能来讲，在Component中注册bean和在Configuration中注册bean效果是完全一样的。
但是在方法调用上是有区别的，我们通过测试的例子就能发现，当我们调用 **@Component**  注解的类中的方法时，返回的Person对象是不一样的，说明**是按照普通的方法调用**
，而使用 **@Configuration注解的类**调用getPersion方法时，生成的对象完全一样，那么就 **不是按照普通的方法调用**了。

### 原理
@Configuration标注下的@Bean调用函数使用都是代理对象，获取的都是从IOC容器里获取的bean，因此都是同一个。而@Component标注下的@Bean下只是普通的函数方法调用。下面来看一下@configuration注册@Bean生成代理的过程。
    
    
 ## 使用扫描注解时，bean的命名
    
@CompmentSscan会扫描指定包下所有@Compment注解的类，然后会将其加入到ioc容器中。那这些通过扫描加入ioc容器的bean的名称是如果确定的呢？

>这些bean的名称由Spring提供的bean命名策略生成，我们可以自定义bean的生成策略，实现 BeanNameGenerator接口，然后在 @ComponentScan注解的nameGenerator 属性中指定我们自定义的策略的class

    @Configuration
    @ComponentScan(basePackages = "org.example", nameGenerator = MyNameGenerator.class)
    public class AppConfig {
    }
 
那么自定义的bean名字生成策略又是什么了？
> @Component，@Service，@Repository,@Controller等注解有value，这个value当我们指定时就是用这个value作为bean的标识，否则使用是类的名称（第一个字母小写）作为bean的名称。

> @Bean注解类似，提供了名称属性，如果不定义就会使用方法名作为bean的名称。

## 使用扫描注解时，bean的范围
> 要为范围解析提供自定义策略而不是依赖基于注释的方法，您可以实现该 ScopeMetadataResolver 接口。请确保包含默认的无参数构造函数。然后，您可以在配置扫描程序时提供完全限定的类名，因为以下注释和bean定义示例显示
    
    @Configuration
    @ComponentScan(basePackages = "org.example", scopeResolver = MyScopeResolver.class)
    public class AppConfig {
    }
## 生成候选组件索引
虽然类路径扫描速度非常快，但可以通过在编译时创建候选的静态列表来提高大型应用程序的启动性能。在此模式下，所有作为组件扫描目标的模块都必须使用此机制。

使用方法很简单，引入相关的依赖就行了。

    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context-indexer</artifactId>
        <version>5.1.9.RELEASE</version>
        <optional>true</optional>
    </dependency>
   
# JSR303标准注释的支持
从Spring 3.0开始，Spring提供对JSR-330标准注释（依赖注入）的支持。这些注释的扫描方式与Spring注释相同。要使用它们，您需要在类路径中包含相关的jar。
    
    <dependency>
        <groupId>javax.inject</groupId>
        <artifactId>javax.inject</artifactId>
        <version>1</version>
    </dependency>
    
个人感觉不会用到，如果用到可以去官网查询详细用法


