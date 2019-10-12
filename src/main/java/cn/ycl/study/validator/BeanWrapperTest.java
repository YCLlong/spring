package cn.ycl.study.validator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;

import java.util.ArrayList;
import java.util.HashMap;

public class BeanWrapperTest {

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
}
