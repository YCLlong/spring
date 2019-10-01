package cn.ycl.study.ioc.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class JDKI18N {

    public static void main(String[] args) {
        Locale locale = Locale.CHINA;
        /**
         * 配置文件的命名要求是，位置放在classpath下
         * basename_language_country.properties
         * 找不到配置文件就会报错 java.util.MissingResourceException
         */
        ResourceBundle bundle = ResourceBundle.getBundle("base", locale);
        //在配置文件中找key对应的字符
        String msg = bundle.getString("msg");
        System.out.println(msg);
    }
}
