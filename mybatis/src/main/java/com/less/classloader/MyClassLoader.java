package com.less.classloader;

import java.io.*;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader{

    private static final String MESSAGE_CLASS_PATH = "D:\\Project\\java\\idea\\less\\mybatis\\target\\classes\\com\\less\\classloader\\"+ "Message.class";

    /**
     * 进行制定类的加载
     *
     * @param className 类的完整名称: XX包.XX类
     * @return 返回一个指定类的Class对象
     * @throws Exception 如果类文件不存在,则无法加载
     */
    public Class<?> loadData(String className) throws Exception {
        byte[] data = this.loadClassData();        //读取二进制数据文件
        if (data != null) {       //读取到了
            return super.defineClass(className, data, 0, data.length);
        }
        return null;
    }

    private byte[] loadClassData() throws Exception {     //通过文件进行类的加载
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        byte[] result = null;
        try {
            inputStream = new FileInputStream(new File(MESSAGE_CLASS_PATH));
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
}

class TestClassLoader {
    public static void main(String[] args) throws Exception {
        MyClassLoader loader = new MyClassLoader();     //自定义类加载器
        Class<?> aClass = loader.loadData("com.less.classloader.Message");//进行类的机载
        System.out.println(aClass);
        System.out.println(aClass.getClassLoader());
        System.out.println(aClass.getClassLoader().getParent());
        System.out.println(aClass.getClassLoader().getParent().getParent());
        System.out.println(aClass.getClassLoader().getParent().getParent().getParent());

        Object o = aClass.newInstance();
        Method send = aClass.getDeclaredMethod("send");
        send.invoke(o);
    }
}
