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
* @Description: ������ݴ������
* @author Richard Zeng 
* @date 2017��10��13�� ����5:11:12 
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
			if(JsonUtil.decodeUnicode(accountCategory).contains("�û��ֽ����")){
				canUseBalance = balanceObject.getDouble("Balance");
			}
		}
		logger.info("�������Ϊ��" + canUseBalance);
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
    	logger.info("�������Ϊ��"+ loanIds.length + "���з��ձ����Ϊ��"+ j);

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
    	/*if(JsonUtil.decodeUnicode(batchListInfos).contains("���Ĳ���̫Ƶ����")) {
    		System.out.println("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
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
    	loanInfoMap.put("Amount",loanInfoObj.getInt("Amount"));							//�����					
		loanInfoMap.put("CreditCode",loanInfoObj.getString("CreditCode"));				//��ĵȼ�
		loanInfoMap.put("RemainFunding",loanInfoObj.getInt("RemainFunding"));			//ʣ���Ͷ���
    	loanInfoMap.put("Gender",loanInfoObj.getInt("Gender"));							//�Ա�	
		loanInfoMap.put("EducationDegree",loanInfoObj.getString("EducationDegree"));	//ѧ��
		loanInfoMap.put("StudyStyle",loanInfoObj.getString("StudyStyle"));				//ѧϰ��ʽ
		loanInfoMap.put("Age",loanInfoObj.getInt("Age"));								//����
		loanInfoMap.put("SuccessCount",loanInfoObj.getInt("SuccessCount"));				//�ɹ�������
		loanInfoMap.put("NormalCount",loanInfoObj.getInt("NormalCount"));				//�����������
		loanInfoMap.put("OverdueLessCount",loanInfoObj.getInt("OverdueLessCount"));		//����(1-15)�������
		loanInfoMap.put("OverdueMoreCount",loanInfoObj.getInt("OverdueMoreCount"));		//����(15������)�������
		loanInfoMap.put("OwingAmount",loanInfoObj.getInt("OwingAmount"));				//�������
		loanInfoMap.put("HighestDebt",loanInfoObj.getInt("HighestDebt"));				//��ʷ��߸�ծ
		loanInfoMap.put("HighestPrincipal",JsonUtil.parseInt(loanInfoObj.get("HighestPrincipal"))); //������߽����
		loanInfoMap.put("TotalPrincipal",JsonUtil.parseInt(loanInfoObj.get("TotalPrincipal")));  	//�ۼƽ����
		loanInfoMap.put("LastSuccessBorrowTime",loanInfoObj.getString("LastSuccessBorrowTime")); 	//�ϴγɹ����ʱ��
		loanInfoMap.put("CertificateValidate",loanInfoObj.getInt("CertificateValidate"));			//ѧ����֤0 δ��֤ 1����֤
		loanInfoMap.put("NciicIdentityCheck",loanInfoObj.getInt("NciicIdentityCheck"));	//������֤0 δ��֤ 1����֤
		loanInfoMap.put("VideoValidate",loanInfoObj.getInt("VideoValidate"));			//��Ƶ��֤0 δ��֤ 1����֤
		loanInfoMap.put("CreditValidate",loanInfoObj.getInt("CreditValidate"));			//������֤0 δ��֤ 1����֤
		loanInfoMap.put("EducateValidate",loanInfoObj.getInt("EducateValidate"));		//ѧ����֤0 δ��֤ 1����֤
		return loanInfoMap;
    }
    
}
