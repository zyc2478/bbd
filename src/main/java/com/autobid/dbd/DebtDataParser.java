package com.autobid.dbd;

import java.util.ArrayList;

import net.sf.json.JSONArray;
//import org.apache.log4j.Logger;

/** 
* @ClassName: DebtDataParser 
* @Description: ծȨ���ݴ������
* @author Richard Zeng 
* @date 2017��12��22�� ����10:47:12 
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
			 * ���1���������35��Ԫ�����һ�֣�partCount = 4,m!=0,i=3ʱ��fromIndexΪ30,toindexΪsize-1��34
			 * ���2���������30��Ԫ�أ�m==0)���һ�֣�m==0,partCount=3,fromIndexΪ20��toindexΪ30
			 * ���3���������35��Ԫ�صڶ��֣�i=1,fromIndex=10,toindex=20
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
