package com.less.classloader.mask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class JarDecoder extends ClassLoader {
    private JarInputStream jis;
    private Map<String, ByteBuffer> entryMap;

    public JarDecoder(String src) throws FileNotFoundException, IOException, Base64DecodingException {
        this(new FileInputStream(src));
    }

    public JarDecoder(File file) throws FileNotFoundException, IOException, Base64DecodingException {
        this(new FileInputStream(file));
    }

    public JarDecoder(InputStream is) throws IOException, Base64DecodingException {
        jis = new JarInputStream(is);
        entryMap = new HashMap<String, ByteBuffer>();
        JarEntry entry = null;
        while((entry = jis.getNextJarEntry()) != null) {
            String name = entry.getName();
            if(name.endsWith(".class")) { //class文件解密后再缓存
                byte[] bytes = getBytes(jis); //读取class文件内容
                byte[] debytes = Base64.decode(bytes); //解密class文件内容
                ByteBuffer buffer = ByteBuffer.wrap(debytes); //把数据复制到ByteBuffer对象中
                entryMap.put(name, buffer); //缓存数据
            } else { //其他文件直接缓存
                byte[] bytes = getBytes(jis);
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                entryMap.put(name, buffer);
            }
        }
        jis.close();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        ByteBuffer buffer = entryMap.get(path);
        if(buffer == null) {
            return super.findClass(name);
        } else {
            byte[] bytes = buffer.array();
            return defineClass(name, bytes, 0, bytes.length);
        }
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

    /**
     * 关闭Decoder
     * @throws IOException
     */
    public void close() throws IOException {
        Iterator<ByteBuffer> iterator = entryMap.values().iterator();
        while(iterator.hasNext()) {
            ByteBuffer buffer = iterator.next();
            buffer.clear(); //清空ByteBuffer对象缓存
        }
        entryMap.clear(); //清空HashMap
    }
}