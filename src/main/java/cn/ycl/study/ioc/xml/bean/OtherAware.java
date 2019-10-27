package cn.ycl.study.ioc.xml.bean;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.*;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;

public class OtherAware implements NotificationPublisherAware, LoadTimeWeaverAware, ApplicationEventPublisherAware, BeanClassLoaderAware, BeanFactoryAware, BeanNameAware, ResourceLoaderAware, MessageSourceAware {

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

    }

    public void setBeanClassLoader(ClassLoader classLoader) {

    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }

    public void setBeanName(String name) {

    }

    public void setResourceLoader(ResourceLoader resourceLoader) {

    }

    public void setMessageSource(MessageSource messageSource) {

    }

    public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {

    }

    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {

    }
}
