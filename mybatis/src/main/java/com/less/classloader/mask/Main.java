package com.less.classloader.mask;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {
        encode();
        decode();
    }

    private static void encode() {
        try {
            JarEncoder encoder = new JarEncoder("D:\\Tools\\maven\\repository\\com\\clove\\clove-util\\1.0\\clove-util-1.0.jar");
            encoder.write("D:\\Tools\\maven\\repository\\com\\clove\\clove-util\\1.0\\clove-util-enc-1.0.jar");
            System.out.println("encode success");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void decode() {
        try {
            JarDecoder decoder = new JarDecoder("D:\\Tools\\maven\\repository\\com\\clove\\clove-util\\1.0\\clove-util-enc-1.0.jar");
            Class<?> cls = decoder.loadClass("com.clove.util.common.DebugUtil");
            Method method = cls.getMethod("print", null);
            method.invoke(cls.newInstance(), null);
            decoder.close();
            System.out.println("decode success");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException | Base64DecodingException e) {
            e.printStackTrace();
        }
    }
}
