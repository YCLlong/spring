package cn.ycl.study.ioc.annotation;

import org.springframework.stereotype.Component;

@Component
public class XmlToAnnotationBean {
    private String desc;

    public XmlToAnnotationBean() {
        desc = "test desc";
    }

    public void show() {
        System.out.println("XmlToAnnotationBean show");
    }
}
