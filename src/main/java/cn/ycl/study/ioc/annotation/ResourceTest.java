package cn.ycl.study.ioc.annotation;

import cn.ycl.study.ioc.annotation.comonentscanfilter.MyScanFilter;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Configuration
public class ResourceTest {
    private String desc;

    @PostConstruct
    private void init(){
        desc = "123";
    }

}
