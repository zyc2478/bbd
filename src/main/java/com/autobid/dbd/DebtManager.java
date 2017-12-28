package com.autobid.dbd;

import java.util.ArrayList;


import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.autobid.bbd.AuthInit;
import com.autobid.bbd.BidDataParser;
import com.autobid.bbd.BidService;

import com.autobid.entity.DebtResult;

import com.autobid.filter.DebtInfosListFilter;
import com.autobid.filter.BidInfosFilter;
import com.autobid.filter.DebtListFilter;
import com.autobid.entity.Constants;
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;
import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



/** 
* @ClassName: BidManager 
* @Description: �Զ�Ͷ���������
* @author Richard Zeng 
* @date 2017��10��13�� ����5:14:02 
* @version 1.0 
*/
public class DebtManager implements Constants {

	private static String token = "";
    private static String openId;
    private static String redisHost;
    private static int redisPort;
	private static Jedis jedis;
	private static ConfBean confBean;
	private ArrayList<DebtResult> successDebtList = new ArrayList<DebtResult>();
	
	private static Logger logger = Logger.getLogger(DebtManager.class);  

    //����
    private volatile static DebtManager instance;
    
    public DebtManager(){}
    public static DebtManager getInstance(){
    	if(instance == null){
    		synchronized (DebtManager.class){
    			if(instance == null){
    				instance = new DebtManager();
    			}
    		}
    	}
    	return instance;
    }
    //String bidList; 
    String loanList;
    //int listingId;
    int[] loanIds;
	//HashMap<Integer,String> bidResultMap = new HashMap<Integer,String>();
	//int bidAmount = 0;

	//static double balance;
    static{
		try{
			AuthInit.init();
		    confBean = ConfUtil.readAllToBean();
			openId = confBean.getOpenId();
    		redisHost = confBean.getRedisHost();
    		redisPort = Integer.parseInt(confBean.getRedisPort());
    		
    		jedis = new Jedis(redisHost,redisPort);
			
			//���TokenInit��������ڣ����ʼ��Token���洢��Redis��
			if(!TokenUtil.determineTokenInitExsits()) {
				TokenInit.initToken();
			}
			//���Token�쵽�ڣ����ȡһ����Token		
			if(TokenUtil.determineRefreshDate()) {
				TokenUtil.genNewToken();
			}
			//��ȡToken�������ļ��������ȣ�û�����ȡRedis
			token = TokenUtil.getToken();
			//logger.info("token:" + token);
			//String balanceJson = BidService.queryBalanceService(token); 

		}catch(Exception e){
			e.printStackTrace();
		}
	}
    
	@Test
    public void debtExcecute() throws Exception {  	
		logger.info("debtExcecute");
		int overdueSwitch = Integer.parseInt(confBean.getDebtOverdueSwitch());
		int debtMix = Integer.parseInt(confBean.getDebtMix());

		if(overdueSwitch==0) {
			debtNoOverdueExecute();
		}else{
			if(debtMix==1) {
				debtOverdueExecute();
			}else {
				debtNoOverdueExecute();
				debtOverdueExecute();
			} 
		}
/*		logger = null;
		instance = null;*/
    }

	private void debtNoOverdueExecute() throws Exception {
		logger.info("debtNoOverdueExecute");
		execute();
	}
	
	private void debtOverdueExecute() throws Exception {
		logger.info("debtOverdueExcecute");
		execute();
	}
	
	private void execute() throws Exception {
		String balanceJson = BidService.queryBalanceService(token); 
	    double balance = BidDataParser.getBalance(balanceJson);	    
    	if(!DebtDetermine.determineBalance(balance)) {
    		return;
    	}    
		int indexNum = 1;
		int debtCount = 0;
		//int debtFCount = 0;
		int totalDebtCount = 0;
		DebtListFilter dlf = new DebtListFilter();
		DebtInfosListFilter dilf = new DebtInfosListFilter();
		BidInfosFilter bif = new BidInfosFilter();
		int debtGroups = Integer.parseInt(confBean.getDebtGroups());
		
		do {
			JSONArray debtListArray = DebtService.debtListService(indexNum);		
			debtCount = debtListArray.size();
			totalDebtCount += debtCount;
			//����ȡ��debtList����DebtListFilter�ж���Ĺ������
			//logger.info("��"+indexNum+"�� �����±�ģ�");
/*			for(int i=0;i<debtListArray.size();i++) {
				JSONObject debtListObject = debtListArray.getJSONObject(i);
				//logger.info(debtListObject);	
			}
			*/
			JSONArray dlFiltered = dlf.filter(debtListArray,confBean);
			
			logger.info("��"+indexNum+"�� �������˺�dlFiltered������" + dlFiltered.size());
			//debtFCount += dlFiltered.size();
			
			//System.out.println("debtFCount "+ indexNum + " is: "+ debtFCount);
			
			//��debtList�з�Ϊ10��һ��,��ƴ�ӳ�һ��Collector
			ArrayList<JSONArray> daList = DebtDataParser.getDebtsCollector(dlFiltered);
						
			for(int i=0;i<daList.size();i++) {
							
				//��ȡծȨ�����ϸ
				
				//logger.info(daList.get(i));
				
				JSONArray debtInfosList = DebtService.batchDebtInfosService(daList.get(i));
				
/*				System.out.println(debtInfosList.size());
				for(int m=0;m<debtInfosList.size();m++) {
					logger.info(debtInfosList.getJSONObject(m));
				}
				*/
				//ͨ����ծȨ��ϸ���ݷ�����ѡ�����Ͷ��ծȨ��
				JSONArray dFiltered = dilf.filter(debtInfosList,confBean);
				
				//System.out.println("��"+indexNum+"�� �� "+i+"�� dFiltered.size =  ��" + dFiltered.size());
				
				//��ծȨ������ɸѡ����ListingId��List,�ٵ��÷����ѯ��Щ�����ϸ��Ϣ
				List<Integer> listingIds = DebtDataParser.getListingIds(dFiltered);
				
/*				listingIds = new ArrayList<Integer>();
				listingIds.add(55476937);*/
				
				JSONArray batchBidInfos = BidService.batchListingInfosService(token, listingIds);
				
				//ͨ����ծȨ��Ӧ������ݷ�������ѡ����Ͷ�ı�
				JSONArray bidFiltered = bif.filter(batchBidInfos,confBean);				
				//System.out.println("��"+indexNum+"�� �� "+i+"�� bidFiltered.size = ��" + bidFiltered.size());
				
				//��֮ת��Ϊ��Ͷ��ծȨ��
				JSONArray dbFiltered = DebtDataParser.parseDebtInfoFromBids(dFiltered,bidFiltered);
				
				//System.out.println("��"+indexNum+"�� �� "+i+"���ͶծȨ��������" + dbFiltered.size());
				
				//�������飬��ÿ����ͶծȨ�곢��Ͷ��
				for(int j=0;j<dbFiltered.size();j++) {
					JSONObject di = dbFiltered.getJSONObject(j);
					DebtResult debtResult = null;
					if(!DebtDetermine.determineDuplicateId(di.getInt("DebtId"),jedis)){
						debtResult = DebtService.buyDebtService(token,openId,di);
					}
					if(debtResult != null) {
						if(!successDebtList.contains(debtResult)) {
				    		successDebtList.add(debtResult);
						}
						jedis.setex(String.valueOf(di.getInt("DebtId")), 172800, String.valueOf(di.getInt("ListingId")));
						logger.info(indexNum+": "+di);
						//System.out.println("��ͶծȨ�� DebtId:"+ di.getInt("DebtId") + ", ListingId:" + di.getInt("ListingId") + ", Price:" + di.getDouble("PriceforSale"));
					}
				}
				Thread.sleep(200);
			}
			indexNum ++;
			//logger.info(indexNum);

		}while(debtCount == 50 && indexNum <= debtGroups); //ÿҳ50��Ԫ��		
		
		logger.info("Total Debt Count is :"+totalDebtCount);
		
		debtResultsPrint(successDebtList,totalDebtCount);		
	}
	

	private void debtResultsPrint(ArrayList<DebtResult> successDebtList,int successDebtCount) {
    	if(successDebtCount==0){
    		System.out.println("*~~~~~~~~~~~~~~~~~~~~~�ܱ�Ǹ��û�п�ͶծȨ~~~~~~~~~~~~~~~~~~~~~~~~~*");
    	}else if(successDebtList.isEmpty()){
    		System.out.println("*~~~~~~~~~~~~~~~~~~~�ܱ�Ǹ��û���ҵ�����ծȨ~~~~~~~~~~~~~~~~~~~~~~~*");
    	}else{
    		for(int i=0;i<successDebtList.size();i++) {
    			DebtResult dr = successDebtList.get(i);
        		System.out.println("*~~~~~~~~~~~ DebtId:"+dr.getDebtId() + ", ListingId:"+
        				dr.getListingId() + ", Price:" + dr.getPrice()+"~~~~~~~~~~~*");
    		}
    	}		
	}
 }
 