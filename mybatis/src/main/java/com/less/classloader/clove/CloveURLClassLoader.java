package com.less.classloader.clove;

import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class CloveURLClassLoader extends URLClassLoader {

    private JarURLConnection cachedJarFile = null;

    private final static ConcurrentHashMap<URL, JarURLConnection> CACHE_JARS = new ConcurrentHashMap<>();


    public CloveURLClassLoader(){
        super(new URL[] {}, findParentClassLoader());
    }

    public CloveURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, findParentClassLoader());
    }

    public CloveURLClassLoader(URL[] urls) {
        super(urls);
    }

    public CloveURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, findParentClassLoader(), factory);
    }

    private static ClassLoader findParentClassLoader() {
        ClassLoader parent = CloveURLClassLoader.class.getClassLoader();
        if (parent == null) {
            parent = CloveURLClassLoader.class.getClassLoader();
        }
        if (parent == null) {
            parent = ClassLoader.getSystemClassLoader();
        }
        return parent;
    }

    public void addURLFile(URL file) {
        try {
            cachedJarFile = CACHE_JARS.get(file);
            if(null == cachedJarFile){
                // 打开并缓存文件url连接
                URLConnection uc = file.openConnection();
                if (uc instanceof JarURLConnection) {
                    uc.setUseCaches(true);
                    ((JarURLConnection) uc).getManifest();
                    cachedJarFile = (JarURLConnection)uc;
                    CACHE_JARS.put(file,cachedJarFile);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to cache plugin JAR file: " + file.toExternalForm());
        }
        addURL(file);
    }


    public void unloadJarFile(String url) throws MalformedURLException {
        URL jarUrl = new URL("jar:file:/"+url+"!/");
        JarURLConnection jarURLConnection = CACHE_JARS.get(jarUrl);
        if(null == jarURLConnection){
            return;
        }
        try {
            System.err.println("Unloading plugin JAR file " + jarURLConnection.getJarFile().getName());
            jarURLConnection.getJarFile().close();
            jarURLConnection = null;
            CACHE_JARS.remove(jarUrl);
        } catch (Exception e) {
            System.err.println("Failed to unload JAR file\n"+e);
        }
    }


    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name, false);
    }

}