package com.less.classloader;

import java.net.*;

public class CloveURLClassLoader extends URLClassLoader{

    private JarURLConnection cachedJarFile = null;

    public CloveURLClassLoader(){
        super(new URL[] {}, findParentClassLoader());
    }

    public CloveURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public CloveURLClassLoader(URL[] urls) {
        super(urls);
    }

    public CloveURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }
    /**
     * 将指定的文件url添加到类加载器的classpath中去，并缓存jar connection，方便以后卸载jar
     * 一个可想类加载器的classpath中添加的文件url
     * @param
     */
    public void addURLFile(URL file) {
        try {
            // 打开并缓存文件url连接
            URLConnection uc = file.openConnection();
            if (uc instanceof JarURLConnection) {
                uc.setUseCaches(true);
                ((JarURLConnection) uc).getManifest();
                cachedJarFile = (JarURLConnection)uc;
            }
        } catch (Exception e) {
            System.err.println("Failed to cache plugin JAR file: " + file.toExternalForm());
        }
        addURL(file);
    }


    public void unloadJarFile(String url) throws MalformedURLException {
        URL jarUrl = new URL("jar:file:/"+url+"!/");

        JarURLConnection jarURLConnection = cachedJarFile;
        if(jarURLConnection==null){
            return;
        }
        try {
            System.err.println("Unloading plugin JAR file " + jarURLConnection.getJarFile().getName());
            jarURLConnection.getJarFile().close();
            jarURLConnection=null;
//            System.gc();
        } catch (Exception e) {
            System.err.println("Failed to unload JAR file\n"+e);
        }
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
}