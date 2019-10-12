# 数据校验
在web系统中，数据校验一般放到控制层，但是在业务逻辑中不应该参与数据的校验，并且验证不应该与Web层绑定，并且应该易于本地化，应该可以插入任何可用的验证器。 考虑到这些问题，Spring提出了一个基本的，非常有用的Validator接口，它在应用程序的每一层都可用。
## Validator接口
org.springframework.validation.Validator

    public interface Validator {
        boolean supports(Class<?> clazz);
        void validate(Object target, Errors errors);
    }

接口中两个方法很好理解，第一个是支持检测的类，第二个是校验，参数1是校验的目标对象，参数二是校验的结果，如果校验不通过就将错误信息保存到Errors对象中

> 校验器实现Validator接口即可

    public class PersonValidator implements Validator {
        public boolean supports(Class<?> clazz) {
            return clazz==Person.class;
        }
    
        public void validate(Object target, Errors errors) {
            Person person = (Person)target;
            if(person.getName() == null || person.getName().equals("")){
                errors.rejectValue("name","用户名不能为空");
            }
            if(person.getAge() < 0){
                errors.rejectValue("age","年龄太小");
            }
            if(person.getAge() > 120){
                errors.rejectValue("age","年龄过大");
            }
        }
    }

在SpringMVC中，Errors会被自动注入，直接使用校验器的validate方法就可以得到校验结果。
ValidationUtils 是一个校验工具类，可以研究一下

    校验器可以很好的校验基本属性，但是有时候可能我们要校验的对象A中又存在一个对象B，我们可以针对对象B封装一个校验器，然后在对象A的校验器中注入对象B的校验器。


# 属性绑定
SpringMVC中，可以直接将表单中的数据封账到java对象中，免去了我们从request中获取参数，然后set到对象中的步骤，非常方便

## BeanWrapper接口

使用反射技术可以很好的做到这点，Spring提供了BeanWrapper接口和实现类BeanWrapperImpl。我们通过BeanWrapperImpl对象可以非常的方便操作对象的属性。
而且没有属性嵌套的深度的限制
    
    //需要get和set
    public class MyClass {
        private String name;
        private Integer no;
        private List<Person> studentList;
        private Person teacher;
        private Map<String,Person> committee;
    }
    
    public static void main(String[] args) {
        MyClass myClass=new MyClass();
        BeanWrapper classWrapper = new BeanWrapperImpl(myClass);
        //设置基本属性
        classWrapper.setPropertyValue("name","天堂教室");
        //还能这样设置基本属性
        PropertyValue noValue = new PropertyValue("no",1001);
        classWrapper.setPropertyValue(noValue);

        //设置引用类型的对象的属性
        //classWrapper.setPropertyValue("teacher.name","张老师");//这样写法会直接报错,Value of nested property 'teacher' is null。
        //要先给teacher赋值
        classWrapper.setPropertyValue("teacher",new Person());
        //然后可以这样设置
        classWrapper.setPropertyValue("teacher.name","张老师");

        //或者这样设置
        BeanWrapper person = new BeanWrapperImpl(new Person());
        person.setPropertyValue("name","燕老师");
        classWrapper.setPropertyValue("teacher",person.getWrappedInstance());

        //给list或者数组赋值
        classWrapper.setPropertyValue("studentList",new ArrayList<Person>());
        classWrapper.setPropertyValue("studentList[0]",new Person("y0",0));
        classWrapper.setPropertyValue("studentList[1]",new Person("y1",1));

        //给map赋值
        classWrapper.setPropertyValue("committee",new HashMap<String,Person>());
        classWrapper.setPropertyValue("committee[班长]",new Person("燕成龙",23));
        classWrapper.setPropertyValue("committee[搞事课代表]",new Person("焦燕飞",23));

        //获取属性值
        String teacherName = (String) classWrapper.getPropertyValue("teacher.name");
        System.out.println(teacherName);
    }
    
Expression|Explanation
:-|:-
name	| 表示属性 name与getName()或isName()和setName(..)方法相对应
account.name	| 表示 account 属性的嵌套属性name与getAccount().setName() 或 getAccount().getName() 相对应.
account[2]	| 表示索引属性account的第_3_个属性. 索引属性可以是array, list, 其他自然排序的集合.
account[COMPANYNAME]	| 表示映射属性account被键COMPANYNAME 索引的映射项的值。也就是map。

我们可能觉得这个功能真是画蛇添足，对象我可以自己创建，可以直接set属性。但是要注意，我们创建对象我们设置属性有人工识别的因素
而这个类更多相当于是软件去控制.
比如web mvc 中属性的装配实现
1，使用反射技术获取访问方法的属性，然后获取这个属性的类型，如果不是基本类型，再获取这个对象的所有的变量名（递归）
2，通过request对象获取所有的parameter的key和value
3，通过BeanWrapperImpl设置属性

## PropertyEditor
Spring使用 PropertyEditor的概念来实现 Object和String之间的转换
> 比如Date对象，用字符串显示就很方便而且容读，比如byte[] 字节数据对象，用字符串表示就能清晰的看到对象的值

内置的属性编辑器

类 | 解释
:-|:-
ByteArrayPropertyEditor	| 字节数组的编辑器。 将字符串转换为其对应的字节表示形式。BeanWrapperImpl默认注册。
ClassEditor |	将表示类的字符串解析为实际的类，反之亦然。 找不到类时，抛出IllegalArgumentException。 默认情况下，由BeanWrapperImpl注册。
CustomBooleanEditor |	Boolean属性的可自定义属性编辑器。 默认情况下，由BeanWrapperImpl注册，但可以通过将其自定义实例注册为自定义编辑器来覆盖。
CustomCollectionEditor |	Collection的属性编辑器，将任何源Collection转换为给定的目标Collection类型。
CustomDateEditor | java.util.Date的可自定义属性编辑器，支持自定义DateFormat。 未默认注册。 必须根据需要使用适当的格式进行用户注册。
CustomNumberEditor |	任何Number 子类的可自定义属性编辑器，例如Integer, Long, Float或Double。 默认情况下，由BeanWrapperImpl注册，但可以通过将其自定义实例注册为自定义编辑器来覆盖。
FileEditor	| 将字符串解析为java.io.File对象。 默认情况下，由BeanWrapperImpl注册。
InputStreamEditor |	单向属性编辑器，可以获取字符串并生成（通过中间ResourceEditor和Resource）InputStream，以便InputStream属性可以直接设置为字符串。 请注意，默认用法不会为您关闭 InputStream。 默认情况下，由 BeanWrapperImpl注册。
LocaleEditor	| 可以将字符串解析为Locale对象，反之亦然（字符串格式为_[country]_[variant]，与Locale的 toString() 方法相同）。 默认情况下，由BeanWrapperImpl注册。
PatternEditor	| 可以将字符串解析为java.util.regex.Pattern对象，反之亦然。
PropertiesEditor	| 可以将字符串（使用 java.util.Properties类的javadoc中定义的格式进行格式化）转换为 Properties 对象。 默认情况下，由BeanWrapperImpl注册。
StringTrimmerEditor	| 修剪字符串的属性编辑器。 （可选）允许将空字符串转换为空值。 默认情况下未注册 - 必须是用户注册的。
URLEditor |	可以将URL的字符串表示形式解析为实际的URL 对象。 默认情况下，由BeanWrapperImpl注册。


        