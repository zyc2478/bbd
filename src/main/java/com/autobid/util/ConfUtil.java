package com.autobid.util;

//import java.io.BufferedInputStream;
//import java.io.FileNotFoundException;

import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

//import java.io.InputStreamReader;
//import java.io.Reader;

//import org.apache.log4j.Logger;

public class ConfUtil {

    //private static Logger logger = Logger.getLogger(ConfUtil.class);

    private static Properties prop = new OrderProperties();
    private static Properties lprop = new OrderProperties();
    //private static Reader in = new InputStreamReader(ConfUtil.class.getResourceAsStream("config.properties"));

    //InputStream in = ConfUtil.class.getClassLoader().getResourceAsStream("Config.properties");
    //private static String fullFileName;

    private static String fullFileName = System.getProperty("user.dir") + "/config.properties" ;


    //private static String fileName = "config.properties";

    public static ConfBean readAllToBean() throws Exception {
        InputStream in = new FileInputStream(fullFileName);
        prop.load(in);
        Set<String> keySet = prop.stringPropertyNames();
        ConfBean cb = new ConfBean();
        //获取方法
        for (String propName : keySet) {
            //cb.setAmountBegin(prop.getProperty("amount_begin"));

            //将下划线字符串改为驼峰字符串，并将首字母大写，前面加上set得到函数名
            String methodName = "set" + camelCaseName(captureName(propName));

            //根据类和函数名得到Method对象
            Method method = cb.getClass().getDeclaredMethod(methodName, String.class);

            //调用执行该method的方法，是set赋值方法，赋的值为配置文件属性名
            method.invoke(cb, prop.getProperty(propName));
        }

        //System.out.println(cb.getClass().getDeclaredMethod("getAmountBegin").invoke(cb));
        in.close();
        return cb;
    }
    public static LocalConfBean readAllToLocalBean() throws Exception {
        String localFileName = System.getProperty("user.dir") + "/local.properties" ;
        InputStream in = new FileInputStream(localFileName);
        lprop.load(in);
        Set<String> keySet = lprop.stringPropertyNames();
        LocalConfBean lcb = new LocalConfBean();
        //获取方法
        for (String propName : keySet) {
            //cb.setAmountBegin(prop.getProperty("amount_begin"));

            //将下划线字符串改为驼峰字符串，并将首字母大写，前面加上set得到函数名
            String methodName = "set" + camelCaseName(captureName(propName));

            //根据类和函数名得到Method对象
            Method method = lcb.getClass().getDeclaredMethod(methodName, String.class);

            //调用执行该method的方法，是set赋值方法，赋的值为配置文件属性名
            method.invoke(lcb, lprop.getProperty(propName));
        }

        //System.out.println(cb.getClass().getDeclaredMethod("getAmountBegin").invoke(cb));
        in.close();
        return lcb;
    }

    private static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    private static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    public static String getProperty(String key) throws IOException {
        //InputStream in = new BufferedInputStream (new FileInputStream(fileName));
        //InputStream in = ConfUtil.class.getClassLoader().getResourceAsStream(fileName);
        //InputStream in = new FileInputStream(fileName);
        //prop.load(in);     ///加载属性列表

        InputStreamReader inputStreamReader;
        InputStream inputStream = new BufferedInputStream(new FileInputStream(fullFileName));
        //prop.load(in);//直接这么写，如果properties文件中有汉字，则汉字会乱码。因为未设置编码格式。
        inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        prop.load(inputStreamReader);
        inputStreamReader.close();
        //System.out.println(key + ":"+value);
        return prop.getProperty(key);
    }

    public static String getLocalProperty(String key) throws IOException {
        //InputStream in = new BufferedInputStream (new FileInputStream(fileName));
        //InputStream in = ConfUtil.class.getClassLoader().getResourceAsStream(fileName);
        //InputStream in = new FileInputStream(fileName);
        //prop.load(in);     ///加载属性列表
        String localFileName = System.getProperty("user.dir") + "/local.properties" ;
        InputStreamReader inputStreamReader;
        InputStream inputStream = new BufferedInputStream(new FileInputStream(localFileName));
        //prop.load(in);//直接这么写，如果properties文件中有汉字，则汉字会乱码。因为未设置编码格式。
        inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        lprop.load(inputStreamReader);
        inputStreamReader.close();
        //System.out.println(key + ":"+value);
        return lprop.getProperty(key);
    }

    public static void setProperty(String key, String value) throws IOException {
        InputStreamReader inputStreamReader;
        InputStream inputStream = new BufferedInputStream(new FileInputStream(fullFileName));
        //prop.load(in);//直接这么写，如果properties文件中有汉字，则汉字会乱码。因为未设置编码格式。
        inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        //一定要先装载配置文件
        prop.load(inputStreamReader);
        FileOutputStream oFile = new FileOutputStream(fullFileName);//new FileOutputStream(fileName, true)
        prop.setProperty(key, value);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowDate = sdf.format(new Date());
        prop.store(oFile, "Copyright (c) SkyWalker Studio, modified date is " + nowDate);
        oFile.close();
    }

    public static void setLocalProperty(String key, String value) throws IOException {

        String localFileName = System.getProperty("user.dir") + "/local.properties" ;

        InputStreamReader inputStreamReader;
        InputStream inputStream = new BufferedInputStream(new FileInputStream(localFileName));
        //prop.load(in);//直接这么写，如果properties文件中有汉字，则汉字会乱码。因为未设置编码格式。
        inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        //一定要先装载配置文件
        lprop.load(inputStreamReader);
        FileOutputStream oFile = new FileOutputStream(localFileName);//new FileOutputStream(fileName, true)
        lprop.setProperty(key, value);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowDate = sdf.format(new Date());
        lprop.store(oFile, "Copyright (c) SkyWalker Studio, modified date is " + nowDate);
        oFile.close();
    }

/*    public void readConf() throws IOException {
        //读取属性文件
        //InputStream in = new BufferedInputStream (new FileInputStream(fileName));
        //InputStream in = ConfUtil.class.getClassLoader().getResourceAsStream(fileName);
        InputStream in = new FileInputStream(fileName);
        prop.load(in);
        //prop.load(in);     ///加载属性列表
        Iterator<String> it = prop.stringPropertyNames().iterator();
        while (it.hasNext()) {
            String key = it.next();
            System.out.println(key + ":" + prop.getProperty(key));
        }
        in.close();
    }*/
}