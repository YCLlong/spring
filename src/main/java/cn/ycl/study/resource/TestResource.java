package cn.ycl.study.resource;

import org.springframework.core.io.UrlResource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class TestResource {
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

}
