package cn.ycl.study.ioc.bean;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ValueConfig {
    private List<String> stringList;
    private List<Config> configList;
    private Map<String,String> stringMap;
    private Map<String,Config> configMap;
    private Set<String> stringSet;
    private Set<Config> configSet;
    private String[] strArray;
    private Config[] configArray;
    private Properties prop;

    public ValueConfig(){}

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<Config> getConfigList() {
        return configList;
    }

    public void setConfigList(List<Config> configList) {
        this.configList = configList;
    }

    public Map<String, String> getStringMap() {
        return stringMap;
    }

    public void setStringMap(Map<String, String> stringMap) {
        this.stringMap = stringMap;
    }

    public Map<String, Config> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, Config> configMap) {
        this.configMap = configMap;
    }

    public Set<String> getStringSet() {
        return stringSet;
    }

    public void setStringSet(Set<String> stringSet) {
        this.stringSet = stringSet;
    }

    public Set<Config> getConfigSet() {
        return configSet;
    }

    public void setConfigSet(Set<Config> configSet) {
        this.configSet = configSet;
    }

    public String[] getStrArray() {
        return strArray;
    }

    public void setStrArray(String[] strArray) {
        this.strArray = strArray;
    }

    public Config[] getConfigArray() {
        return configArray;
    }

    public void setConfigArray(Config[] configArray) {
        this.configArray = configArray;
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }
}
