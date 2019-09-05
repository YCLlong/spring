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
 
 