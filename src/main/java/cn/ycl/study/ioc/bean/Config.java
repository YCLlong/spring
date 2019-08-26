package cn.ycl.study.ioc.bean;

import cn.ycl.study.ioc.HelloIOC;

public class Config {
    private String name;
    private HelloIOC ioc;
    private int version;

    public Config() {
    }

    public Config(String name, HelloIOC ioc, int version) {
        this.name = name;
        this.ioc = ioc;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HelloIOC getIoc() {
        return ioc;
    }

    public void setIoc(HelloIOC ioc) {
        this.ioc = ioc;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
