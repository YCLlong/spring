package cn.ycl.study.redis;


import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import redis.clients.jedis.Jedis;

import java.io.IOException;

@ComponentScan
public class Main {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        Jedis jedis = context.getBean(Jedis.class);
        ObjectMapper mapper = context.getBean(ObjectMapper.class);

        Person p = new Person("燕成龙",24);
        jedis.set("userName-燕成龙",mapper.writeValueAsString(p));


        String value = jedis.get("userName-燕成龙");
        //反序列化一定要无参构造函数
        Person person = mapper.readValue(value,Person.class);
        System.out.println(person);
        jedis.close();
    }
}
