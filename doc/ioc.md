# 简介
>在Spring中，构成应用程序的主干部分并有Spring IOC容器进行管理的对象是Bean,Bean是一个由Sprng IOC容器实例化，组装和管理的对象。

>在org.springframework.beans和org.springframework.context包是Spring框架的IOC容器基础。

>org.springframework.context.ApplicationContext接口代表IOC容器，容器通过读取配置元数据，实例化，组装和管理Bean对象，配置元数据可以
是 xml配置文件，java注解，java配置类的形式。关于ApplicationContext接口，Spring提供了多种实现，比如ClasspathXmlApplicationContext,可以读取xml配置文件
创建IOC容器实例。

## 什么是IOC
    IoC(Inversion of control)控制权由对象本身转向容器；由容器根据配置文件去创建实例并创建各个实例之间的依赖关系
    
# 实例化容器
 >首先创建配置元数据
 
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    
        <!--可以通过import标签引入其他位置的配置文件-->
        <import resource="spring-service.xml"></import>
    
        <bean id="helloIOC" class="cn.ycl.study.ioc.HelloIOC"></bean>
    
    </beans>
    
>创建IOC容器实例对象，并通过容器对象创建Bean实例对象

    public void createIOC(){
        //1，创建ioc容器对象需要提供配置元数据，sping-context.xml 是xml形式的元数据配置方式
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sping-context.xml");
    
        /**
         * 2，通过ioc容器对象创建Bean对象。前提是，在元数据中，已经配置了bean信息
         *    <bean id="helloIOC" class="cn.ycl.study.ioc.HelloIOC"></bean>
         */
        HelloIOC helloIOC = context.getBean(HelloIOC.class);
        helloIOC.hello();
    }
    
# Bean
bean是IOC容器管理的对象，在IOC容器中 Bean定义表示为 **org.springframework.beans.factory.config.BeanDefinition** 对象(BeanDefinition是一个接口)
配置元数据包括：
* 包限定的类名：通常是正在定义的bean的实际实现类
* Bean行为配置元素，说明bean在容器中的行为方式（范围，声明周期等）
* 引用bean执行其工作所需要的其他bean,这些引用也成为协作者或者依赖项
* 要在新创建的对象中，为这个对象设置属性，例如线程池对象的最大连接数，等。
此元数据转换为构成每个bean定义的一组属性

| 属性 | 描述 | 
| ----- | ---- |
| Class | 类名 | 
| Name | bean实例名称 | 
| Scope | 作用范围 | 
| Constructor arguments | 构造参数注入 | 
| Properties | 属性注入 | 
| Autowiring mode | 自动装配模式 | 
| Lazy initialization mode | 延迟加载模式 | 
| Initialization method | bean创建时回调 | 
| Destruction method | bean销毁时回调 | 

## 不通过配置元数据实例化Bean
    /**
     * 通过BeanFactory对象注册bean，并不通过配置元数据配置bean
     */
    public void registerBean(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sping-context.xml");

        Config config = new Config("ioc配置","通过BeanFactory进行注册");
        context.getBeanFactory().registerSingleton("config",config);

        Config bean = context.getBean(Config.class);
        System.out.println(bean.getName());
    }
    
> 建议不要使用这个方式注册bean。
我们应该需要尽早注册Bean元数据和手动提供的单例实例，以便容器在自动装配和其他内省步骤期间正确推理它们。虽然在某种程度上支持覆盖现有元数据和现有单例实例，但是在运行时注册新bean（与对工厂的实时访问同时）并未得到官方支持，并且可能导致并发访问异常，bean容器中的状态不一致
    
## Bean的命名
小写字母开头，驼峰式命名
### bean别名
可以为一个bean定义多个name,和多个别名
在bean标签中，多个name可以使用逗号隔开，同时可以使用alias标签为name指定别名。
在通过ioc容器实例化Bean时，通过context.getName()方法获取到对象。
    
    <alias name="h1" alias="h2"></alias>
    <alias name="h1" alias="h3"></alias>
    <bean id="helloIOC" name="h1,h11"  class="cn.ycl.study.ioc.HelloIOC">
        <constructor-arg name="desc" value="123"></constructor-arg>
    </bean>

    public void beanName(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sping-context.xml");
        HelloIOC t1 = (HelloIOC) context.getBean("h1");
        HelloIOC t2 = (HelloIOC) context.getBean("h11");
        HelloIOC t3 = (HelloIOC) context.getBean("h2");
        HelloIOC t4 = (HelloIOC) context.getBean("h3");
        System.out.println(t1==t2);//true
        System.out.println(t1==t2);//true
        System.out.println(t1==t3);//true
        System.out.println(t1==t4);//true
    }
    
## Bean的标识
ioc容器查找一个Bean时常见的两种方法，**通过类类型查找**，**通过bean的标识找**
### 通过类类型查找  ClassType
通过类类型查找可以准确无误的找到指定类型的bean,但是当ioc容器中同一种类型的bean不止一个的时候就会报错

### 通过bean的标识找
我们创建bean时，有id,name,还有alias。这些都可以作为bean的标识。
在同一个bean中，id和name可以一致，但是不同bean中，id和name还有别名都必须唯一
同时指定了id和name，那么id就是标识，name就相当于是别名
> 通过id，name或者alias都可以找到bean
## Bean的实例化
配置元数据定义了一个Bean被实例化成一个或者多个对象的规则。如果配置元的形式是XML配置文件，那么Bean定义在<beans>标签下的<bean>标签中。
创建对象的方式有**构造函数创建**、**静态工厂方法创建**、**实例工厂方法创建**

### 构造函数创建
要求class中必须有一个无参构造函数，否则无法创建。类中默认有一个隐式的无参构造函数，如果我们定义了有参构造函数，那么这个无参构造函数就会被覆盖掉，需要显示的声明一个构造函数。
配置危机定义：

    <bean id="config" name="config" class="cn.ycl.study.ioc.bean.Config"></bean>

### 静态工厂方法创建
bean标签中的 factory-method 属性可以指定创建对象的**静态**方法。

    <bean id="config2" name="config2" class="cn.ycl.study.ioc.ConfigFactory" factory-method="createConfig"></bean>
    
### 实例工厂方法创建
        <bean id="factory" class="cn.ycl.study.ioc.ConfigFactory"></bean>
    
        <bean id="config3" name="config3" factory-bean="factory" factory-method="createConfigInstance"></bean>
        
工厂的java类

    public class ConfigFactory {
    
        public static Config createConfig(){
            Config config = new Config();
            config.setName("静态工厂方法创建");
            return config;
        }
    
        /**
         * 修饰符 public或者privite都能正常的返回对象
         * @return
         */
        public Config createConfigInstance(){
            Config config = new Config();
            config.setName("实例工厂方法创建");
            return config;
        }
    }
   
 # 依赖注入Dependency Injection(DI)
一个应用程序往往通过多个对象组成，对象之间相互协作，完成定制的功能。IOC容器创建对象的同时也会根据配置元数据信息给创建的对象进行赋值。赋值的过程就叫做注入。对象和对象之间的依赖关系往往是对象之间的相互引用。比如对象B是对象A的成员属性，那么对象A就要依赖于对象B.

每个对象往往都需要获取与其合作的对象（也就是它所依赖的对象）的引用。如果这个获取过程要靠自身实现，那么这将导致代码高度耦合并且难以维护和调试。

IoC模式，系统中通过引入实现了IoC模式的IoC容器，即可由IoC容器来管理对象的生命周期、依赖关系等，从而使得应用程序的配置和依赖性规范与实际的应用程序代码分离。其中一个特点就是通过文本的配置文件进行应用程序组件间相互关系的配置，而不用重新修改并编译具体的代码。
从而解耦.

DI主要的方式：基于构造函数的依赖注入和基于Setter的依赖注入以及方法注入。

## 构造函数注入
有些时候对象的属性并不是都提供了set方法，比如第三方的一些类库，我们只能通过构造参数注入。

    <!--构造函数属性注入->
    <bean id="config4" name="config4" class="cn.ycl.study.ioc.bean.Config">
        <constructor-arg name="name" type="java.lang.String" value="构造函数注入"></constructor-arg>
        <constructor-arg index="1" ref="h1"></constructor-arg>
        <constructor-arg name="version" value="2"></constructor-arg>
    </bean>
    
> 通过bean标签内的constructor-arg 标签指定。注意其中index从0开始-

## setter方法注入

    <!--setter注入。->
       <bean id="config5" name="config5" class="cn.ycl.study.ioc.bean.Config">
           <property name="name" value="setter注入"></property>
           <property name="ioc" ref="h1"></property>
           <property name="version" value="12"></property>
       </bean>
       
 > 要求bean需要提供属性的set方法，且修饰符是public，否则的就会报错-
 
构造函数注入和Set方法注入可以混合着用。

## 方法注入
当一个单例的BeanA,它有一个方法可以获得非单例的BeanB,但是要求每次都能获得一个新的对象。
当我们用构造函数或者属性注入BeanB时，那么值就不能被改变了。于是Spring提供了方法注入的方式可以实现这个功能。
### 实现ApplicationContextAware接口：
要达到上面说的功能，需要BeanA实现ApplicationContextAware接口中的setApplicationContext方法，以此来注入ioc实例，通过ioc实例的getBean(BeanB.class)返回不同的BeanB示例

    <!--方法注入-->
    <bean name="beanB" class="cn.ycl.study.ioc.methoddi.BeanB" scope="prototype"></bean>
    <bean name="beanA" class="cn.ycl.study.ioc.methoddi.BeanA"></bean>
    
    public class BeanA implements ApplicationContextAware {
        private ApplicationContext context;
        private String desc;
    
        public BeanB getBeanB(){
            return context.getBean(BeanB.class);
        }
    
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }
    
    public class BeanB {
        private String desc;
    
        public String getDesc() {
            return desc;
        }
    
        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    
     /**
     * 方法注入
     */
    public void methodDi(){
        ApplicationContext context = getContrext();
        BeanA beanA = context.getBean(BeanA.class);
        BeanB b1 = beanA.getBeanB();
        BeanB b2 = beanA.getBeanB();
        System.out.println(b1 == b2);//结果是false
    }
但是，我们的代码需要实现ApplicationContextWare接口，就等于和Spring代码耦合了。所以Spring为我们提供了第二种方式
lookup-method

### lookup-method
我们需要将返回对象的方法定义为抽象方法,在bean标签的内容中再定义lookup-method标签，指定方法名称和返回的bean、如下。
其他代码和上面实现ApplicationContextAware接口的方式一样。

    public abstract class BeanA{
        private String desc;
    
        //定义为抽象方法
        public abstract BeanB getBeanB();
    }
    
    <bean name="beanA" class="cn.ycl.study.ioc.methoddi.BeanA">
        <lookup-method name="getBeanB" bean="beanB"></lookup-method>
    </bean>
    
### replace-method
spring提供的这种方法注入的方式比较强大。当后期扩展时，我们需要**完全替换**一个原有的bean的某个方法的实现逻辑
就可以使用replace-method.
    
定义替换类，实现MethodReplacer接口

    /**
     * BeanA 的getDesc方法替换的类
     * 需要实现接口 MethodReplacer
     */
    public class ReplaceBeanA implements MethodReplacer {
        public Object reimplement(Object o, Method method, Object[] objects) throws Throwable {
            System.out.println(o);
            System.out.println(method.getName());
            return "我是替换之后的值";
        }
    }
    
编写配置文件 
    
    <!--方法注入 replace-method-->
    <bean name="replaceBeanA" class="cn.ycl.study.ioc.methoddi.ReplaceBeanA"/>
    <bean name="replaceBeanB" class="cn.ycl.study.ioc.methoddi.BeanB">
        <property name="desc" value="replace-method测试，没有替换之前的初始值"/>
        <!--替换BeanB中的 getDesc 方法，使用替换对象 replaceBeanA-->
        <replaced-method  name="getDesc" replacer="replaceBeanA"/>
    </bean>
    
调用
    
    public void replaceDi(){
        ApplicationContext context = getContrext();
        BeanB beanB = (BeanB) context.getBean("replaceBeanB");
        String desc = beanB.getDesc();
        System.out.println(desc);
    }
    
输出结果

    cn.ycl.study.ioc.methoddi.BeanB$$EnhancerBySpringCGLIB$$b97b6895@4ae82894
    getDesc
    我是替换之后的值

    

## p命名空间
为了使配置更加的简介，<property>标签可以被p命名空间的方式简化
>在\<beans>标签中 加入属性  xmlns:p="http://www.springframework.org/schema/p"

    <!--p命名空间-->
    <bean id="config6" name="config6" class="cn.ycl.study.ioc.bean.Config"
          p:name="p命名空间"
          p:ioc-ref="h2"
          p:version="10086"
    />

## idref
\<idref>标签可以是  \<constructor-arg>标签或者\<property>标签的内部标签。

    <bean id="config7" name="config7" class="cn.ycl.study.ioc.bean.Config">
        <constructor-arg name="name">
            <idref bean="helloIOC"></idref>
        </constructor-arg>
        <property name="version">
            <idref bean="helloIOC"></idref>
        </property>
    </bean>
   
idref和ref的区别在于 idref注入的是Bean的id的值，相当于注入的是一个字符串而不是对象的引用。同时会检查当前ioc容器内是否存在这个bean.

## 引用ioc父容器内的bean
什么是父容器和子容器呢？例如：
> Spring和SpringMVC的容器具有父子关系，Spring容器为父容器，SpringMVC为子容器，子容器可以引用父容器中的Bean，而父容器不可以引用子容器中的Bean。

    <!-- in the child (descendant) context -->
    <bean id="accountService" <!-- bean name is the same as the parent bean -->
        class="org.springframework.aop.framework.ProxyFactoryBean">
        <property name="target">
            <ref parent="accountService"/> <!-- notice how we refer to the parent bean -->
        </property>
        <!-- insert other configuration and dependencies as required here -->
    </bean>
## 内部bean
内部bean不需要指定id和name

    <bean id="config7" name="config7" class="cn.ycl.study.ioc.bean.Config">
        <constructor-arg name="ioc">
            <bean class="cn.ycl.study.ioc.HelloIOC">
                <constructor-arg name="desc" value="内部bean"></constructor-arg>
            </bean>
        </constructor-arg>
    </bean>

## 复杂类型的属性注入
    public class ValueConfig {
        private List<String> stringList;
        private List<Config> configList;
        private Map<String,String> stringMap;
        private Map<String,Config> configMap;
        private Set<String> stringSet;
        private Set<Config> configSet;
        private String[] strArray;
        private Config[] configArray;
        private Properties prop;
    
        public ValueConfig(){}
    
        //省略属性的set方法
    }
    
 配置文件
 
    <!--复杂属性的注入-->
        <bean id="config8" name="config8" class="cn.ycl.study.ioc.bean.ValueConfig">
            <property name="configArray">
                <!--对象数组注入-->
                <array>
                    <bean class="cn.ycl.study.ioc.bean.Config">
                        <property name="name" value="configArray1"></property>
                        <property name="ioc">
                            <null></null>
                        </property>
                    </bean>
    
                    <bean class="cn.ycl.study.ioc.bean.Config">
                        <property name="name" value="configArray2"></property>
                        <property name="ioc" ref="h3"></property>
                    </bean>
                </array>
            </property>
    
            <property name="strArray">
                <!--简单类型数组注入-->
                <array>
                    <value>123</value>
                    <value>456</value>
                </array>
            </property>
    
            <property name="configList">
                <!--对象List注入-->
                <list>
                    <bean class="cn.ycl.study.ioc.bean.Config">
                        <property name="name" value="configList1"></property>
                        <property name="ioc">
                            <null></null>
                        </property>
                    </bean>
                    <bean class="cn.ycl.study.ioc.bean.Config">
                        <property name="name" value="configList2"></property>
                    </bean>
                </list>
            </property>
    
            <property name="stringList">
                <!--简单类型List注入-->
                <list>
                    <value>list1</value>
                    <value>list2</value>
                    <value>list3</value>
                </list>
            </property>
    
            <property name="configMap">
                <!--map注入，如果key是引用类型，则用key-ref,值是引用类型value-ref-->
                <map>
                    <entry key="configMap1" value-ref="config1"></entry>
                    <entry key="configMap2" value-ref="config2"></entry>
                </map>
            </property>
    
            <property name="stringMap">
                <map>
                    <entry key="configMap1" value="config1"></entry>
                    <entry key="configMap2" value="config2"></entry>
                </map>
            </property>
    
            <property name="configSet">
                <!--对象 set注入-->
                <set>
                    <ref bean="config1"></ref>
                    <ref bean="config2"></ref>
                </set>
            </property>
    
            <property name="stringSet">
                <set>
                    <value>set1</value>
                    <value>set2</value>
                </set>
            </property>
    
            <!--property类型注入，简单的keyvalue的类型-->
            <property name="prop">
                <props>
                    <prop key="prop1">属性prop1对应的值</prop>
                    <prop key="prop2">属性prop2的值</prop>
                </props>
            </property>
        </bean>
        
## depends-on
depends-on是\<bean>标签中的属性。当一个bean需要依赖另外一个bean时，但是又不是很明显的引用时，就可以使用depends-on属性。
简单的讲，如果Bean A在初始化之前一定需要Bean B先初始化.那么BeanA就 depends-on BeanB。
    
    <bean id="BeanA" class = "bean.BeanA" depends-on = "BeanB"/>
    
    <bean id = "BeanB" class = "bean.BeanB"/>

如果依赖多个Bean，在depends-on属性的值就是多个bean，用逗号隔开

    <bean id="BeanA" class = "bean.BeanA" depends-on = "BeanB,BeanC"/>
        
    <bean id = "BeanB" class = "bean.BeanB"/>
    <bean id = "BeanC" class = "bean.BeanC"/>
    
## 延迟加载lazy-init
Spring的IOC容器在初始化时会解析配置元数据，然后创建并装配所有单例的Bean实例。有时候我们有需求，并不想让所有单例Bean在IOC容器初始化时被装配。
而是在第一次使用这个Bean时才让它加载（和单例模式的懒汉模式一样一样的）
lazy-init是 \<bean>标签的属性
    
    <bean id="BeanA" class = "bean.BeanA" lazy-init="true"/>

## 自动装配 autowire
自动装配就是在实例化bean对象时，将bean的属性自动注入。一般是引用类型的属性才支持自动注入（不包括String和数组这类引用类型）。
autowire 是 \<bean>标签中的属性。

**autowire="no"** 表示没有自动装配
> \<bean name="config9" autowire="no" class="cn.ycl.study.ioc.bean.Config"/>

**autowire="byName"** 当bean(config9)属性的名称和ioc容器中某个bean（A）的**名称一致且类型一致**时，就会将这个bean（A）注入到bean(config9)对应的属性中。
找不到就不装配
> \<bean name="config9" autowire="byName" class="cn.ycl.study.ioc.bean.Config"/>

**autowire="byType"** 当bean(config9)属性的类型和ioc容器中某个bean（A）的类型一致时，就会将这个bean（A）注入到bean(config9)对应的属性中。
如果和bean(A)的类型一致的Bean在ioc容器中还存在对个，就会报错。找不到就不装配

**autowire="constructor"** 这个适用于以构造函数自动注入的方式。等待装配的Bean中构造函数中**所有的参数必须在ioc容器中都存在bean实例**，否则不会注入任何参数。

1. 查找属性的相同类型的所有bean的列表
2. 在列表中查找和属性名称相同的bean，找到之后注入
3. 如果找不到称相同的bean，但是列表中只有**一个**和属性类型相同的bean,就**装配**这个bean
4. 如果找不到称相同的bean，但是列表中只有**多个**和属性类型相同的bean,那么**不装配**，也不报错

> 显示申明的属性装配和构造函数转配的不论什么时候都会覆盖掉自动装配的值

### autowire-candidate 自动注入候选者
autowire-candidate 是\<bean>的属性，有时候，我们不希望某个bean作为别的bean自动注入的对象，可以声明
> \<bean id="BeanA" class = "bean.BeanA" autowire-candidate="false"/>

那么BeanA就不会被别的Bean自动注入了。

# Bean的作用范围scope
scope 是\<bean>标签中的属性，标记着bean的作用范围。
bean的socpe**默认是singleton**,单例，它和设计模式中的单例模式有点像但是不完全是。
单例的bean在ioc容器中同一个标识只存在一个实例。
>\<bean name="config10" class="cn.ycl.study.ioc.bean.Config" scope="prototype"/>

| 值 | 描述 | 
| ----- | ---- |
| singleton(scope的默认值) | 通过ioc容器查找这个实例时每次都返回相同的实例 |
| prototype| 通过ioc容器查找scope=prototype的实例时每次都返回新的实例，相当于new出来的新对象 |

下面的范围request,session,application,websocket,和Servlet  API中的对象作用域类似。
需要有web的环境才会生效.称之为**web作用域范围**

 | 值 | 描述 | 
 | ----- | ---- |
 | request | 在同一个请求下，通过容器查找这个对象范围相同的实例，不同的请求返回不同的实例 |
  | session | 在同一个回话的范围内，通过容器查找这个对象范围相同的实例，不同的回话返回不同的实例 |
  | application| 类似于singletone. 但是当存在多个应用环境返回的对象就不同，比如分布式环境下|
  | websocket | 当一个websocket连接，到在最后的断开这个范围内通过ioc容器查找的对象相同/


## 作用域代理 \<aop:scoped-proxy/>
当我们想把一个web作用域的bean注入到一个作用域长（比如singleton）的bean的属性中，就可以用作用域代理

    <!-- 一个HTTP session作用域的Bean 作为代理暴露出去 -->
    <bean id="userPreferences" class="com.foo.UserPreferences" scope="session">
        <!--指示容器代理这个Bean -->
        <aop:scoped-proxy/>
    </bean>
    <!--一个单例Bean注入一个代理Bean -->
    <bean id="userManager" class="com.foo.UserManager">
      <!-- 实际使用的是userPreferences的代理对象 -->
        <property name="userPreferences" ref="userPreferences"/>
    </bean>

为什么要这样配置呢？

在userPreferences bean配置中加入<aop:scoped-proxy/>后，容器将创建一个代理对象，该对象拥有和UserPreferences完全相同的public接口并暴露。代理对象每次调用时会从 Session范围内获取真正的UserPreferences对象，而userManager类却不知道。
    
    因为单例的bean中，它的属性在bean被实例化时会被装配一次(当然我们可以在代码中通过set方法可以重新设置这个值）。
    这样设置的好处就在web作用域范围内，我们通过ioc容器返回这userManager时，这个userManager的userPreferences的属性就是最新的，而不是不变的。
    
## 自定义bean的scope
实现 接口 org.springframework.beans.factory.config.Scope

然后注册
    
    <bean class="org.springframework.beans.factory.config.CustomScopeConfigurer">
        <property name="scopes">
            <map>
                <entry key="myScope">
                    <bean class="com.app.MyScope"/>
                </entry>
            </map>
        </property>
    </bean>
    
# Bean的扩展点
SpringFramework提供了许多可用于可自定义bean特性的接口。比如bean初始化时的回调接口，销毁时的接口。ApplicationContextAware接口还有BeanNameAware接口以及别的Aware接口。

## 生命周期回调
 
当我们想要在某个Bean初始化时进行扩展，可以实现 InitializingBean 接口的 afterPropertiesSet方法。在方法中扩展。
但是这种方法需要实现Spring框架的接口，不提倡这样使用。在配置元数据中可以在\<bean>标签的**init-method**属性指定最定义初始化方法

同理，当bean销毁时也提供了回调的接口，如果bean实现了DisposableBean接口的destroy方法，在bean销毁的时候就会调用这个方法。
在配置元数据中可以使用 destroy-method 属性指定

> 要注意的是当bean的scope是prototype时，ioc容器就不会管这个bean的生命周期了，但是在初始化时，会调用初始化的方法，但是销毁时并不会调用销毁的扩展方法

### 基于接口
    <!--bean的扩展点-->
    <bean name="expondLifeInterface" class="cn.ycl.study.ioc.expond.TestLifeInterfaceBean"/>
    
    public class TestLifeInterfaceBean implements InitializingBean, DisposableBean {
        private String desc;
    
        public void afterPropertiesSet() throws Exception {
            System.out.println("开始执行自定义的初始化扩展方法");
            System.out.println("desc=" + desc);
            desc = desc + "自定义初始化";
        }
    
        public void destroy() throws Exception {
            System.out.println("bean销毁，开始执行扩展的销毁方法");
            System.out.println("desc=" + desc);
        }
    
        public String getDesc() {
            return desc;
        }
    
        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    
     /**
     * bean声明周期扩展
     */
    public void expondLifeInterface(){
        ApplicationContext context = getContrext();
        TestLifeInterfaceBean bean = (TestLifeInterfaceBean) context.getBean("expondLifeInterface");
        //按照猜想，得到单例的bean后，将它指向null。后面从容器获取这个bean时理论上是Null。但是结果是配置数据装配后的bean
        bean = null;
        System.gc();
        TestLifeInterfaceBean bean1 = (TestLifeInterfaceBean) context.getBean("expondLifeInterface");
        TestLifeInterfaceBean bean2 = (TestLifeInterfaceBean) context.getBean("expondLifeInterface");
        System.out.println(bean1 == bean2);//结果返回true
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
 
### 基于配置元（提倡这种方式）

    <bean name="expondLifeInterface" class="cn.ycl.study.ioc.expond.TestLifeInterfaceBean" init-method="afterPropertiesSet" destroy-method="destroy"/>
    
测试的时候发现，**初始化**的时候方法确实回调了，但是程序结束后,**销毁**的方法并没有回调。
当程序结束之后，我们可以通过注册钩子程序(context.registerShutdownHook())，让程序结束前，调用我们定义的destroy方法进行资源的释放。

    public static ApplicationContext getContrext(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sping-context.xml");
        //注册回调钩子，可以保证程序关闭时，能够回调bean的destory方法，从而释放资源
        context.registerShutdownHook();
        return context;
    }
    
我们也没有必要在配置每一个bean的时候定义初始化和回调方法，比较麻烦，可以配置默认的回调方法。
default-init-method，default-destroy-method两个属性，是根标签\<beans>中的属性。

    <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd"
       default-init-method="initBean"
       default-destroy-method="destroyBean">

这样定义之后，默认的初始化方法就是initBean,默认的销毁方法就是destroyBean,如果Bean中定义了这个方法就回调，如果没有定义就不会被回调
然后我们需要在自己定义的bean中写这两个方法,即使类型是private也能正常回调

> 如果我们定义了默认的回调方法同时显示的指定了回调方法，那么默认的回调方法就不会被执行

    //bean,将方法的修饰符定义为privite也能正常调用
    public class BeanLife {
        private void initBean(){
            System.out.println("默认初始化bean的方法被调用");
        }
    
        private void destroyBean(){
            System.out.println("默认初始化销毁bean的方法被调用");
        }
    }
    
    //测试调用
    public void lifeConfig(){
        ApplicationContext context = getContrext();
        Lifecycle lifecycle = context.getBean(Lifecycle.class);
        System.out.println("程序执行完毕");
    }
    
    //运行结果【可以通过顺序查看自定义初始化和销毁方法和默认初始化和销毁的方法的执行顺序】
    开始执行自定义的初始化扩展方法
    desc=null
    默认初始化bean的方法被调用
    程序执行完毕
    默认初始化销毁bean的方法被调用
    bean销毁，开始执行扩展的销毁方法
    desc=null自定义初始化
    
 ###基于注解
 >@PostConstruct     注释的方法在bean初始化时回调
 
 >@PreDestroy        注释的方法会在bean销毁时回调
 
 它们都是基于方法的注解。


### 启动和关闭的回调
有点让人疑惑的回调，我们已经有了初始化和销毁的方法回调，那么这个启动和暂停的回调的业务场景在哪儿目前我还不清楚。
打开软件，播放视频，暂停视频，播放视频，关闭软件，这样和初始化销毁的回调区分。
Spring提供了Lifecycle 接口，如下，任何由IOC容器管理的对象都可以实现这个接口。

    public interface Lifecycle {
        void start();
        void stop();
        boolean isRunning();
    }
    
如果有相关的需求，再看官方文档。

## ApplicationContextAware和BeanNameAware
ApplicationContextAware接口中只有一个setApplicationContext的方法。
>其中方法的参数 就是当前IOC容器的具体实例。我们可以通过这个接口得到ioc容器实例对象。

    public interface ApplicationContextAware {
        void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
    }
    
同理BeanNameAware接口中只有一个setBeanName(String name) 方法
    
    public class BeanNameAwareTest implements BeanNameAware {
        public void setBeanName(String name) {
            System.out.println(name);
        }
    }

> 当我们的bean实现了这个接口之后，实例化bean对象时，通过setBeanName方法的参数就能拿到当前bean的name.

## 其他的Aware
Aware(意识到，知道，明白)
>Spring提供了很多Aware接口的扩展，通过这些接口我们可以获取到很多IOC环境中的对象

 | 接口 | 接口中的方法 | 
 | ----- | ---- |
 |ApplicationContextAware| setApplicationContext(ApplicationContext applicationContext)|
 |ApplicationEventPublisherAware| setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)|
 |BeanClassLoaderAware| setBeanClassLoader(ClassLoader classLoader)|
 |BeanFactoryAware| setBeanFactory(BeanFactory beanFactory)|
 |BeanNameAware| setBeanName(String name)|
 |ResourceLoaderAware| setResourceLoader(ResourceLoader resourceLoader)|
 |ResourceLoaderAware| setResourceLoader(ResourceLoader resourceLoader)|
 |MessageSourceAware| setMessageSource(MessageSource messageSource)|
 |LoadTimeWeaverAware| setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver)|
 |NotificationPublisherAware| setNotificationPublisher(NotificationPublisher notificationPublisher)|
 |BootstrapContextAware| |
 |ServletConfigAware| |
 |ServletContextAware| |
 
 
# 定义bean的继承
在java中，继承的关键字是extends，
>在bean的定义中，定义的信息也可以继承,当然java代码中需要存在继承关系

    <bean id="inheritedTestBean" abstract="true"
            class="org.springframework.beans.TestBean">
        <property name="name" value="parent"/>
        <property name="age" value="1"/>
    </bean>
    
    <bean id="inheritsWithDifferentClass"
            class="org.springframework.beans.DerivedTestBean"
            parent="inheritedTestBean" init-method="initialize">  
        <property name="name" value="override"/>
        <!-- the age property value of 1 will be inherited from parent -->
    </bean>

在java代码中 inheritsWithDifferentClass 是 inheritedTestBean的子类。
在 inheritsWithDifferentClass的定义中，使用parent属性执行父类bean的定义。就等于继承了父类的定义信息。
由于在子类中，没有显示的配置 age 属性的值，那么age就会从父类继承。

#IOC容器扩展点


