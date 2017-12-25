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
* @Description: �����Ĵ��ĸ�Ͷ��API���÷���
* @author Richard Zeng 
* @date 2017��10��13�� ����5:15:21 
*  
*/
public class BidService {
	
	private static Logger logger = Logger.getLogger(BidService.class); 
	
	public static String queryBalanceService(String token) throws Exception{
		//����url
		String url = "http://gw.open.ppdai.com/balance/balanceService/QueryBalance";
		
		Result result = OpenApiClient.send(url, token);
		//System.out.println(result.getContext());
		if(JsonUtil.decodeUnicode(result.getContext()).contains("���Ĳ���̫Ƶ����")) {
    		System.out.println("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
    		Thread.sleep(60000);
    	}else if(JsonUtil.decodeUnicode(result.getContext()).contains("�û���Ч�������ѹ���Ч��")) {
			logger.info("Error!�û���Ч�������ѹ���Ч��");
			//System.out.println(TokenInit.getInitFlag());
			if(TokenInit.getInitFlag()==false) {
				TokenInit.initToken();
			}
			logger.info("��������������");
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
		if(JsonUtil.decodeUnicode(result.getContext()).contains("���Ĳ���̫Ƶ����")) {
    		logger.info("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
			logger.error("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
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
        	System.out.println("��"+indexNum+"��������Ϊ��"+ loanIds.length + "���з��ձ����Ϊ��"+ j);
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
    		if(JsonUtil.decodeUnicode(result.getContext()).contains("���Ĳ���̫Ƶ����")) {
        		logger.error("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
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
    	
    	if(JsonUtil.decodeUnicode(bidResult).contains("���Ĳ���̫Ƶ����")) {
    		logger.error("xxxxxx ���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ���Ӱ� ~~~xxxxxx");
    		Thread.sleep(60000);
    	}
    	BidResult successBidResult = null;
    	//System.out.println("Success? "+ bidResult.isSucess());
    	//System.out.println(String.format("���ؽ��:%s", result.isSucess() ? bidResult : result.getErrorMessage()));
    	if(JsonUtil.decodeUnicode(bidResult).contains("����У��ʧ��")){
    		logger.error("xxxxxx Error������У��ʧ�ܣ�xxxxxx");
    	}else if(JsonUtil.decodeUnicode(bidResult).contains("�������Ʋ�����")){
    		logger.error("xxxxxx �������Ʋ����ڣ�xxxxxx");
    		AuthInfo authInfo = OpenApiClient.refreshToken(openId,token);
    		token = authInfo.getRefreshToken();
    		System.out.println("Refresh token = " + token);
        	Result retryResult = OpenApiClient.send(url,token,
    				new PropertyObject("ListingId", listingId,ValueTypeEnum.Int32),
    				new PropertyObject("Amount", bidAmount,ValueTypeEnum.Double),
    				new PropertyObject("UseCoupon", useCoupon, ValueTypeEnum.String));
        	System.out.println(String.format("���ؽ��:%s", retryResult.isSucess() ? retryResult.getContext() : retryResult.getErrorMessage()));
    	}else if(JsonUtil.decodeUnicode(bidResult).contains("�������ظ�Ͷ��")){
    		logger.error("xxxxxx Error��" + listingId + "�������ظ�Ͷ�꣡ xxxxxx");
    		//logger.error("xxxxxx Error��" + listingId + "�������ظ�Ͷ�꣡ xxxxxx");
    	}else if(JsonUtil.decodeUnicode(bidResult).contains("���ȳ�ֵ")){
    		logger.error("Error���˻����㣬���ȳ�ֵ��");
    		Thread.sleep(300000);
    	}else if(JsonUtil.decodeUnicode(bidResult).contains("������")){
    		logger.error("xxxxxx Error��" + listingId + "�����꣡xxxxxx");
    	}else if(JsonUtil.decodeUnicode(bidResult).contains("��Ĳ�����")){
    		logger.error("xxxxxx Error��" + listingId + "��Ĳ����ڣ�xxxxxx");
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
