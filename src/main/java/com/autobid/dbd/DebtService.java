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
* @Description: �����Ĵ��ĸ�Ͷ��API���÷���
* @author Richard Zeng 
* @date 2017��10��13�� ����5:15:21 
*  
*/
public class DebtService {
	
	private static Logger logger = Logger.getLogger(DebtService.class); 

	
	public static JSONArray debtListService(int indexNum) throws Exception{

		String  url = "http://gw.open.ppdai.com/invest/LLoanInfoService/DebtListNew";
    	Result result = OpenApiClient.send(url, new PropertyObject("PageIndex",indexNum,ValueTypeEnum.Int32));
		if(JsonUtil.decodeUnicode(result.getContext()).contains("���Ĳ���̫Ƶ����")) {
    		logger.info("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
			logger.error("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
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
    	if(JsonUtil.decodeUnicode(result.getContext()).contains("���Ĳ���̫Ƶ����")) {
    		logger.info("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
			logger.error("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
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
    	
    	if(JsonUtil.decodeUnicode(debtResult).contains("���Ĳ���̫Ƶ����")) {
    		logger.error("xxxxxx ���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ���Ӱ� ~~~xxxxxx");
    		Thread.sleep(60000);
    	}
    	DebtResult successBidResult = null;
    	//System.out.println("Success? "+ bidResult.isSucess());
    	//System.out.println(String.format("���ؽ��:%s", result.isSucess() ? bidResult : result.getErrorMessage()));
    	if(JsonUtil.decodeUnicode(debtResult).contains("����У��ʧ��")){
    		logger.error("xxxxxx Error������У��ʧ�ܣ�xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("�������Ʋ�����")){
    		logger.error("xxxxxx �������Ʋ����ڣ�xxxxxx");
    		AuthInfo authInfo = OpenApiClient.refreshToken(openId,token);
    		token = authInfo.getRefreshToken();
    		System.out.println("Refresh token = " + token);
        	Result retryResult = OpenApiClient.send(url,token,
					new PropertyObject("debtDealId", debtId, ValueTypeEnum.Int32));
        	System.out.println(String.format("���ؽ��:%s", retryResult.isSucess() ? retryResult.getContext() : retryResult.getErrorMessage()));
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("�������ظ�Ͷ��")){
    		logger.error("xxxxxx Error��" + debtId + "�������ظ�Ͷ�꣡ xxxxxx");
    		//logger.error("xxxxxx Error��" + listingId + "�������ظ�Ͷ�꣡ xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("���ȳ�ֵ")){
    		logger.error("Error���˻����㣬���ȳ�ֵ��");
    		Thread.sleep(300000);
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("������")){
    		logger.error("xxxxxx Error��" + debtId + "�����꣡xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("��Ĳ�����")){
    		logger.error("xxxxxx Error��" + debtId + "��Ĳ����ڣ�xxxxxx");
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
    	
    	if(JsonUtil.decodeUnicode(debtResult).contains("���Ĳ���̫Ƶ����")) {
    		logger.error("xxxxxx ���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ���Ӱ� ~~~xxxxxx");
    		Thread.sleep(60000);
    	}
    	DebtResult successBidResult = null;
    	//System.out.println("Success? "+ bidResult.isSucess());
    	//System.out.println(String.format("���ؽ��:%s", result.isSucess() ? bidResult : result.getErrorMessage()));
    	if(JsonUtil.decodeUnicode(debtResult).contains("����У��ʧ��")){
    		logger.error("xxxxxx Error������У��ʧ�ܣ�xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("�������Ʋ�����")){
    		logger.error("xxxxxx �������Ʋ����ڣ�xxxxxx");
    		AuthInfo authInfo = OpenApiClient.refreshToken(openId,token);
    		token = authInfo.getRefreshToken();
    		System.out.println("Refresh token = " + token);
        	Result retryResult = OpenApiClient.send(url,token,
					new PropertyObject("debtDealId", debtId, ValueTypeEnum.Int32));
        	System.out.println(String.format("���ؽ��:%s", retryResult.isSucess() ? retryResult.getContext() : retryResult.getErrorMessage()));
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("�������ظ�Ͷ��")){
    		logger.error("xxxxxx Error��" + debtId + "�������ظ�Ͷ�꣡ xxxxxx");
    		//logger.error("xxxxxx Error��" + listingId + "�������ظ�Ͷ�꣡ xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("���ȳ�ֵ")){
    		logger.error("Error���˻����㣬���ȳ�ֵ��");
    		Thread.sleep(300000);
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("������")){
    		logger.error("xxxxxx Error��" + debtId + "�����꣡xxxxxx");
    	}else if(JsonUtil.decodeUnicode(debtResult).contains("��Ĳ�����")){
    		logger.error("xxxxxx Error��" + debtId + "��Ĳ����ڣ�xxxxxx");
    	}else if(debtResult.contains("\"Result\":0,\"ResultMessage\":null")){
    		successBidResult = new DebtResult(debtId,debtInfo.getListingId(),debtInfo.getPriceForSale());
    		logger.info(debtResult);
    	} 
    	return successBidResult;
	}
	
}
