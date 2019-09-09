package cn.ycl.study.ioc.xml.bean;

import cn.ycl.study.ioc.xml.HelloIOC;

public class AutowireConfig {
    private String desc;
    private HelloIOC ioc;

    /**
     * 假设ioc容器中已有HelloIOC bean
     * 如果构造函数只有这一个，那么就无法注入参数，desc和ioc都是null
     * @param desc
     * @param ioc
     */
    public AutowireConfig(String desc, HelloIOC ioc) {
        this.desc = desc;
        this.ioc = ioc;
    }

    /**
     * 在加上这个构造函数，就可以注入ioc
     * @param ioc
     */
   /* public AutowireConfig(HelloIOC ioc) {
        this.ioc = ioc;
    }*/
    public AutowireConfig(){}
    public HelloIOC getIoc() {
        return ioc;
    }

    public void setIoc(HelloIOC ioc) {
        this.ioc = ioc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
