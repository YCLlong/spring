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

## 字面值
Spring的表达式可以将文字解析成值

    ExpressionParser parser = new SpelExpressionParser();
    // evals to "Hello World"
    String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();
    double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();
    // evals to 2147483647
    int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();
    boolean trueValue = (Boolean) parser.parseExpression("true").getValue();
    Object nullValue = parser.parseExpression("null").getValue();
    
## 属性
当我们想使用一个对象的属性的值时，表达式中可以直接使用 点号应用属性
    
    int year = (Integer) parser.parseExpression("Birthdate.Year + 1900").getValue(context);
    String city = (String) parser.parseExpression("placeOfBirth.City").getValue(context);
    
## 数组和List
数组和集合可以直接使用 方括号引用元素 [] 

    ExpressionParser parser = new SpelExpressionParser();
    
    // Inventions Array
    StandardEvaluationContext teslaContext = new StandardEvaluationContext(tesla);
    
    // evaluates to "Induction motor"
    String invention = parser.parseExpression("inventions[3]").getValue(
            teslaContext, String.class);
    
    // Members List
    StandardEvaluationContext societyContext = new StandardEvaluationContext(ieee);
    
    // evaluates to "Nikola Tesla"
    String name = parser.parseExpression("Members[0].Name").getValue(
            societyContext, String.class);
    
    // List and Array navigation
    // evaluates to "Wireless communication"
    String invention = parser.parseExpression("Members[0].Inventions[6]").getValue(
            societyContext, String.class);
            
数组类似于 java语法，可以赋初始值
    
    int[] numbers1 = (int[]) parser.parseExpression("new int[4]").getValue(context);
    
    // Array with initializer
    int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue(context);
    
    // Multi dimensional array
    int[][] numbers3 = (int[][]) parser.parseExpression("new int[4][5]").getValue(context);
            
## map
通过Map的key找到value。可以获取值也可以设置值

    Inventor pupin = parser.parseExpression("Officers['president']").getValue(
            societyContext, Inventor.class);
    
    // evaluates to "Idvor"
    String city = parser.parseExpression("Officers['president'].PlaceOfBirth.City").getValue(
            societyContext, String.class);
    
    // setting values
    parser.parseExpression("Officers['advisors'][0].PlaceOfBirth.Country").setValue(
            societyContext, "Croatia");

## 内联集合
在表达式内部定义的集合就是内联集合，内联集合写法和json机会一样
    
    List numbers = (List) parser.parseExpression("{1,2,3,4}").getValue(context);
    List listOfLists = (List) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue(context);、、、
    Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context);
    Map mapOfMaps = (Map) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue(context);
    
## 方法
表达式执行方法和java执行方法一样的