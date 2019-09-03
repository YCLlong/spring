package cn.ycl.study.ioc;

import cn.ycl.study.ioc.bean.*;
import cn.ycl.study.ioc.expond.TestLifeInterfaceBean;
import cn.ycl.study.ioc.methoddi.BeanA;
import cn.ycl.study.ioc.methoddi.BeanB;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    /**
     * 创建IOC容器实例对象，并通过容器对象创建Bean实例对象
     */
    public void createIOC(){
        //1，创建ioc容器对象需要提供配置元数据，sping-context.xml 是xml形式的元数据配置方式
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sping-context.xml");
        /**
         * 2，通过ioc容器对象创建Bean对象。前提是，在元数据中，已经配置了bean信息
         *    <bean id="helloIOC" class="cn.ycl.study.ioc.HelloIOC"></bean>
         */
        HelloIOC helloIOC = context.getBean(HelloIOC.class);
        helloIOC.hello();
    }

    /**
     * 通过BeanFactory对象注册bean，并不通过配置元数据配置bean
     */
    public void registerBean(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sping-context.xml");

        Config config = new Config();
        context.getBeanFactory().registerSingleton("config",config);

        Config bean = context.getBean(Config.class);
        System.out.println(bean.getName());

    }

    public void beanName(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sping-context.xml");
        HelloIOC t1 = (HelloIOC) context.getBean("h1");
        HelloIOC t2 = (HelloIOC) context.getBean("h11");
        HelloIOC t3 = (HelloIOC) context.getBean("h2");
        HelloIOC t4 = (HelloIOC) context.getBean("h3");
        System.out.println(t1==t2);//true
        System.out.println(t1==t2);//true
        System.out.println(t1==t3);//true
        System.out.println(t1==t4);//true


    }

    /**
     * 无参构造函数创建对象
     */
    public void createByConstructor(){
        ApplicationContext context = getContrext();
        context.getBean("config1");
    }

    /**
     * 静态工厂方法创建Bean实例
     */
    public void createByStaticFactoryMethod(){
        ApplicationContext context = getContrext();
        Config config = (Config) context.getBean("config2");
        System.out.println(config);
    }

    /**
     * 实例工厂创建Bean实例
     */
    public void createByFactoryInstance(){
        ApplicationContext context = getContrext();
        Config config = (Config) context.getBean("config3");
        System.out.println(config);
    }

    public void constructorArgsDi(){
        ApplicationContext context = getContrext();
        Config config = (Config) context.getBean("config4");
        System.out.println(config);
    }
    /**
     * setter方式的依赖注入
     */
    public void setterDi(){
        ApplicationContext context = getContrext();
        Config config = (Config) context.getBean("config7");
        System.out.println(config);
    }

    /**
     * 复杂属性注入
     */
    public void complexPropertiesDi(){
        ApplicationContext context = getContrext();
        ValueConfig config = (ValueConfig) context.getBean("config8");
        System.out.println(config);
    }

    /**
     * 自动装配
     */
    public void autowire(){
        ApplicationContext context = getContrext();
        AutowireConfig config = (AutowireConfig) context.getBean("config9");
        System.out.println(config);
    }

    /**
     * 方法注入
     */
    public void methodDi(){
        ApplicationContext context = getContrext();
        BeanA beanA = context.getBean(BeanA.class);
        BeanB b1 = beanA.getBeanB();
        BeanB b2 = beanA.getBeanB();
        System.out.println(b1 == b2);//结果是false
    }

    public void replaceDi(){
        ApplicationContext context = getContrext();
        BeanB beanB = (BeanB) context.getBean("replaceBeanB");
        String desc = beanB.getDesc();
        System.out.println(desc);
    }

    /**
     * bean声明周期扩展
     */
    public void expondLifeInterface(){
        ApplicationContext context = getContrext();
        TestLifeInterfaceBean bean = (TestLifeInterfaceBean) context.getBean("expondLifeInterface");
        //按照猜想，得到单例的bean后，将它指向null。后面从容器获取这个bean时理论上是Null。但是结果是配置数据装配后的bean
        bean = null;
        System.gc();
        TestLifeInterfaceBean bean1 = (TestLifeInterfaceBean) context.getBean("expondLifeInterface");
        TestLifeInterfaceBean bean2 = (TestLifeInterfaceBean) context.getBean("expondLifeInterface");
        System.out.println(bean1 == bean2);//结果返回true
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void lifeConfig(){
        ApplicationContext context = getContrext();
        BeanLife lifecycle = context.getBean(BeanLife.class);
        System.out.println("程序执行完毕");
    }

    public void beanNameAware(){
        ApplicationContext context = getContrext();
        BeanNameAwareTest lifecycle = context.getBean(BeanNameAwareTest.class);
    }

    public void registerBeanTest(){
        ApplicationContext context = getContrext();
        context.getBean(RegisterBean.class);
    }

   public static void main(String[] args) {
        Main main = new Main();
        main.lifeConfig();
    }

    public static ApplicationContext getContrext(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:sping-context.xml");
        //注册回调钩子，可以保证程序关闭时，能够回调bean的destory方法，从而释放资源
        context.registerShutdownHook();
        return context;
    }
}
