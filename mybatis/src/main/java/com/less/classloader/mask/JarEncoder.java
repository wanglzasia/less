package com.less.classloader.mask;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;


public class JarEncoder extends ClassLoader {
    private JarInputStream jis;

    public JarEncoder(String src) throws FileNotFoundException, IOException {
        this(new FileInputStream(src));
    }

    public JarEncoder(File file) throws FileNotFoundException, IOException {
        this(new FileInputStream(file));
    }

    public JarEncoder(InputStream is) throws IOException {
        jis = new JarInputStream(is);
    }

    /**
     * 通过指定的路径输出加密后的jar
     * @param target
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void write(String target) throws FileNotFoundException, IOException {
        write(new FileOutputStream(target));
    }

    /**
     * 通过指定的文件输出加密后的jar
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void write(File file) throws FileNotFoundException, IOException {
        write(new FileOutputStream(file));
    }

    /**
     * 通过指定的输出流输出加密后的jar
     * @param os
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void write(OutputStream os) throws FileNotFoundException, IOException {
        Manifest menifest = jis.getManifest(); //获取jar的Manifest信息
        JarOutputStream jos = null;
        if(menifest == null) {
            jos = new JarOutputStream(os);
        } else {
            //JarInputStream的getNextJarEntry()方法无法获取Manifest信息，所以只能通过这种方式写入Manifest信息
            jos = new JarOutputStream(os, menifest);
        }

        JarEntry entry = null;
        while((entry = jis.getNextJarEntry()) != null) {
            jos.putNextEntry(new JarEntry(entry.getName()));
            //jos.putNextEntry(new ZipEntry(entry.getName()));
            if(entry.getName().endsWith(".class")) { //只加密class文件
                byte[] bytes = getBytes(jis); //读取class文件内容
                byte[] enbytes = Base64.encode(bytes).getBytes(); //加密后的信息
                jos.write(enbytes, 0, enbytes.length); //把加密后的信息写入流
            } else { //其他类型的文件直接写入流
                byte[] bytes = getBytes(jis);
                jos.write(bytes, 0, bytes.length);
            }
            jos.flush();
        }
        jos.close();
        jis.close();
    }

    /**
     * 从jar输入流中读取信息
     * @param jis
     * @return
     * @throws IOException
     */
    private byte[] getBytes(JarInputStream jis) throws IOException {
        int len = 0;
        byte[] bytes = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        while((len = jis.read(bytes, 0, bytes.length)) != -1) {
            baos.write(bytes, 0, len);
        }
        return baos.toByteArray();
    }
}
