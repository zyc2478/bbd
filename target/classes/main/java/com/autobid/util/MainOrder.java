package com.autobid.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MainOrder {  
	
	private static String fileName = System.getProperty("user.dir") + "/config.properties";
	
    public static void main(String[] args) throws IOException {  
        Properties prop = readOrderedPropertiesFile();  
        printProp(prop);  
        writeOrderedPropertiesFile(prop);  
    }  
  
    /** 
     * ���properties��key��value 
     */  
    public static void printProp(Properties properties) {  
        System.out.println("---------����ʽһ��------------");  
        for (String key : properties.stringPropertyNames()) {  
            System.out.println(key + "=" + properties.getProperty(key));  
        }  
  
        System.out.println("---------����ʽ����------------");  
        Set<Object> keys = properties.keySet();//��������key�ļ���  
        for (Object key : keys) {  
            System.out.println(key.toString() + "=" + properties.get(key));  
        }  
  
        System.out.println("---------����ʽ����------------");  
        Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();//���ص����Լ�ֵ��ʵ��  
        for (Map.Entry<Object, Object> entry : entrySet) {  
            System.out.println(entry.getKey() + "=" + entry.getValue());  
        }  
  
        System.out.println("---------����ʽ�ģ�------------");  
        Enumeration<?> e = properties.propertyNames();  
        while (e.hasMoreElements()) {  
            String key = (String) e.nextElement();  
            String value = properties.getProperty(key);  
            System.out.println(key + "=" + value);  
        }  
    }  
  
    /** 
     * ��Properties�ļ������� 
     */  
    private static Properties readOrderedPropertiesFile() {  
        Properties properties = new OrderProperties();  
        InputStreamReader inputStreamReader = null;  
        try {  
            InputStream inputStream = new BufferedInputStream(new FileInputStream(fileName));  
            //prop.load(in);//ֱ����ôд�����properties�ļ����к��֣����ֻ����롣��Ϊδ���ñ����ʽ��  
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            properties.load(inputStreamReader);  
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
        } finally {  
            if (inputStreamReader != null) {  
                try {  
                    inputStreamReader.close();  
                } catch (IOException e) {  
                    System.out.println(e.getMessage());  
                }  
            }  
        }  
        return properties;  
    }  
  
    /** 
     * дProperties�ļ������� 
     * @throws IOException 
     */  
    private static void writeOrderedPropertiesFile(Properties properties) throws IOException {  
        //Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(fileName);
		//һ��Ҫ��װ�����е������ļ�
		properties.load(fis);
        properties.setProperty("phone", "10086");  
        OutputStreamWriter outputStreamWriter = null;  
        try {  
            //�������Ե�b.properties�ļ�  
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);//true��ʾ׷�Ӵ�,falseÿ�ζ����������д  
            //prop.store(oFile, "�˲����Ǳ�������properties�ļ��е�һ�е�ע��˵������");//����������ط�����  
            //prop.store(new OutputStreamWriter(oFile, "utf-8"), "��������");//����������ɵ�properties�ļ��е�һ�е�ע����������  
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");  
            properties.store(outputStreamWriter, "lll");  
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
        } finally {  
            if (outputStreamWriter != null) {  
                try {  
                    outputStreamWriter.close();  
                } catch (IOException e) {  
                    System.out.println(e.getMessage());  
                }  
            }  
        }  
  
    }  
}  