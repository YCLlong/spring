# IOC和Bean
## 简介
>在Spring中，构成应用程序的主干部分并有Spring IOC容器进行管理的对象是Bean,Bean是一个由Sprng IOC容器实例化，组装和管理的对象。

>在org.springframework.beans和org.springframework.context包是Spring框架的IOC容器基础。

>org.springframework.context.ApplicationContext接口代表IOC容器，容器通过读取配置元数据，实例化，组装和管理Bean对象，配置元数据可以
是 xml配置文件，java注解，java配置类的形式。关于ApplicationContext接口，Spring提供了多种实现，比如ClasspathXmlApplicationContext,可以读取xml配置文件
创建IOC容器实例。

### 什么是IOC
    IoC(Inversion of control)控制权由对象本身转向容器；由容器根据配置文件去创建实例并创建各个实例之间的依赖关系
    
## 实例化容器
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
    
## Bean
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

### 不通过配置元数据实例化Bean
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
    
### Bean的命名
小写字母开头，驼峰式命名
#### bean别名
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

### Bean的实例化
配置元数据定义了一个Bean被实例化成一个或者多个对象的规则。如果配置元的形式是XML配置文件，那么Bean定义在<beans>标签下的<bean>标签中。
创建对象的方式有**构造函数创建**、**静态工厂方法创建**、**实例工厂方法创建**

#### 构造函数创建
要求class中必须有一个无参构造函数，否则无法创建。类中默认有一个隐式的无参构造函数，如果我们定义了有参构造函数，那么这个无参构造函数就会被覆盖掉，需要显示的声明一个构造函数。
配置危机定义：

    <bean id="config" name="config" class="cn.ycl.study.ioc.bean.Config"></bean>

#### 静态工厂方法创建
bean标签中的 factory-method 属性可以指定创建对象的**静态**方法。

    <bean id="config2" name="config2" class="cn.ycl.study.ioc.ConfigFactory" factory-method="createConfig"></bean>
    
#### 实例工厂方法创建
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
   
 ### 依赖注入Dependency Injection(DI)
一个应用程序往往通过多个对象组成，对象之间相互协作，完成定制的功能。IOC容器创建对象的同时也会根据配置元数据信息给创建的对象进行赋值。赋值的过程就叫做注入。对象和对象之间的依赖关系往往是对象之间的相互引用。比如对象B是对象A的成员属性，那么对象A就要依赖于对象B.

每个对象往往都需要获取与其合作的对象（也就是它所依赖的对象）的引用。如果这个获取过程要靠自身实现，那么这将导致代码高度耦合并且难以维护和调试。

IoC模式，系统中通过引入实现了IoC模式的IoC容器，即可由IoC容器来管理对象的生命周期、依赖关系等，从而使得应用程序的配置和依赖性规范与实际的应用程序代码分离。其中一个特点就是通过文本的配置文件进行应用程序组件间相互关系的配置，而不用重新修改并编译具体的代码。
从而解耦.

DI主要的方式：基于构造函数的依赖注入和基于Setter的依赖注入。

#### 构造函数注入
有些时候对象的属性并不是都提供了set方法，比如第三方的一些类库，我们只能通过构造参数注入。

    <!--构造函数属性注入->
    <bean id="config4" name="config4" class="cn.ycl.study.ioc.bean.Config">
        <constructor-arg name="name" type="java.lang.String" value="构造函数注入"></constructor-arg>
        <constructor-arg index="1" ref="h1"></constructor-arg>
        <constructor-arg name="version" value="2"></constructor-arg>
    </bean>
    
> 通过bean标签内的constructor-arg 标签指定。注意其中index从0开始-

#### setter方法注入

    <!--setter注入。->
       <bean id="config5" name="config5" class="cn.ycl.study.ioc.bean.Config">
           <property name="name" value="setter注入"></property>
           <property name="ioc" ref="h1"></property>
           <property name="version" value="12"></property>
       </bean>
       
 > 要求bean需要提供属性的set方法，且修饰符是public，否则的就会报错-
 
构造函数注入和Set方法注入可以混合着用。

#### p命名空间
为了使配置更加的简介，<property>标签可以被p命名空间的方式简化
>在\<beans>标签中 加入属性  xmlns:p="http://www.springframework.org/schema/p"

    <!--p命名空间-->
    <bean id="config6" name="config6" class="cn.ycl.study.ioc.bean.Config"
          p:name="p命名空间"
          p:ioc-ref="h2"
          p:version="10086"
    />

#### idref
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

#### 引用ioc父容器内的bean
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
#### 内部bean
内部bean不需要指定id和name

    <bean id="config7" name="config7" class="cn.ycl.study.ioc.bean.Config">
        <constructor-arg name="ioc">
            <bean class="cn.ycl.study.ioc.HelloIOC">
                <constructor-arg name="desc" value="内部bean"></constructor-arg>
            </bean>
        </constructor-arg>
    </bean>

#### 复杂类型的属性注入
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