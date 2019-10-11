package cn.ycl.study.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
