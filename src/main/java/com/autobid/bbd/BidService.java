package com.autobid.bbd;

//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.autobid.entity.BidResult;
import com.autobid.entity.LoanListResult;
import com.autobid.util.JsonUtil;
import com.autobid.util.TokenInit;
import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.PropertyObject;
import com.ppdai.open.core.Result;
import com.ppdai.open.core.ValueTypeEnum;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
* @ClassName: BidService 
* @Description: 与拍拍贷的各投标API调用服务
* @author Richard Zeng 
* @date 2017年10月13日 下午5:15:21 
*  
*/
public class BidService {
	
	private static Logger logger = Logger.getLogger(BidService.class); 
	
	public static String queryBalanceService(String token) throws Exception{
		//请求url
		String url = "http://gw.open.ppdai.com/balance/balanceService/QueryBalance";
		
		Result result = OpenApiClient.send(url, token);
		//System.out.println(result.getContext());
		if(JsonUtil.decodeUnicode(result.getContext()).contains("您的操作太频繁啦")) {
    		System.out.println("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
    		Thread.sleep(60000);
    	}else if(JsonUtil.decodeUnicode(result.getContext()).contains("用户无效或令牌已过有效期")) {
			logger.info("Error!用户无效或令牌已过有效期");
			//System.out.println(TokenInit.getInitFlag());
			if(TokenInit.getInitFlag()==false) {
				TokenInit.initToken();
			}
			logger.info("已重置最新令牌");
    	}
		return result.getContext();
	}
	
	public static LoanListResult loanListService(int indexNum) throws Exception{
	    int[] loanIds = null;
	    ArrayList<String> loanInfosList = new ArrayList<String>();
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
    		String loanListResult = result.getContext();
    		logger.info("loanListResult is :" + loanListResult);
    		JSONObject loanListJson = JSONObject.fromObject(loanListResult);
        	JSONArray loanInfosArray = loanListJson.getJSONArray("LoanInfos");
        	//String loanListJson = loanAllList.substring(13);
        	logger.info("loanListArray is :" + loanInfosArray);   //Modified
        	int size = loanInfosArray.size();
        	loanIds = new int[size];
        	int j=0;
        	for(int i=0;i<size;i++){
        		JSONObject loanInfoObj = loanInfosArray.getJSONObject(i);
        		int listingId = loanInfoObj.getInt("ListingId");
        		creditCode = loanInfoObj.getString("CreditCode");
        		if(!creditCode.equals("AA")){
        			loanIds[i]=listingId;
        			//System.out.println("loanId" + i + " value:" + loanIds[i]);
        			j++;
        		}
        	}
        	System.out.println("第"+indexNum+"组标的总数为："+ loanIds.length + "，中风险标的数为："+ j);
	    	loanInfosList.add(loanInfosArray.toString());
	    	//logger.info(loanInfosList);
    	}else{
    		logger.error(result.getErrorMessage());
    	}
		LoanListResult llr = new LoanListResult();
		llr.setIndexNum(indexNum);
		llr.setLoanList(loanInfosList);
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
	
    public static BidResult biddingService(String token,String openId,int listingId,int bidAmount) throws Exception {
        System.out.println("-------------------biddingService----------------------------");
        String useCoupon = "true";
    	String url = "http://gw.open.ppdai.com/invest/BidService/Bidding";
    	Result result = OpenApiClient.send(url,token,
    				new PropertyObject("ListingId", listingId,ValueTypeEnum.Int32),
    				new PropertyObject("Amount", bidAmount,ValueTypeEnum.Double),
    				new PropertyObject("UseCoupon", useCoupon, ValueTypeEnum.String));
    	String bidResult = result.getContext();
    	
    	if(JsonUtil.decodeUnicode(bidResult).contains("您的操作太频繁啦")) {
    		logger.error("xxxxxx 您的操作太频繁啦！先喝杯茶吧，歇一分钟吧 ~~~xxxxxx");
    		Thread.sleep(60000);
    	}
    	BidResult successBidResult = null;
    	//System.out.println("Success? "+ bidResult.isSucess());
    	//System.out.println(String.format("返回结果:%s", result.isSucess() ? bidResult : result.getErrorMessage()));
    	if(JsonUtil.decodeUnicode(bidResult).contains("令牌校验失败")){
    		logger.error("xxxxxx Error！令牌校验失败！xxxxxx");
    	}else if(JsonUtil.decodeUnicode(bidResult).contains("访问令牌不存在")){
    		logger.error("xxxxxx 访问令牌不存在！xxxxxx");
    		AuthInfo authInfo = OpenApiClient.refreshToken(openId,token);
    		token = authInfo.getRefreshToken();
    		System.out.println("Refresh token = " + token);
        	Result retryResult = OpenApiClient.send(url,token,
    				new PropertyObject("ListingId", listingId,ValueTypeEnum.Int32),
    				new PropertyObject("Amount", bidAmount,ValueTypeEnum.Double),
    				new PropertyObject("UseCoupon", useCoupon, ValueTypeEnum.String));
        	System.out.println(String.format("返回结果:%s", retryResult.isSucess() ? retryResult.getContext() : retryResult.getErrorMessage()));
    	}else if(JsonUtil.decodeUnicode(bidResult).contains("不允许重复投标")){
    		logger.error("xxxxxx Error！" + listingId + "不允许重复投标！ xxxxxx");
    		//logger.error("xxxxxx Error！" + listingId + "不允许重复投标！ xxxxxx");
    	}else if(JsonUtil.decodeUnicode(bidResult).contains("请先充值")){
    		logger.error("Error！账户余额不足，请先充值！");
    		Thread.sleep(300000);
    	}else if(JsonUtil.decodeUnicode(bidResult).contains("已满标")){
    		logger.error("xxxxxx Error！" + listingId + "已满标！xxxxxx");
    	}else if(JsonUtil.decodeUnicode(bidResult).contains("标的不存在")){
    		logger.error("xxxxxx Error！" + listingId + "标的不存在！xxxxxx");
    	}else if(bidResult.contains("\"Result\":0,\"ResultMessage\":null")){
    		successBidResult = new BidResult(listingId,bidAmount);
    		logger.info(bidResult);
    	} 
    	return successBidResult;
    }

	public static JSONArray batchListingInfosService(String token, List<Integer> listingIds) throws Exception {
		String url = "http://gw.open.ppdai.com/invest/LLoanInfoService/BatchListingInfos";
		Result result = OpenApiClient.send(url,token,new PropertyObject("ListingIds",listingIds,ValueTypeEnum.Other));
		
		JSONArray bidInfos = new JSONArray();
		if(result.isSucess()) {
			JSONObject resultObject = JSONObject.fromObject(result.getContext());
			bidInfos = resultObject.getJSONArray("LoanInfos");
		}
		return bidInfos;
	}	
}
