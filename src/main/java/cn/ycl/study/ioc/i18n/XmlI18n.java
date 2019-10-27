package cn.ycl.study.ioc.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Locale;

public class XmlI18n {
    public static void main(String[] args) {
        MessageSource messageSource = getMessageSource();
        String msgUs = messageSource.getMessage("tip", new Object[]{"name", "code"}, Locale.US);
        System.out.println(msgUs);
        String msgChina = messageSource.getMessage("tip", new Object[]{"姓名", "编号"}, Locale.CHINA);
        System.out.println(msgChina);
    }

    public static MessageSource getMessageSource() {
        MessageSource source = new ClassPathXmlApplicationContext("classpath:sping-context.xml");
        return source;
    }
}
