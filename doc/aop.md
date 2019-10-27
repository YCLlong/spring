# 面向切面编程AOP
面向切面编程（Aspect-oriented Programing 简称AOP），面向切面编程是相对于面向对象编程（Object-oriented Programing 简称OOP）提出的概念。
是作为OOP的一种功能补充。OOP主要的单元模块是类，而AOP则是切面（aspect）编程。要连接面向切面编程需要先了解如下几个概念。

AOP是Spring框架的重要组件，虽然IOC容器并不依赖与AOP，Spring也不会强迫开发者使用AOP，但是AOP提供了非常棒的功能，作为IOC的补充

## 为什么用AOP？
1，扩展某些功能可以不用修改现有的某些代码，即可以让我们少些代码，现有的代码不改变也会导致bug少，节省测试资源。
2，可以让代码逻辑更加清晰，我们有一段业务代码，有时候我们需要加上一段性能日志，可能还要保存到数据库中。那么要加的功能就需要写代码，就要在现有的业务代码前后加上一些代码，这样就会导致代码混在一起，不是很清晰。如果使用aop的话就可以无需修改现有业务代码而达到我们的功能，也就是使得逻辑更加清晰。

## 常见术语

### 通知(Advice)
比如上面例子中加性能日志的功能，这个具体的功能就是通知，常见的通知有安全，事务，日志。
spring aop通知(advice)分成五类： 
前置通知[Before advice]：在连接点前面执行，前置通知不会影响连接点的执行，除非此处抛出异常。 
正常返回通知[After returning advice]：在连接点正常执行完成后执行，如果连接点抛出异常，则不会执行。 
异常返回通知[After throwing advice]：在连接点抛出异常后执行。 
返回通知[After (finally) advice]：在连接点执行完成后执行，不管是正常执行完成，还是抛出异常，都会执行返回通知中的内容。 
环绕通知[Around advice]：环绕通知围绕在连接点前后，比如一个方法调用的前后。

### 连接点（JoinPoint）
允许使用通知的地方叫做连接点，哪些地方允许使用通知呢（增强功能）？比如方法的前，后（前后都有），或则抛出异常都是连接点。在Spring AOP中，一个连接点总是表示一个方法的执行。

### 切入点（Pointcut）
在连接点的基础上定义切入点就非常好理解，比如一个类有10个方法，那么允许使用通知的地方就哟有10几个了，但是我们并不是想增强每一个方法，只想增强指定的目标方法，那么这些指定的要增强功能的方法就是切入点

### 切面（Aspect）
面向切面编程的切面就是这个切面，切面就是通知和切入点的结合。我们发现没什么连接点的概念了，连接点的提出是为了让我们更好的理解切入点。通知表示要干什么，什么时候干（before，after,around等通知类型）,切入点表示在哪儿干，这两个条件就定义了一个完整的切面

### 引入(introduction)
允许我们向现有的类添加新方法属性。这不就是把切面（也就是新方法属性：通知定义的）用到目标类中吗

### 目标（target）
引入中所提到的目标类，也就是要被通知的对象，也就是真正的业务逻辑，他可以在毫不知情的情况下，被咱们织入切面。而自己专注于业务本身的逻辑。

### 代理(proxy)
AOP框架创建的对象，用来实现切面（包括方法通知等功能），在Spring中，AOP代理可以是JDK动态代理或CGLIB代理

### 织入(weaving)
把切面应用到目标对象来创建新的代理对象的过程。这个过程可以在编译时（例如使用AspectJ编译器）、类加载时或运行时中完成，Spring和其他纯java AOP框架一样，是在运行时完成植入的。


## AOP 代理
Spring 默认使用JDK的动态代理作为AOP的代理，这样任何接口（或者接口的set）都能被代理。

Spring也支持使用CGLIB代理，对于需要代理类而不是接口的时候，CGLIB代理非常有必要的，如果业务对象并没有实现接口，默认会使用CGLIB代理。当然并不是说实现了接口就不能使用CGLIB代理，可以强制使用CGLIB代理。

## @AspectJ的支持
要在Spring中使用@AjpectJ切面，需要Spring启动AspectJ支持。

可以使用XML或Java配置的方式启用@AspectJ支持。不管哪一种方式，您还需要确保AspectJ的aspectjweaver.jar库位于应用程序的类路径中（版本1.8或更高版本）。此库可在AspectJ分发的lib 目录中或Maven Central存储库中找到。
    
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>1.9.4</version>
    </dependency>

XML配置的方式启动
    
    <!--开启aspectJ的注解开发，AspectJ的自动代理-->
    <!--<aop:aspectj-autoproxy/>-->

注解的方式启动

    @Configuration
    @EnableAspectJAutoProxy
    public class AppConfig {
    
    }

## 切点表达式
Spring Aop 支持使用以下AspectJ切点标识符（PCD），用于切点表达式。
由下列方式来定义或者通过 &&、 ||、 !、 的方式进行组合，例如：
    
    @Pointcut("execution(public * *(..))")
    private void anyPublicOperation() {} (1)
    
    @Pointcut("within(com.xyz.someapp.trading..*)")
    private void inTrading() {} (2)
    
    @Pointcut("anyPublicOperation() && inTrading()")
    private void tradingOperation() {} (3)
    
    （3） 使用 && 组合了 (1) 和 (2) 这两个切点表达式，注意写法，写的是方法名
    

execution：用于匹配方法执行的连接点，这是使用Spring AOP时主要的切点标识符；

within：用于匹配指定类型内的方法执行；

this：用于匹配当前AOP代理对象类型的执行方法；注意是AOP代理对象的类型匹配，这样就可能包括引入接口也类型匹配；        

target：用于匹配当前目标对象类型的执行方法；注意是目标对象的类型匹配，这样就不包括引入接口也类型匹配；

args：用于匹配当前执行的方法传入的参数为指定类型的执行方法；

@within：用于匹配所以持有指定注解类型内的方法；

@target：用于匹配当前目标对象类型的执行方法，其中目标对象持有指定的注解；

@args：用于匹配当前执行的方法传入的参数持有指定注解的执行；

@annotation：用于匹配当前执行方法持有指定注解的方法；


示例：
    
    匹配任意公共方法的执行:
    
    execution(public * *(..))
    匹配任意以set开始的方法:
    
    execution(* set*(..))
    匹配定义了AccountService接口的任意方法:
    
    execution(* com.xyz.service.AccountService.*(..))
    匹配定义在service 包中的任意方法:
    
    execution(* com.xyz.service.*.*(..))
    匹配定义在service包和其子包中的任意方法:
    
    execution(* com.xyz.service..*.*(..))
    匹配在service包中的任意连接点（只在Spring AOP中的方法执行）:
    
    within(com.xyz.service.*)
    匹配在service包及其子包中的任意连接点（只在Spring AOP中的方法执行）:
    
    within(com.xyz.service..*)
    匹配代理实现了AccountService 接口的任意连接点（只在Spring AOP中的方法执行）：
    
    this(com.xyz.service.AccountService)
    'this' 常常以捆绑的形式出现. 见后续的章节讨论如何在声明通知中使用代理对象。
    
    匹配当目标对象实现了AccountService接口的任意连接点（只在Spring AOP中的方法执行）:
    
    target(com.xyz.service.AccountService)
    'target' 常常以捆绑的形式出现. 见后续的章节讨论如何在声明通知中使用目标对象。
    
    匹配使用了单一的参数，并且参数在运行时被传递时可以序列化的任意连接点（只在Spring的AOP中的方法执行）。:
    
    args(java.io.Serializable)
    'args' 常常以捆绑的形式出现.见后续的章节讨论如何在声明通知中使用方法参数。
    
    注意在这个例子中给定的切点不同于execution(* *(java.io.Serializable)). 如果在运行时传递的参数是可序列化的，则与execution匹配，如果方法签名声明单个参数类型可序列化，则与args匹配。
    
    匹配当目标对象有@Transactional注解时的任意连接点（只在Spring AOP中的方法执行）。
    
    @target(org.springframework.transaction.annotation.Transactional)
    '@target' 也可以以捆绑的形式使用.见后续的章节讨论如何在声明通知中使用注解对象。
    
    匹配当目标对象的定义类型有@Transactional注解时的任意连接点（只在Spring的AOP中的方法执行）:
    
    @within(org.springframework.transaction.annotation.Transactional)
    '@within' 也可以以捆绑的形式使用.见后续的章节讨论如何在声明通知中使用注解对象。
    
    匹配当执行的方法有@Transactional注解的任意连接点（只在Spring AOP中的方法执行）:
    
    @annotation(org.springframework.transaction.annotation.Transactional)
    '@annotation' 也可以以捆绑的形式使用.见后续的章节讨论如何在声明通知中使用注解对象。
    
    匹配有单一的参数并且在运行时传入的参数类型有@Classified注解的任意连接点（只在Spring AOP中的方法执行）:
    
    @args(com.xyz.security.Classified)
    '@args' 也可以以捆绑的形式使用.见后续的章节讨论如何在声明通知中使用注解对象。
    
    匹配在名为tradeService的Spring bean上的任意连接点（只在Spring AOP中的方法执行）:
    
    bean(tradeService)
    匹配以Service结尾的Spring bean上的任意连接点（只在Spring AOP中方法执行） :
    
    bean(*Service)

# AOP代理

Spring**默认**使用jdk动态代理作为AOP代理。这样任何接口（或者接口的Set）都可以被代理。

Spring也支持使用CGLIB代理，对于需要代理类而不是接口的时候CGLIB代理是很有必要的，如果业务对象没有接口实现，默认就会使用CGLIB代理，此外，面向接口编程实践，业务对象通常会实现一个或多个接口，当然还能强制使用CGLIB代理。

## 强制使用CGLIB代理

若要强制使用过CGLIB代理，将
### xml配置：
    <aop:config proxy-target-class = "true">

### java配置：
    @EnableAspectJAutoProxy(proxyTargetClass = true)

    
## 注解的方式实现AOP
要增强的目标类：

    @Component
    public class TestAopTarget {
        //现有的类的方法，需要增强这个方法，但是不修改这个源码
        public void sayHello(){
            System.out.println("hello world");
        }
    }

增强后的类：
    
    @Aspect
    @Component
    public class AopBean {
        private long t;
    
        @Pointcut("execution(public void cn.ycl.study.aop.bean.TestAopTarget.sayHello())")
        public void say(){
        }
    
        @Before("say()")
        public void before(){
            t = System.currentTimeMillis();
            System.out.println("before:" + t);
        }
    
        @After("say()")
        public void after(){
            Long end = System.currentTimeMillis();
            t = end - t;
            System.out.println("after,end =" + end + "间隔：" + t);
        }
    }
    
调用客户端：
    
    public static void main(String[] args) {
        ApplicationContext context = getAnnotationContext();
        TestAopTarget target = context.getBean(TestAopTarget.class);
        target.sayHello();
    }
    
### 不同通知类型之间的变量传递
spring aop可以让我们在要增强的消息前加方法增强，也能在要增强的目标方法之后增强，比如性能检测组件，执行前记录当前时间，方法执行后当前时间减去执行前的时间就是方法调用时间。
那么涉及到两个方法之间变量的处理。
我们可以将这个变量作为**增强类的成员变量**，那么这个变量在通知的方法中就是共享的。

### 使用总结
1,启动AspectJ支持后
2,编写增强类，增强类加上@AspectJ注解
3，增强类的方法中配置切面 @Pointcut 注解
4，配置通知类型，@Before ，@After， @Around 等，增强切面
5，调用增强的目标方法，会直接执行增强后代理的对象，及增强功能的对象

## XML配置的方式实现AOP

