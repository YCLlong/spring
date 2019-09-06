package cn.ycl.study.annotation.comonentscanfilter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM,value = MyScanFilter.class)})
public class ConfigApp {
}
