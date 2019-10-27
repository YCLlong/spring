package cn.ycl.study.ioc.annotation.comonentscanfilter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM, value = cn.ycl.study.annotation.comonentscanfilter.MyScanFilter.class)})
public class ConfigApp {
}
