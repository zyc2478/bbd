package com.autobid.util;

import org.junit.Test;

public class ConfUtilTest {


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

