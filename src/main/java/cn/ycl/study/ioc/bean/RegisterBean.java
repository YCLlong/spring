package cn.ycl.study.ioc.bean;

import cn.ycl.study.ioc.HelloIOC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

public class RegisterBean implements BeanDefinitionRegistryPostProcessor {
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for(int i=0; i<10; i++){
            BeanDefinitionBuilder db =  BeanDefinitionBuilder.rootBeanDefinition(HelloIOC.class);
            db.addPropertyValue("desc","动态注册bean" + i);
            db.addConstructorArgValue("构造函数注入");
            registry.registerBeanDefinition("testRegisterBean" + i,db.getBeanDefinition());
        }
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String name:beanFactory.getBeanDefinitionNames()){
            System.out.println("registerBean:" + name);
        }
    }
}
