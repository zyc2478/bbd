package com.autobid.dbd;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.autobid.entity.DebtInfo;
import com.autobid.entity.DebtResult;
import com.autobid.util.JsonUtil;
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
public class DebtService {
	
	private static Logger logger = Logger.getLogger(DebtService.class); 

	
	public static JSONArray debtListService(int indexNum) throws Exception{

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
    		JSONObject debtListJson = JSONObject.fromObject(debtListResult);
    		//logger.info(balanceJson);
    		debtListArray = debtListJson.getJSONArray("DebtInfos");
    	}else{
    		logger.error(result.getErrorMessage());
    	}
		return debtListArray;
	}
	
	public static JSONArray batchDebtInfosService(JSONArray diArray) throws Exception {
		String  url = "https://openapi.ppdai.com/invest/LLoanInfoService/BatchDebtInfos";
		List<Integer> debtIds = new ArrayList<Integer>();
		//System.out.println(diArray);
		for(int i=0;i<diArray.size();i++) {
			//debtIds.add(new Integer();
			debtIds.add(Integer.valueOf(((JSONObject) diArray.get(i)).getString("DebtdealId")));
		}
    	Result result = OpenApiClient.send(url,new PropertyObject("DebtIds", debtIds, ValueTypeEnum.Other));
    	JSONArray debtInfosArray = null;
    	if(JsonUtil.decodeUnicode(result.getContext()).contains("您的操作太频繁啦")) {
    		logger.info("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
			logger.error("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
    		Thread.sleep(60000);
    	}
    	if(result.isSucess()){
    		String batchDebtInfosResult = result.getContext();
    		
    		JSONObject batchDebtInfosJson = JSONObject.fromObject(batchDebtInfosResult);
    		//logger.info(balanceJson);

    		debtInfosArray = batchDebtInfosJson.getJSONArray("DebtInfos");
    		//logger.info(debtInfosArray);
    	}else{
    		logger.error(result.getErrorMessage());
    	}
    	return debtInfosArray;
	}

	public static DebtResult buyDebtService(String token, String openId, JSONObject debtInfo) throws Exception {
		System.out.println("-------------------buyDebtService----------------------------");
    	String url = "https://openapi.ppdai.com/invest/BidService/BuyDebt";
    	int debtId = debtInfo.getInt("DebtId");
    	Result result = OpenApiClient.send(url,token,
    					new PropertyObject("debtDealId", debtId, ValueTypeEnum.Int32));
    	String debtResult = result.getContext();
    	
    	if(JsonUtil.decodeUnicode(debtResult).contains("您的操作太频繁啦")) {
    		logger.error("xxxxxx 您的操作太频繁啦！先喝杯茶吧，歇一分钟吧 ~~~xxxxxx");
    		Thread.sleep(60000);
    	}
    	DebtResult successBidResult = null;
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
					new PropertyObject("debtDealId", debtId, ValueTypeEnum.Int32));
        	System.out.println(String.format("返回结果:%s", retryResult.isSucess() ? retryResult.getContext() : retryResult.getErrorMessage()));
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("不允许重复投标")){
    		logger.error("xxxxxx Error！" + debtId + "不允许重复投标！ xxxxxx");
    		//logger.error("xxxxxx Error！" + listingId + "不允许重复投标！ xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("请先充值")){
    		logger.error("Error！账户余额不足，请先充值！");
    		Thread.sleep(300000);
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("已满标")){
    		logger.error("xxxxxx Error！" + debtId + "已满标！xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("标的不存在")){
    		logger.error("xxxxxx Error！" + debtId + "标的不存在！xxxxxx");
    	}else if(debtResult.contains("\"Result\":0,\"ResultMessage\":null")){
    		successBidResult = new DebtResult(debtId,debtInfo.getInt("ListingId"),debtInfo.getDouble("PriceforSale"));
    		logger.info(debtResult);
    	} 
    	return successBidResult;
	}
	
	public static DebtResult buyDebtService(String token, String openId, DebtInfo debtInfo) throws Exception {
		System.out.println("-------------------buyDebtService----------------------------");
    	String url = "https://openapi.ppdai.com/invest/BidService/BuyDebt";
    	int debtId = debtInfo.getDebtId();
    	Result result = OpenApiClient.send(url,token,
    					new PropertyObject("debtDealId", debtId, ValueTypeEnum.Int32));
    	String debtResult = result.getContext();
    	
    	if(JsonUtil.decodeUnicode(debtResult).contains("您的操作太频繁啦")) {
    		logger.error("xxxxxx 您的操作太频繁啦！先喝杯茶吧，歇一分钟吧 ~~~xxxxxx");
    		Thread.sleep(60000);
    	}
    	DebtResult successBidResult = null;
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
					new PropertyObject("debtDealId", debtId, ValueTypeEnum.Int32));
        	System.out.println(String.format("返回结果:%s", retryResult.isSucess() ? retryResult.getContext() : retryResult.getErrorMessage()));
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("不允许重复投标")){
    		logger.error("xxxxxx Error！" + debtId + "不允许重复投标！ xxxxxx");
    		//logger.error("xxxxxx Error！" + listingId + "不允许重复投标！ xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("请先充值")){
    		logger.error("Error！账户余额不足，请先充值！");
    		Thread.sleep(300000);
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("已满标")){
    		logger.error("xxxxxx Error！" + debtId + "已满标！xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("标的不存在")){
    		logger.error("xxxxxx Error！" + debtId + "标的不存在！xxxxxx");
    	}else if(debtResult.contains("\"Result\":0,\"ResultMessage\":null")){
    		successBidResult = new DebtResult(debtId,debtInfo.getListingId(),debtInfo.getPriceForSale());
    		logger.info(debtResult);
    	} 
    	return successBidResult;
	}
	
}
