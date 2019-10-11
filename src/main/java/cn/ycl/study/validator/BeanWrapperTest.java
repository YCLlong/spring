package cn.ycl.study.validator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;

public class BeanWrapperTest {

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
}
