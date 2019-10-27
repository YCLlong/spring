package cn.ycl.study.ioc.annotation.qulifier;

import org.springframework.beans.factory.annotation.Autowired;

public class QulifierTest {

    public SelfQulifier qulifier;

    //由于在ioc容器中声明了两个类型一致的SelfQulifier bean,这样自动装配会直接报错
    @Autowired
    //使用自定义限定符注解，会从配置元数据中读取限定符信息。如果读取到多个相同类型具有相同的限定符信息时也会报错
    @Offline(name = "test")
    private void test(SelfQulifier qulifier) {
        this.qulifier = qulifier;
    }

    @Autowired
    @Offline(name = "test", level = 1)
    private void test1(SelfQulifier qulifier) {
        this.qulifier = qulifier;
    }
}
