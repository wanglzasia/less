package com.less.classloader.clove2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class Clove2ClassLoader extends ClassLoader {


    // 加载入口，定义了双亲委派规则
    public Class loadClass(String name) throws ClassNotFoundException {
        // 是否已经加载了
        Class t = this.findLoadedClass(name);
        if(t == null) {
            // 交给双亲
            t = this.getParent().loadClass(name);
        }
        if(t == null) {
        // 双亲都不行，只能靠自己了
            t = this.findClass(name);
        }
        return t;
    }


    // 交给子类自己去实现
    public Class findClass(String className) {
        byte[] data = new byte[1024];        //读取二进制数据文件
        try {
            data = this.loadClassData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data != null) {       //读取到了
            return super.defineClass(className, data, 0, data.length);
        }
        return null;
    }



    private byte[] loadClassData() throws Exception {     //通过文件进行类的加载
        String path = "D:\\Project\\java\\idea\\less\\mybatis\\target\\classes\\com\\less\\classloader\\"+ "Message.class";
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        byte[] result = null;
        try {
            inputStream = new FileInputStream(new File(path));
            outputStream = new ByteArrayOutputStream();
            byte data[] = new byte[1024];       //进行读取
            int len;
            while ((len = inputStream.read(data)) != -1){       //将数据信息读取到内存中
                outputStream.write(data,0,len);
            }

            result = outputStream.toByteArray();      //将所有读取到的字节数据取出
            System.out.println("result: " + result.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        Clove2ClassLoader loader = new Clove2ClassLoader();
        Class aClass = loader.loadClass("com.less.classloader.Message");
        Object o = aClass.newInstance();
        Method send = aClass.getDeclaredMethod("send");
        send.invoke(o);
    }
}
