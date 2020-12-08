package com.less.classloader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CloveClassLoader extends ClassLoader{


    private final static ConcurrentHashMap<String,CloveURLClassLoader> LOADER_CACHE = new ConcurrentHashMap<>();






    public void loadJar(String jarName) throws Exception {
        CloveURLClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
        if(urlClassLoader!=null){
            return;
        }
        urlClassLoader = new CloveURLClassLoader();
        String path = "D:\\Tools\\maven\\repository\\com\\clove\\clove-satp\\1.0";
        String jarFileDir = "D:\\Tools\\maven\\repository\\com\\clove\\clove-satp\\1.0\\clove-satp-1.0.jar";
        URL jarUrl = new URL("jar:file:/"+path+"/"+jarName+"!/");
        System.out.println("jar:file:/"+path+"/"+jarName+"!/");
        urlClassLoader.addURLFile(jarUrl);
        LOADER_CACHE.put(jarName,urlClassLoader);

        JarFile jarFile = null;
        Class c = null;
        try {
            jarFile = new JarFile(jarFileDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Enumeration<JarEntry> en = jarFile.entries();
        while (en.hasMoreElements()) {
            JarEntry je = en.nextElement();
            String name = je.getName();
            String s5 = name.replace('/', '.');
            if (s5.lastIndexOf(".class") > 0) {
                String className = je
                        .getName()
                        .substring(0, je.getName().length() - ".class".length())
                        .replace('/', '.');
                //System.out.println(className);
            }
        }
        Class claz = loadClass("clove-satp-1.0.jar","com.clove.satp.Test");
        System.out.println(claz);
    }


    public Class loadClass(String jarName,String name) throws ClassNotFoundException {
        CloveURLClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
        if(urlClassLoader==null){
            return null;
        }
        return urlClassLoader.loadClass(name);
    }

    public void unloadJarFile(String jarName) throws MalformedURLException {
        CloveURLClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
        if(urlClassLoader==null){
            return;
        }
        String path = "D:\\Tools\\maven\\repository\\com\\clove\\clove-satp\\1.0";
        String jarStr = "jar:file:/"+path+"/"+jarName+"!/";
        urlClassLoader.unloadJarFile(jarStr);
        urlClassLoader = null;
        LOADER_CACHE.remove(jarName);
    }
}
