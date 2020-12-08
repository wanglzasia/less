package com.less.classloader.clove;


import sun.misc.URLClassPath;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        CloveClassLoader classLoader = new CloveClassLoader();
        // while (true) {
        try {
            List<String> jars = new ArrayList<String>();
            jars.add("D:\\Tools\\maven\\repository\\com\\clove\\clove-engine\\1.0\\clove-engine-1.0.jar");
            jars.add("D:\\Tools\\maven\\repository\\com\\clove\\clove-satp\\1.0\\clove-satp-1.0.jar");
            jars.add("D:\\Tools\\maven\\repository\\com\\clove\\clove-gear\\1.0\\clove-gear-1.0.jar");
            jars.add("D:\\Tools\\maven\\repository\\com\\clove\\clove-util\\1.0\\clove-util-1.0.jar");

            classLoader.addJars(jars);
            classLoader.loadClass();

            final URLClassPath ucp;

            Thread.sleep(1000);
            classLoader.unloadJar();
            //classLoader.unloadJarFile("clove-satp-1.0.jar");
            //System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //}
    }
}
