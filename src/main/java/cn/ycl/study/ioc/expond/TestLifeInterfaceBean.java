package cn.ycl.study.ioc.expond;


public class TestLifeInterfaceBean{
    private String desc;

    public void afterPropertiesSet() throws Exception {
        System.out.println("开始执行自定义的初始化扩展方法");
        System.out.println("desc=" + desc);
        desc = desc + "自定义初始化";
    }

    public void destroy() throws Exception {
        System.out.println("bean销毁，开始执行扩展的销毁方法");
        System.out.println("desc=" + desc);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
