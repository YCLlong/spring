Spring的表达式语言SpEL是SpringFramework 和核心部分 core
Spring Expression Language（简称“SpEL”）是一种强大的表达式语言，**支持在运行时查询和操作对象**。语言语法类似于Unified EL，但提供了其他功能，**最有名的是方法调用和基本字符串模板功能**。
尽管还有其他几种Java表达式语言，例如OGNL, MVEL, and JBoss，但SpEL只是为了向Spring社区提供一种支持良好的表达式语言，开发者可以在所有用到Spring框架的产品中使用SpEL。 其语言特性是由使用Spring框架项目的需求所驱动的

# 表达式语言支持以下功能:
文字表达
布尔和关系运算符
正则表达式
类表达式
访问属性，数组，list和maps
方法调用
关系运算符
声明
调用构造器
bean的引用
数组的构造
内嵌的list
内嵌的map
三元表达式
变量
用户自定义函数
集合映射
集合选择
模板表达式

> 我们编写的代码需要按照语言规范要求严格编写，不然会导致编译不通过，像java语言，写好的代码需要编译成字节码文件，再运行。java提供了比如动态代理的机制，允许我们在程序运行时“动态替换”代码
动态代理感觉扩展性很好。但是Spring表达式提供了非常强大的功能，以上的功意味着我们可以通过动态修改表达式（字符串）就能动态的编写代码（拼接字符串），这个扩展性是非常的吓人的！！！

## 使用表达式
Spring表达式使用非常简单，大致分3
1，创建表达式解析器对象     ExpressionParser 
2，创建表达式对象           通过解析器对象解析表达式返回表达式对象
3，获取解析结果             通过表达式对象获取解析结果

    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("'hello world'");
        System.out.println( expression.getValueType()); //class java.lang.String
        System.out.println(expression.getValue());// hello world
    }
