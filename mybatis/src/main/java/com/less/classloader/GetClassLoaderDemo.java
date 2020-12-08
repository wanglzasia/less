package com.less.classloader;



public class GetClassLoaderDemo {
    public static void main(String[] args) {
        ClassLoader loader = Message.class.getClassLoader();        //获取当前类的类加载器
        ClassLoader parent = loader.getParent();                    //获取父类加载器
        System.out.println(loader);
        System.out.println(parent);
        System.out.println(parent.getParent());

        System.out.println(loader.getClass().getPackage());

    }

    public class Message {
    }
}