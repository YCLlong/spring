# Resource 资源
所谓资源，可谓是所有的二进制文件都称之为资源，视屏，音频，图片，txt文档，xml配置文件等都是资源。

Spring封装了资源对象

    public interface Resource extends InputStreamSource {
        boolean exists();
        boolean isOpen();
        URL getURL() throws IOException;
        File getFile() throws IOException;
        Resource createRelative(String relativePath) throws IOException;
        String getFilename();
        String getDescription();
    }
    
    public interface InputStreamSource {
        InputStream getInputStream() throws IOException;
    }
    
 我们发现，Resource对象其实底层还是对File对象的一些包装
 
    getInputStream(): 用于定位和打开当前资源, 返回当前资源的InputStream ，预计每一次调用都会返回一个新的InputStream. 因此调用者必须自行关闭当前的输出流.
    exists(): 返回boolean值，表示当前资源是否存在。
    isOpen(): 返回boolean值，表示当前资源是否有已打开的输入流。如果为 true，那么InputStream不能被多次读取 ，只能在一次读取后即关闭以防止内存泄漏。除了InputStreamResource外，其他常用Resource实现都会返回false。
    getDescription(): 返回当前资源的描述，当处理资源出错时，资源的描述会用于输出错误的信息。一般来说，资源的描述是一个完全限定的文件名称，或者是当前资源的真实URL。
    
## 内置的资源实现
Spring内置实现了多种资源，比如来源于网络，ftp服务器，文件系统，类路径，输入流甚至是字节数组 等等
 
### UrlResource
UrlResource 封装了java.net.URL用来访问正常URL的任意对象。例如file: ，HTTP目标，FTP目标等。所有的URL都可以用标准化的字符串来表示，例如通过正确的标准化前缀。 可以用来表示当前URL的类型。 这包括file:，用于访问文件系统路径，http: ：用于通过HTTP协议访问资源，ftp:：用于通过FTP访问资源，以及其他。

### ClassPathResource
ClassPathResource代表从类路径中获取资源，它使用线程上下文加载器，指定类加载器或给定class类来加载资源。

### FileSystemResource
FileSystemResource是用于处理java.io.File和java.nio.file.Path的实现，显然，它同时能解析作为File和作为URL的资源。

### ServletContextResource
这是ServletContext资源的 Resource实现，用于解释相关Web应用程序根目录中的相对路径。

### InputStreamResource
InputStreamResource是针对InputStream提供的Resource实现。在一般情况下，如果确实无法找到合适的Resource实现时，才去使用它。 同时请优先选择ByteArrayResource或其他基于文件的Resource实现，迫不得已的才使用它。

###  ByteArrayResource
这是给定字节数组的Resource实现。 它为给定的字节数组创建一个ByteArrayInputStream。
当需要从字节数组加载内容时，ByteArrayResource会是个不错的选择，无需求助于单独使用的InputStreamResource。

### 示例
例如：下载一个网络资源
    
     public static void main(String[] args)  {
        try {
            UrlResource resource = new UrlResource("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570678764872&di=0031b573d574c9812f9a7d379160814f&imgtype=0&src=http%3A%2F%2Fi0.sinaimg.cn%2Fty%2F2014%2F1204%2FU11648P6DT20141204190014.jpg");
            BufferedOutputStream bot = new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\ycl\\Desktop\\p.jpg")));
            BufferedInputStream bin = new BufferedInputStream(resource.getInputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while ((len= bin.read(data)) != -1){
                bot.write(data,0,len);
            }
            bin.close();
            bot.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

Spring帮我们做的事情就是封装这个资源，我们提供路径就行了。拿到资源就也能拿到输入流，可以读取资源的内容，后继步骤就看我们怎么操作了。

## ResourceLoader接口
    public interface ResourceLoader {
        /** Pseudo URL prefix for loading from the class path: "classpath:". */
        String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;
        Resource getResource(String location);
        ClassLoader getClassLoader();
    }
这个接口 有两个方法，一个是getResource,一个是getClassLoader

ApplicationContext 接口继承了接口ResourcePatternResolver，而接口 ResourcePatternResolver又继承了 ResourceLoader

    public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
            MessageSource, ApplicationEventPublisher, ResourcePatternResolver 
		
    public interface ResourcePatternResolver extends ResourceLoader {
        String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
        Resource[] getResources(String locationPattern) throws IOException;
    }
 
 
> 所以Application对象可以通过getResource方法直接加载资源。资源协议解析器会根据资源路径，将资源类型动态的绑定到内置的资源类上从而加载对应的资源

例如：
针对ClassPathXmlApplicationContext，该代码返回 ClassPathResource。

    Resource template = ctx.getResource("some/resource/path/myTemplate.txt");
    
也可以从资源路径中直接指定：

    Resource template = ctx.getResource("classpath:some/resource/path/myTemplate.txt");
    Resource template = ctx.getResource("file:///some/resource/path/myTemplate.txt");
    Resource template = ctx.getResource("http://myhost.com/resource/path/myTemplate.txt");

### 资源路径解析总结

前缀 |	示例 |	解释
:-: | :-: | :-:
classpath:|	classpath:com/myapp/config.xml	| 从类路径加载
file: |	file:///data/config.xml	| 从文件系统加载为URL。 另请参见FileSystemResource 警告。
http: |	http://myserver/logo.png |	作为URL加载。
(none)|	/data/config.xml |	取决于底层的ApplicationContext。

> 资源路径没有前缀。 因此，因为应用程序上下文本身将用作ResourceLoader， 所以资源本身通过ClassPathResource，FileSystemResource或ServletContextResource加载，具体取决于上下文的确切类型。

## ResourceLoaderAware 接口
    public interface ResourceLoaderAware {
        void setResourceLoader(ResourceLoader resourceLoader);
    }
当类实现ResourceLoaderAware并部署到应用程序上下文（作为Spring管理的bean）时，它被应用程序上下文识别为ResourceLoaderAware。 然后，应用程序上下文调用setResourceLoader(ResourceLoader)，将其自身作为参数提供（请记住，Spring中的所有应用程序上下文都实现了ResourceLoader接口）。

> 这种写法会让Spring框架的API和我们的代码有耦合性，可以使用自动装配也能得到 ResourceLoader 对象

## 资源依赖

<bean id="myBean" class="...">
    <property name="template" value="some/resource/path/myTemplate.txt"/>
</bean>

在bean中有一个Resource类型的属性叫做template 就能这样加载

##  应用上下文和资源路径
    ApplicationContext ctx = new ClassPathXmlApplicationContext("conf/appContext.xml");
    ApplicationContext ctx = new FileSystemXmlApplicationContext("conf/appContext.xml");
    //这种写法可以加载xml配置文件的同时加载配置类
    ApplicationContext ctx = new ClassPathXmlApplicationContext(
        new String[] {"services.xml", "daos.xml"}, MessengerService.class);
