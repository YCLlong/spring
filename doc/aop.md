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
环绕通知[Around advice]：环绕通知围绕在连接点前后，比如一个方法调用的前后。这是最

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

## Spring 自动检测AOP

