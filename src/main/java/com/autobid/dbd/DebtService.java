package com.autobid.dbd;

//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.autobid.entity.BidResult;
import com.autobid.entity.DebtListResult;
import com.autobid.entity.LoanListResult;
import com.autobid.util.JsonUtil;
import com.autobid.util.TokenInit;
import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.PropertyObject;
import com.ppdai.open.core.Result;
import com.ppdai.open.core.ValueTypeEnum;

/** 
* @ClassName: BidService 
* @Description: 与拍拍贷的各投标API调用服务
* @author Richard Zeng 
* @date 2017年10月13日 下午5:15:21 
*  
*/
public class DebtService {
	
	private static Logger logger = Logger.getLogger(DebtService.class); 

	
	public static JSONArray debtListService(int indexNum) throws Exception{
	    ArrayList<String> debtListJsonList = new ArrayList<String>();
		String  url = "http://gw.open.ppdai.com/invest/LLoanInfoService/DebtListNew";
    	Result result = OpenApiClient.send(url, new PropertyObject("PageIndex",indexNum,ValueTypeEnum.Int32));
		if(JsonUtil.decodeUnicode(result.getContext()).contains("您的操作太频繁啦")) {
    		logger.info("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
			logger.error("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
    		Thread.sleep(60000);
    	}
		JSONArray debtListArray = null;
    	if(result.isSucess()){
    		String debtListResult = result.getContext();
    		
    		JSONObject debtListJson = new JSONObject(debtListResult);
    		//logger.info(balanceJson);

    		debtListArray = debtListJson.getJSONArray("DebtInfos");

    	}else{
    		logger.error(result.getErrorMessage());
    	}

		return debtListArray;
	}
	
	public static JSONArray BatchDebtInfosService(List<Integer> debtIds) throws Exception{
	    ArrayList<String> debtListJsonList = new ArrayList<String>();
		String  url = "https://openapi.ppdai.com/invest/LLoanInfoService/BatchDebtInfos";
    	String creditCode;
    	Result result = OpenApiClient.send(url,new PropertyObject("DebtIds", debtIds, ValueTypeEnum.Other));
    	JSONArray debtInfosArray = null;
    	if(JsonUtil.decodeUnicode(result.getContext()).contains("您的操作太频繁啦")) {
    		logger.info("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
			logger.error("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
    		Thread.sleep(60000);
    	}
    	if(result.isSucess()){
    		String batchDebtInfosResult = result.getContext();
    		
    		JSONObject batchDebtInfosJson = new JSONObject(batchDebtInfosResult);
    		//logger.info(balanceJson);

    		debtInfosArray = batchDebtInfosJson.getJSONArray("DebtInfos");
    		logger.info(debtInfosArray);
    	}else{
    		logger.error(result.getErrorMessage());
    	}
    	return debtInfosArray;
	}
	
	public static LoanListResult loanListService(int indexNum) throws Exception{
	    int[] loanIds = null;
	    ArrayList<String> loanListJsonList = new ArrayList<String>();
		String  url = "http://gw.open.ppdai.com/invest/LLoanInfoService/LoanList";
    	Result result = null;
    	String creditCode;
    	result = OpenApiClient.send(url, new PropertyObject("PageIndex",indexNum,ValueTypeEnum.Int32));
		if(JsonUtil.decodeUnicode(result.getContext()).contains("您的操作太频繁啦")) {
    		logger.info("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
			logger.error("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
    		Thread.sleep(60000);
    	}
    	if(result.isSucess()){
    		String loanAllList = result.getContext();
        	String loanListJson = loanAllList.substring(13);
        	//logger.info("loanListJson is :" + loanListJson);
        	JSONArray loanListArray = new JSONArray(loanListJson);
        	int size = loanListArray.length();
        	loanIds = new int[size];
        	int j=0;
        	for(int i=0;i<size;i++){
        		JSONObject loanInfoObj = loanListArray.getJSONObject(i);
        		int listingId = loanInfoObj.getInt("ListingId");
        		creditCode = loanInfoObj.getString("CreditCode");
        		if(!creditCode.equals("AA")){
        			loanIds[i]=listingId;
        			//System.out.println("loanId" + i + " value:" + loanIds[i]);
        			j++;
        		}
        	}
        	System.out.println("第"+indexNum+"组标的总数为："+ loanIds.length + "，中风险标的数为："+ j);
	    	loanListJsonList.add(loanListJson);
    	}else{
    		logger.error(result.getErrorMessage());
    	}
		LoanListResult llr = new LoanListResult();
		llr.setIndexNum(indexNum);
		llr.setLoanList(loanListJsonList);
		llr.setLoanIdCount(loanIds.length);
		return llr;
	}
	
	public static ArrayList<String> batchListInfosCollectorService(String token, ArrayList<List<Integer>> listingIdsCollector) throws Exception{
    	//System.out.println("--------batchListInfosCollectorService---------");
		String url = "http://gw.open.ppdai.com/invest/LLoanInfoService/BatchListingInfos";
    	ArrayList<String> batchListInfosCollector = new ArrayList<String>();
    	Iterator<List<Integer>> listingIdsIt = listingIdsCollector.iterator();
    	//List<Integer> listingIds = new ArrayList<Integer>();
    	//int num=0;
    	//System.out.println(listingIdsCollector.size());
    	while(listingIdsIt.hasNext()){
    		List<Integer> listingIds = listingIdsIt.next();
    		//System.out.println("listingids size is " + listingIds.size());
    		//System.out.println(listingIds);
    		Result result = OpenApiClient.send(url,token,new PropertyObject("ListingIds",listingIds,ValueTypeEnum.Other));
    		if(JsonUtil.decodeUnicode(result.getContext()).contains("您的操作太频繁啦")) {
        		logger.error("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
        		Thread.sleep(60000);
        	}
    		//System.out.println("result.context: " + result.getContext());
    		batchListInfosCollector.add(result.getContext());
    	}
    	return batchListInfosCollector;
	}
	
    public static BidResult debtService(String token,String openId,int listingId) throws Exception {
        System.out.println("-------------------debtService----------------------------");
    	String url = "http://gw.open.ppdai.com/invest/BidService/BuyDebt";
    	Result result = OpenApiClient.send(url,token,
    				new PropertyObject("debtDealId", listingId,ValueTypeEnum.Int32));
    	String debtResult = result.getContext();
    	System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
    	System.out.println(debtResult);
    	if(JsonUtil.decodeUnicode(debtResult).contains("您的操作太频繁啦")) {
    		logger.error("xxxxxx 您的操作太频繁啦！先喝杯茶吧，歇一分钟吧 ~~~xxxxxx");
    		Thread.sleep(60000);
    	}
    	//"本债权信息发生变动，债权已失效!"
    	BidResult successBidResult = null;
    	//System.out.println("Success? "+ bidResult.isSucess());
    	//System.out.println(String.format("返回结果:%s", result.isSucess() ? bidResult : result.getErrorMessage()));
    	if(JsonUtil.decodeUnicode(debtResult).contains("令牌校验失败")){
    		logger.error("xxxxxx Error！令牌校验失败！xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("访问令牌不存在")){
    		logger.error("xxxxxx 访问令牌不存在！xxxxxx");
    		AuthInfo authInfo = OpenApiClient.refreshToken(openId,token);
    		token = authInfo.getRefreshToken();
    		System.out.println("Refresh token = " + token);
        	Result retryResult = OpenApiClient.send(url,token,
    				new PropertyObject("debtDealId", listingId,ValueTypeEnum.Int32));
        	System.out.println(String.format("返回结果:%s", retryResult.isSucess() ? retryResult.getContext() : retryResult.getErrorMessage()));
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("不允许重复投标")){
    		logger.error("xxxxxx Error！" + listingId + "不允许重复投标！ xxxxxx");
    		//logger.error("xxxxxx Error！" + listingId + "不允许重复投标！ xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("请先充值")){
    		logger.error("Error！账户余额不足，请先充值！");
    		Thread.sleep(300000);
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("已满标")){
    		logger.error("xxxxxx Error！" + listingId + "已满标！xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("标的不存在")){
    		logger.error("xxxxxx Error！" + listingId + "标的不存在！xxxxxx");
    	}else if(debtResult.contains("\"Result\":0,\"ResultMessage\":null")){
    		//successBidResult = new BidResult(listingId,bidAmount);
    		logger.info(debtResult);
    	} 
    	return successBidResult;
    }
	
}
