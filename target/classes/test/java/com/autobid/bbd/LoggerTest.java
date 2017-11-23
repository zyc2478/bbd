package com.autobid.bbd;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.autobid.util.Log4JUtil;


public class LoggerTest {  
	private static Logger logger = Logger.getLogger(Logger.class);
	
	
	@Test
	public void testLogFile(){
		logger.debug("log to file!");
	}
	
	public static void main(String[] args) { 
		BasicConfigurator.configure();
		Log4JUtil.getLogger().debug("Hello World!");  
		//System.out.println("Hello World!");
	}  
}  