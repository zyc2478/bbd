package com.autobid.util;

//import java.io.BufferedInputStream;
//import java.io.FileNotFoundException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.io.InputStreamReader;
//import java.io.Reader;
import java.util.Iterator;
import java.util.Properties; 

//import org.apache.log4j.Logger;

public class ConfUtil {
	
	//private static Logger logger = Logger.getLogger(ConfUtil.class);
	
	public static Properties prop = new OrderProperties();
	//private static Reader in = new InputStreamReader(ConfUtil.class.getResourceAsStream("config.properties"));

	//InputStream in = ConfUtil.class.getClassLoader().getResourceAsStream("Config.properties");  

	private static String fileName = System.getProperty("user.dir") + "/config.properties";
	
	//private static String fileName = "config.properties";
	
	public void readConf() throws IOException {
        //��ȡ�����ļ�
        //InputStream in = new BufferedInputStream (new FileInputStream(fileName));
		//InputStream in = ConfUtil.class.getClassLoader().getResourceAsStream(fileName);  
        InputStream in = new FileInputStream(fileName);
		prop.load(in);
        //prop.load(in);     ///���������б�
        Iterator<String> it=prop.stringPropertyNames().iterator();
        while(it.hasNext()){
            String key=it.next();
            System.out.println(key+":"+prop.getProperty(key));
        }
        in.close();
	}
	
	public static String getProperty(String key) throws IOException {
		//InputStream in = new BufferedInputStream (new FileInputStream(fileName));
		//InputStream in = ConfUtil.class.getClassLoader().getResourceAsStream(fileName);  
		//InputStream in = new FileInputStream(fileName);
        //prop.load(in);     ///���������б�
		
		InputStreamReader inputStreamReader = null;  
	    InputStream inputStream = new BufferedInputStream(new FileInputStream(fileName));  
	    //prop.load(in);//ֱ����ôд�����properties�ļ����к��֣����ֻ����롣��Ϊδ���ñ����ʽ��  
	    inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
	    prop.load(inputStreamReader);  
	    if (inputStreamReader != null) {  
	    	 inputStreamReader.close();  
	    }
        String value = prop.getProperty(key);
        //System.out.println(key + ":"+value);
        return value ;
	}
	
	public static void setProperty(String key,String value) throws IOException, ParseException {
		InputStreamReader inputStreamReader = null;  
	    InputStream inputStream = new BufferedInputStream(new FileInputStream(fileName));  
	    //prop.load(in);//ֱ����ôд�����properties�ļ����к��֣����ֻ����롣��Ϊδ���ñ����ʽ��  
	    inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
	    //һ��Ҫ��װ�������ļ�
	    prop.load(inputStreamReader);  
        FileOutputStream oFile = new FileOutputStream(fileName);//new FileOutputStream(fileName, true)
        prop.setProperty(key, value);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowDate = sdf.format(new Date());
        prop.store(oFile, "Copyright (c) SkyWalker Studio, modified date is " + nowDate);
        oFile.close();
	}
}