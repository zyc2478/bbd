package com.autobid.dbd;

import java.util.ArrayList;
import java.util.List;

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
			 * 情况1：比如对于35个元素最后一轮，partCount = 4,m!=0,i=3时，fromIndex为30,toindex为size即35
			 * 情况2：比如对于30个元素（m==0)最后一轮，m==0,partCount=3,fromIndex为20，toindex为30
			 * 情况3：比如对于35个元素第二轮，i=1,fromIndex=10,toindex=20
			 */
			if(m!=0 && i==partCount-1) {
				List<?> subDebtList = debtList.subList(i*partSize, size);
				//subDebtList = (JSONArray)debtList.subList(i*partSize, size-1);
				//subDebtList = debtList.subList(fromIndex, toIndex)
				JSONArray subDebtArray = new JSONArray();
				for(Object object:subDebtList) {
					subDebtArray.add(object);
				}
				dll.add(subDebtArray);
			}else {
				List<?> subDebtList = debtList.subList(i*partSize, (i+1)*partSize);
				//subDebtList = (JSONArray)debtList.subList(i*partSize, size-1);
				//subDebtList = debtList.subList(fromIndex, toIndex)
				JSONArray subDebtArray = new JSONArray();
				for(Object object:subDebtList) {
					subDebtArray.add(object);
				}
				dll.add(subDebtArray);
			}
		}
		return dll;
	}

	public static List<Integer> getListingIds(JSONArray dFiltered) {
		
		List<Integer> listingIds = new ArrayList<Integer>();
		
		
		for(int i=0;i<dFiltered.size();i++) {
			//System.out.println(dFiltered.getJSONObject(i));
			listingIds.add(new Integer(dFiltered.getJSONObject(i).getInt("ListingId")));
		}
		
		return listingIds;
	}

	public static JSONArray parseDebtInfoFromBids(JSONArray dFiltered, JSONArray bidFiltered) {
		
		JSONArray dbFiltered = new JSONArray();
		for(int i=0;i<bidFiltered.size();i++) {
			for(int j=0;j<dFiltered.size();j++) {
/*				System.out.println("bidFiltered.getJSONObject(i).getInt(\"ListingId\"):"+bidFiltered.getJSONObject(i).getInt("ListingId"));
				System.out.println("dFiltered.getJSONObject(j).getInt(\"ListingId\"):"+dFiltered.getJSONObject(j).getInt("ListingId"));*/
				if(bidFiltered.getJSONObject(i).getInt("ListingId")==dFiltered.getJSONObject(j).getInt("ListingId")) {
					dbFiltered.add(dFiltered.getJSONObject(j));
				}
			}
		}
		
		return dbFiltered;
	}
	
}
