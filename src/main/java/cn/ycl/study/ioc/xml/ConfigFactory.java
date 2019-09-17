package cn.ycl.study.ioc.xml;

import cn.ycl.study.ioc.xml.bean.Config;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class ConfigFactory implements BeanFactoryPostProcessor{

    public static Config createConfig(){
        Config config = new Config();
       // config.setName("静态工厂方法创建");
        return config;
    }

    /**
     * 修饰符 public或者privite都能正常的返回对象
     * @return
     */
    public Config createConfigInstance(){
        Config config = new Config();
     //   config.setName("实例工厂方法创建");
        return config;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String name:beanFactory.getBeanDefinitionNames()){
            System.out.println("ioc:" + name);
            Object test =  beanFactory.getBean(name);
            if(test instanceof HelloIOC){
                HelloIOC helloIOC = (HelloIOC) test;
                helloIOC.hello();
            }

        }


    }
}
