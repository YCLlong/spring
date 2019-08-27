package cn.ycl.study.ioc;

import cn.ycl.study.ioc.bean.Config;

public class ConfigFactory {

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
}
