package com.less.classloader;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class Test {
    public static void main(String[] args) {

        CloveClassLoader classLoader = new CloveClassLoader();
       // while (true) {
            try {
                classLoader.loadJar("clove-satp-1.0.jar");
                //Class clz = classLoader.loadClass("clove-satp-1.0.jar","com.clove.satp.ToolService");

                //ParameterTranlate parameterTranlate = (ParameterTranlate) clz.newInstance();
                //String input = parameterTranlate.getBookInput("sdf");
                //System.out.println(input);
                Thread.sleep(1000);
                classLoader.unloadJarFile("clove-satp-1.0.jar");
                //System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }
}
