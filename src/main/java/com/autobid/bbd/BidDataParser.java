package com.autobid.bbd;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.autobid.util.JsonUtil;


/** 
* @ClassName: BidDataParser 
* @Description: 标的数据处理程序
* @author Richard Zeng 
* @date 2017年10月13日 下午5:11:12 
* 
*/
public class BidDataParser {

	private static Logger logger = Logger.getLogger(BidDataParser.class);  
	
	public static double getBalance(String queryBalanceJson) throws ParseException{
		
		JSONObject balanceJson = new JSONObject(queryBalanceJson);
		//logger.info(balanceJson);

		JSONArray balanceArray = balanceJson.getJSONArray("Balance");

		double canUseBalance = 0;
		for(int i=0;i<balanceArray.length();i++){
			JSONObject balanceObject = balanceArray.getJSONObject(i);
			String accountCategory = balanceObject.getString("AccountCategory");
			if(JsonUtil.decodeUnicode(accountCategory).contains("用户现金余额")){
				canUseBalance = balanceObject.getDouble("Balance");
			}
		}
		logger.info("可用余额为：" + canUseBalance);
		return canUseBalance;
	}
	
	public static List<Integer> getListingIds(String loanListJson) throws ParseException{
    	System.out.println("-------------------getListingIds----------------------------");
    	String creditCode;
    	String loanListArrayJson = loanListJson.substring(13);
    	//logger.info("loanListJson is :" + loanListJson);
    	JSONArray loanListArray = new JSONArray(loanListArrayJson);
    	int size = loanListArray.length();
    	int[] loanIds = new int[size];
    	int j = 0;
    	for(int i=0;i<size;i++){
    		JSONObject loanInfoObj = loanListArray.getJSONObject(i);
    		int listingId = loanInfoObj.getInt("ListingId");
    		creditCode = loanInfoObj.getString("CreditCode");
    		if(!creditCode.equals("AA")){
    			loanIds[i]=listingId;
    			j++;
    		}
    	}
    	logger.info("标的总数为："+ loanIds.length + "，中风险标的数为："+ j);

        List<Integer> listingIds = new ArrayList<Integer>(loanIds.length);
    	for(int i=0;i<loanIds.length;i++){
    		listingIds.add(Integer.valueOf(loanIds[i]));
    	}	
		return listingIds;
	}
	
	public static List<Integer> getListingIds(ArrayList<String> loanListJsonList) throws ParseException{
    	//System.out.println("-------------------getListingIds----------------------------");
    	ArrayList<String> loanJsonList = loanListJsonList;
    	Iterator<String> loanJsonListIt = loanJsonList.iterator();
        List<Integer> listingIds = new ArrayList<Integer>();
    	while(loanJsonListIt.hasNext()){
    		String creditCode;
    		String loanListJson = loanJsonListIt.next();
        	JSONArray loanListArray = new JSONArray(loanListJson);
        	int size = loanListArray.length();
        	int[] loanIds = new int[size];
        	for(int i=0;i<size;i++){
        		JSONObject loanInfoObj = loanListArray.getJSONObject(i);
        		int listingId = loanInfoObj.getInt("ListingId");
        		//System.out.println("ListingId is "  + listingId);
        		creditCode = loanInfoObj.getString("CreditCode");
        		if(!creditCode.equals("AA")){
        			loanIds[i]=listingId;
        		}
        	}
        	for(int i=0;i<loanIds.length;i++){
        		if(loanIds[i]!=0){
            		listingIds.add(Integer.valueOf(loanIds[i]));
        		}
        	}
    	}
		return listingIds;
	}		
	
	
	
	

	
	public static Integer[][] getListingIdsParted(List<Integer> listingIds){
		//System.out.println("-------------------getListingIdsParted----------------------------");
		int size = listingIds.size();
		Integer[][] listingIdsParted;
		if( size % 10 ==0){
			listingIdsParted = new Integer[size/10][10];
		}else{
			listingIdsParted = new Integer[size/10 + 1][10];
		}
		//System.out.println(listingIds);
		Iterator<Integer> listingIdsIt = listingIds.iterator();
		
		Integer[] listingIdsArray = new Integer[listingIds.size()];
		int s = 0;
		while(listingIdsIt.hasNext()){
			listingIdsArray[s] = listingIdsIt.next();
			s++;
			//System.out.println("listingIdsArray:"+listingIdsArray[s]);
		}
		/*for(int i=0;i<listingIdsArray.length;i++) {
			System.out.println(listingIdsArray[i]);
		}*/
		
		int p = 0;
/*		System.out.println("listingIdsParted length :"+listingIdsParted.length);
		System.out.println("listingIdsParted[0] length :"+listingIdsParted[0].length);*/
		
		for(int i=0;i<listingIdsParted.length;i++){
			for(int j=0;j<listingIdsParted[0].length && i*listingIdsParted[0].length+j< listingIdsArray.length ;j++){
				listingIdsParted[i][j] = listingIdsArray[p];
				//System.out.println("listingIdsParted["+i+"]+["+j+"]:"+listingIdsParted[i][j]);
				p++;
			}
		}
		return listingIdsParted;
	}
	
    public static ArrayList<List<Integer>> getLisiingIdsCollector(Integer[][] listingIdsParted) throws Exception{
    	//System.out.println("-------getLisiingIdsCollector------");
    	
	    ArrayList<List<Integer>> listingIdsCollector = new ArrayList<List<Integer>>();
	    for(int i=0;i<listingIdsParted.length;i++){
	    	List<Integer> listingIdsProcessed = new ArrayList<Integer>(listingIdsParted[0].length);
	    	for(int j=0;j<listingIdsParted[0].length && listingIdsParted[i][j]!=null;j++){
	    		listingIdsProcessed.add(listingIdsParted[i][j]);
	    	}
	    	listingIdsCollector.add(listingIdsProcessed);
	    }
	    return listingIdsCollector;
    }
    
    public static JSONArray getLoanInfos(String batchListInfos) throws ParseException, InterruptedException{
    	JSONObject batchListInfosObject = new JSONObject(batchListInfos);
    	//System.out.println(batchListInfosObject);
    	/*if(JsonUtil.decodeUnicode(batchListInfos).contains("您的操作太频繁啦")) {
    		System.out.println("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
    		Thread.sleep(60000);
    	}*/
    	JSONArray  batchListInfoArray = batchListInfosObject.getJSONArray("LoanInfos");
    	return batchListInfoArray;
    }
    
    public static ArrayList<JSONArray> getLoanInfosCollector(ArrayList<String> batchListInfosCollector) 
    		throws ParseException, InterruptedException{
    	ArrayList<JSONArray> loanInfosCollector = new ArrayList<JSONArray>();
    	//System.out.println("batchListInfosCollector size :" + batchListInfosCollector);
    	Iterator<String> it = batchListInfosCollector.iterator();
    	while(it.hasNext()){
    		JSONArray loanInfosArray = getLoanInfos(it.next());
    		loanInfosCollector.add(loanInfosArray);
    	}
    	return loanInfosCollector;
    }
    
    public static HashMap<String,Object> getLoanInfoMap(JSONObject loanInfoObj) throws ParseException{	

		HashMap<String,Object> loanInfoMap = new HashMap<String,Object>();
    	loanInfoMap.put("Amount",loanInfoObj.getInt("Amount"));							//借款金额					
		loanInfoMap.put("CreditCode",loanInfoObj.getString("CreditCode"));				//标的等级
		loanInfoMap.put("RemainFunding",loanInfoObj.getInt("RemainFunding"));			//剩余可投金额
    	loanInfoMap.put("Gender",loanInfoObj.getInt("Gender"));							//性别	
		loanInfoMap.put("EducationDegree",loanInfoObj.getString("EducationDegree"));	//学历
		loanInfoMap.put("StudyStyle",loanInfoObj.getString("StudyStyle"));				//学习形式
		loanInfoMap.put("Age",loanInfoObj.getInt("Age"));								//年龄
		loanInfoMap.put("SuccessCount",loanInfoObj.getInt("SuccessCount"));				//成功借款次数
		loanInfoMap.put("NormalCount",loanInfoObj.getInt("NormalCount"));				//正常还清次数
		loanInfoMap.put("OverdueLessCount",loanInfoObj.getInt("OverdueLessCount"));		//逾期(1-15)还清次数
		loanInfoMap.put("OverdueMoreCount",loanInfoObj.getInt("OverdueMoreCount"));		//逾期(15天以上)还清次数
		loanInfoMap.put("OwingAmount",loanInfoObj.getInt("OwingAmount"));				//待还金额
		loanInfoMap.put("HighestDebt",loanInfoObj.getInt("HighestDebt"));				//历史最高负债
		loanInfoMap.put("HighestPrincipal",JsonUtil.parseInt(loanInfoObj.get("HighestPrincipal"))); //单笔最高借款金额
		loanInfoMap.put("TotalPrincipal",JsonUtil.parseInt(loanInfoObj.get("TotalPrincipal")));  	//累计借款金额
		loanInfoMap.put("LastSuccessBorrowTime",loanInfoObj.getString("LastSuccessBorrowTime")); 	//上次成功借款时间
		loanInfoMap.put("CertificateValidate",loanInfoObj.getInt("CertificateValidate"));			//学历认证0 未认证 1已认证
		loanInfoMap.put("NciicIdentityCheck",loanInfoObj.getInt("NciicIdentityCheck"));	//户籍认证0 未认证 1已认证
		loanInfoMap.put("VideoValidate",loanInfoObj.getInt("VideoValidate"));			//视频认证0 未认证 1已认证
		loanInfoMap.put("CreditValidate",loanInfoObj.getInt("CreditValidate"));			//征信认证0 未认证 1已认证
		loanInfoMap.put("EducateValidate",loanInfoObj.getInt("EducateValidate"));		//学籍认证0 未认证 1已认证
		return loanInfoMap;
    }

	public static ArrayList<List<Integer>> getListingIdsCollector(List<Integer> listingIds) {
		ArrayList<List<Integer>> dll = new ArrayList<List<Integer>>();
		int size = listingIds.size();
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
				List<Integer> subDebtList =  listingIds.subList(i*partSize, size-1);
				dll.add(subDebtList);
			}else {
				List<Integer> subDebtList = listingIds.subList(i*partSize, (i+1)*partSize);
				dll.add(subDebtList);
			}
		}
		return dll;
	}
}
