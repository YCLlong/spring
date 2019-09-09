package cn.ycl.study.ioc.xml.methoddi;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

/**
 * BeanA 的getDesc方法替换的类
 * 需要实现接口 MethodReplacer
 */
public class ReplaceBeanA implements MethodReplacer {
    public Object reimplement(Object o, Method method, Object[] objects) throws Throwable {
        System.out.println(o);
        System.out.println(method.getName());
        return "我是替换之后的值";
    }
}
