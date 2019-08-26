# IOC和Bean
## 简介
>在Spring中，构成应用程序的主干部分并有Spring IOC容器进行管理的对象是Bean,Bean是一个由Sprng IOC容器实例化，组装和管理的对象。

>在org.springframework.beans和org.springframework.context包是Spring框架的IOC容器基础。

>org.springframework.context.ApplicationContext接口代表IOC容器，容器通过读取配置元数据，实例化，组装和管理Bean对象，配置元数据可以
是 xml配置文件，java注解，java配置类的形式。关于ApplicationContext接口，Spring提供了多种实现，比如ClasspathXmlApplicationContext,可以读取xml配置文件
创建IOC容器实例。

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
    