package com.autobid.dbd;

import java.util.ArrayList;

import net.sf.json.JSONArray;
//import org.apache.log4j.Logger;

/** 
* @ClassName: DebtDataParser 
* @Description: 债权数据处理程序
* @author Richard Zeng 
* @date 2017年12月22日 下午10:47:12 
* 
*/
public class DebtDataParser {
	
	//private static Logger logger = Logger.getLogger(DebtDataParser.class);  

	public static ArrayList<JSONArray> getDebtsCollector(JSONArray debtList) {
		
		ArrayList<JSONArray> dll = new ArrayList<JSONArray>();
		int size = debtList.size();
		int partSize = 10;
		int m = size % partSize;
		int partCount;
		
		if(m > 0) {
			partCount = size / partSize + 1;
		}else {
			partCount = size / partSize;
		}
		
		for(int i=0;i<partCount;i++) {
			/*
			 * 情况1：比如对于35个元素最后一轮，partCount = 4,m!=0,i=3时，fromIndex为30,toindex为size-1即34
			 * 情况2：比如对于30个元素（m==0)最后一轮，m==0,partCount=3,fromIndex为20，toindex为30
			 * 情况3：比如对于35个元素第二轮，i=1,fromIndex=10,toindex=20
			 */
			if(m!=0 && i==partCount-1) {
				JSONArray subDebtList = (JSONArray)debtList.subList(i*partSize, size-1);
				dll.add(subDebtList);
			}else {
				JSONArray subDebtList = (JSONArray)debtList.subList(i*partSize, (i+1)*partSize);
				dll.add(subDebtList);
			}
		}
		return dll;
	}
}
