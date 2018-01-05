package com.autobid.util;

import org.apache.log4j.Logger;
import org.junit.Test;
import com.autobid.util.ConfUtil;

public class ConfUtilTest {

    Logger logger = Logger.getLogger("ConfUtilTest.class");
	
/*	@Before
	public void testSetProperty() throws IOException, ParseException {
		//ConfUtil.setProperty("ttkey", "ttvv2");
		logger.info(ConfUtil.getProperty("token_init"));
		//logger.info(ConfUtil.getProperty("ttkey"));
	}
	
	
    @Test
    public void testGetProperty() throws IOException {
    	//logger.info(ConfUtil.getProperty("MIN_AID_AssOUNT"));
    	logger.info(ConfUtil.getProperty("min_bid_amount"));
    	logger.info("token is : \"" + ConfUtil.getProperty("token") +"\"");
    	logger.info("refresh_token is : " + ConfUtil.getProperty("refresh_token"));
    }*/

    @Test
    public void testGetAllFromBean() throws Exception {
        ConfUtil.readAllToBean();
    }

    @Test
    public void testGetFuncValueFromBean() throws Exception {
        ConfBean cb = ConfUtil.readAllToBean();
        System.out.println("cb.getAmountBegin():" + cb.getAmountBegin());
        System.out.println("cb.getAmountMrate():" + cb.getAmountMrate());
    }
    
/*    @Test
    public void testAppendProperty() throws IOException {
    	ConfUtil.appendProperty("testkey", "1");
    	ConfUtil.appendProperty("testkey", "2");
    	ConfUtil.appendProperty("testkey11", "11");

    }*/
    
/*    @Test
    public void testSetProperty() throws IOException {
    	ConfUtil.setProperty("kkkkk", "5");
    	ConfUtil.setProperty("setKey2", "6");
    	ConfUtil.appendProperty("testkey11", "11");
    }*/
}

