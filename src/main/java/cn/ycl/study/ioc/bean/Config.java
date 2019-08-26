package cn.ycl.study.ioc.bean;

public class Config {
    private String name;
    private String desc;

    public Config(String name,String desc){
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
