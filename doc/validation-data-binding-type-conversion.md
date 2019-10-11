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

## BeanWrapper

    public static void main(String[] args) {
        BeanWrapper classWrapper = new BeanWrapperImpl(new MyClass());
        //设置属性
        classWrapper.setPropertyValue("name","天堂教室");
        //还能这样设置
        PropertyValue noValue = new PropertyValue("no",1001);
        classWrapper.setPropertyValue(noValue);
        //获取属性值
        classWrapper.getPropertyValue("name");
    }