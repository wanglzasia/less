package com.less.classloader.clove;

import java.io.*;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CloveClassLoader extends ClassLoader{


    CloveURLClassLoader urlClassLoader = null;

    List<String> jars = null;

    public CloveClassLoader(){
        urlClassLoader = new CloveURLClassLoader();
        jars = new ArrayList<String>();
    }

    public void addJars(List<String> jars) throws Exception{
        for(String jar:jars){
            addJar(jar);
        }
        jars.addAll(jars);
    }

    public void addJar(String jarName) throws Exception{
        addURL(jarName);
        jars.add(jarName);
    }

    private void addURL(String jarName) throws Exception {
        URL jarUrl = new URL("jar:file:/"+jarName+"!/");
        urlClassLoader.addURLFile(jarUrl);
    }

    public void loadClass() throws Exception{
        String className = "";
        for(String jarName:jars) {
            //遍歷jar裡面的文件
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(jarName);
                Enumeration<JarEntry> en = jarFile.entries();
                while (en.hasMoreElements()) {
                    JarEntry je = en.nextElement();
                    String name = je.getName();
                    String s5 = name.replace('/', '.');
                    if (s5.lastIndexOf(".class") > 0) {
                        className = je
                                .getName()
                                .substring(0, je.getName().length() - ".class".length())
                                .replace('/', '.');

                        Class claz = loadClass(className);

                        System.out.println(claz);
                    }
                }
            } catch (Exception e) {
                System.out.println("error"+className);
                e.printStackTrace();
            }
        }
    }

    public Class loadClass(String className) throws ClassNotFoundException {
        return urlClassLoader.loadClass(className);
    }
    /*
    public Class loadClass(String jarName,String className) throws ClassNotFoundException {
        // 是否已经加载了
        Class t = this.findLoadedClass(className);
        if(t == null) {
            // 交给双亲
            t = this.getParent().loadClass(className);
        }
        if(t == null) {
            // 双亲都不行，只能靠自己了
            t = this.findClass(jarName,className);
        }
        return t;
    }
    */

    // 交给子类自己去实现
    public Class findClass(String jarName,String className) throws ClassNotFoundException {
        return urlClassLoader.loadClass(className);
       /*
        byte[] data = new byte[0];        //读取二进制数据文件
        try {
            data = this.loadClassData(jarName,className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data != null) {       //读取到了
            return super.defineClass(className, data, 0, data.length);
        }
        return null;
        */
    }





    /*
    public Class loadClass(String jarName,String name,String className) throws ClassNotFoundException {

        readFile(jarName,name,0);

        //claz = defineClass(name, classData, 0, classData.length);

        return urlClassLoader.loadClass(className);
    }*/


    public void unloadJarFile(String jarName) throws MalformedURLException {
        urlClassLoader.unloadJarFile(jarName);
    }

    public void unloadJar() throws MalformedURLException{
        for(String jar:jars){
            unloadJarFile(jar);
        }
    }


    //////





    public String readFile(String filePath, String entryName, Integer index) {
        InputStream in = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            in = getJarInputStream(filePath, entryName);
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String con = null;
            sb = new StringBuffer();
            while ((con = br.readLine()) != null) {

                //if (before <= row && row < after) {

                    sb.append(con);
                //}
            }

            System.out.println(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (in != null)
                    in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public InputStream getJarInputStream(String filePath, String name)
            throws Exception {
        URL url = new URL("jar:file:" + filePath + "!/" + name);
        JarURLConnection jarConnection = (JarURLConnection) url
                .openConnection();
        InputStream in = jarConnection.getInputStream();

        return in;
    }

    private byte[] loadClassData(String jarName,String name) throws Exception {     //通过文件进行类的加载
        //String path = "D:\\Project\\java\\idea\\less\\mybatis\\target\\classes\\com\\less\\classloader\\"+ "Message.class";


        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        byte[] result = null;
        try {
            inputStream = new FileInputStream(new File(jarName));
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


