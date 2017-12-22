package com.autobid.dbd;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.autobid.bbd.AuthInit;
import com.autobid.bbd.BidDataParser;
import com.autobid.bbd.BidDetermine;
import com.autobid.bbd.BidService;

import com.autobid.entity.DebtResult;

import com.autobid.filter.DebtInfosListFilter;
import com.autobid.filter.BidInfosFilter;
import com.autobid.filter.DebtListFilter;
import com.autobid.entity.Constants;
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

	//private static final int NONE = 0;
	private static int MIN_BID_AMOUNT;
	
	private static String token = "";
    private static String openId;
    private static String redisHost;
    private static int redisPort;
	private static Jedis jedis;
    
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
			MIN_BID_AMOUNT = Integer.parseInt(ConfUtil.getProperty("min_bid_amount"));
			openId = ConfUtil.getProperty("open_id");
    		redisHost = ConfUtil.getProperty("redis_host");
    		redisPort = Integer.parseInt(ConfUtil.getProperty("redis_port"));
    		
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
		System.out.println("debtExcecute");
    	ArrayList<DebtResult> successDebtList = new ArrayList<DebtResult>();
		int debtIdCount = 0;
		
		debtNoOverdueExecute(successDebtList,debtIdCount);
		
		if(Integer.parseInt(ConfUtil.getProperty("debt_overdue_swith"))==1) {
			debtOverdueExecute(successDebtList, debtIdCount);
		}		
		logger = null;
		instance = null;
    }

	private void debtNoOverdueExecute(ArrayList<DebtResult> successDebtList,int debtIdCount) throws Exception {
		System.out.println("debtNoOverdueExecute");
		execute(successDebtList,debtIdCount);
	}
	
	private void debtOverdueExecute(ArrayList<DebtResult> successDebtList,int debtIdCount) throws Exception {
		System.out.println("debtOverdueExcecute");
		execute(successDebtList,debtIdCount);
	}
	
	private void execute(ArrayList<DebtResult> successDebtList,int debtIdCount) throws Exception {
		String balanceJson = BidService.queryBalanceService(token); 
	    double balance = BidDataParser.getBalance(balanceJson);	    
    	if(!BidDetermine.determineBalance(balance)) {
    		return;
    	}    
		int indexNum = 1;

		do {
			JSONArray debtListArray = DebtService.debtListService(indexNum);			
			
			//����ȡ��debtList����DebtListFilter�ж���Ĺ������
			JSONArray dlFiltered = DebtListFilter.filter(debtListArray);
			debtIdCount = dlFiltered.size();
			
			//��debtList�з�Ϊ10��һ��,��ƴ�ӳ�һ��Collector
			ArrayList<JSONArray> daList = DebtDataParser.getDebtsCollector(dlFiltered);
    		
			for(int i=0;i<daList.size();i++) {
							
				//��ȡծȨ�����ϸ
				JSONArray debtInfosList = DebtService.batchDebtInfosService((JSONArray)daList.get(i));
				
				//ͨ����ծȨ��ϸ���ݷ�����ѡ�����Ͷ��ծȨ��
				JSONArray dFiltered = DebtInfosListFilter.filter(debtInfosList);
				
				//ͨ����ծȨ��Ӧ������ݷ�����ѡ������տ�Ͷ��ծȨ��
				JSONArray dbFiltered = BidInfosFilter.filter(dFiltered);
				
				//�������飬��ÿ����ͶծȨ�곢��Ͷ��
				for(int j=0;j<dbFiltered.size();j++) {
					JSONObject di = dbFiltered.getJSONObject(i);
					DebtResult debtResult = DebtService.buyDebtService(token,openId,di);
					if(debtResult != null) {
						successDebtList.add(debtResult);
					}
				}
			}
			indexNum ++;
		}while(debtIdCount == 50); //ÿҳ50��Ԫ��
		debtResultsPrint(successDebtList,debtIdCount);		
	}
	

	private void debtResultsPrint(ArrayList<DebtResult> successDebtList,int debtListSize) {
    	if(debtListSize==0){
    		System.out.println("*~~~~~~~~~~~~~~~~~~~~~�ܱ�Ǹ��û�п�ͶծȨ~~~~~~~~~~~~~~~~~~~~~~~~~*");
    	}else if(successDebtList.isEmpty()){
    		System.out.println("*~~~~~~~~~~~~~~~~~~~�ܱ�Ǹ��û���ҵ�����ծȨ~~~~~~~~~~~~~~~~~~~~~~~*");
    	}else{
    		for(int i=0;i<successDebtList.size();i++) {
    			DebtResult dr = successDebtList.get(i);
        		System.out.println("*~~~~~~~~~~~ DebtId:"+dr.getDebtId() + "ListingId:"+
        				dr.getListingId() + "Price:" + dr.getPrice()+"~~~~~~~~~~~*");
    		}
    	}		
	}
 }
 