package com.autobid.dbd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.autobid.bbd.AuthInit;
import com.autobid.bbd.BidDataParser;
import com.autobid.bbd.BidDetermine;
import com.autobid.bbd.BidService;
import com.autobid.criteria.BasicCriteria;
import com.autobid.criteria.BeginCriteriaGroup;
import com.autobid.criteria.DebtRateCriteriaGroup;
import com.autobid.criteria.EduCriteriaGroup;
import com.autobid.criteria.EduDebtCriteriaGroup;
import com.autobid.entity.CriteriaGroup;
import com.autobid.entity.DebtListResult;
import com.autobid.entity.BidResult;
import com.autobid.entity.LoanListResult;
import com.autobid.entity.Constants;
import com.autobid.util.ConfUtil;
import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;


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
		String balanceJson = BidService.queryBalanceService(token); 
	    double balance = BidDataParser.getBalance(balanceJson);
	    
    	if(!BidDetermine.determineBalance(balance)) {
    		return;
    	}
    	ArrayList<BidResult> successBidList = new ArrayList<BidResult>();    
		int indexNum = 1;
		int debtIdCount = 0;
		List<Integer> listingIds;
		int listingId = 44408199;
		//DebtListResult debtListResult = DebtService.debtListService(1);	
		//DebtService.debtService(token, openId, listingId);
		do {

			DebtListResult debtListResult = DebtService.debtListService(indexNum);	
			debtIdCount = debtListResult.getDebtIdCount();
			//��������ȡListingIds
	    	listingIds = BidDataParser.getListingIds(debtListResult.getDebtList());	
	    	
    		//��ListingIds�зֳ�10��һ�飬��ƴ�ӳ�һ��Collector
    		Integer[][] listingIdsParted = BidDataParser.getListingIdsParted(listingIds);
    		ArrayList<List<Integer>> listingIdsCollector = BidDataParser.getLisiingIdsCollector(listingIdsParted);
    		
			indexNum ++;
		}while(debtIdCount != 50);
		/*do{
			
			LoanListResult loanListResult = DebtService.debtListService(indexNum);
			System.out.println(loanListResult.getLoanIdCount());
			System.out.println(loanListResult.getLoanList());
			loanIdCount = loanListResult.getLoanIdCount();
	    	//��������ȡListingIds
	    	listingIds = BidDataParser.getListingIds(loanListResult.getLoanList());	
    		listingIds = new ArrayList<Integer>();
    		listingIds.add(86084296);
    		//System.out.println(listingIds);
    		
    		//��ListingIds�зֳ�10��һ�飬��ƴ�ӳ�һ��Collector
    		Integer[][] listingIdsParted = BidDataParser.getListingIdsParted(listingIds);

    		ArrayList<List<Integer>> listingIdsCollector = BidDataParser.getLisiingIdsCollector(listingIdsParted);
    		
    		//System.out.println(listingIdsCollector);
    		
    		//ѭ��������ÿ�鴫��ӿ� BatchListingInfos,��������ϲ���Collector
    		ArrayList<String> batchListInfosCollector = BidService.batchListInfosCollectorService(
    				token, listingIdsCollector);
    		//System.out.println(batchListInfosCollector);
    		
    		
    		//���ϲ����Collector�����JSONArray���飬���ٺϲ�Ϊ�µ�Collector
    		ArrayList<JSONArray> loanInfosCollector = BidDataParser.getLoanInfosCollector(batchListInfosCollector);
    		
    		//System.out.println(loanInfosCollector);
    		
    		//ѭ�������ó�JSONArray����ÿ��JSONArray�ٷֲ�Ϊ������JSONObject
    		Iterator<JSONArray> loanInfosIt = loanInfosCollector.iterator();
    		
    		while(loanInfosIt.hasNext()){
    			JSONArray loanInfosArray = loanInfosIt.next();
    			for(int i=0;i<loanInfosArray.length();i++){
    				JSONObject loanInfoObj = loanInfosArray.getJSONObject(i);
       			    
    				//System.out.println(loanInfoObj);
    				
    				//���õ���ÿ�����loanInfo����Ϊÿ���ֶΣ�������ϲ�ΪHashMap
    				HashMap<String,Object> loanInfoMap = BidDataParser.getLoanInfoMap(loanInfoObj);
    				
    				int listingId = loanInfoObj.getInt("ListingId");
    				//���г�ʼ�����ж�
    				int basicCriteriaLevel = new BasicCriteria().getLevel(loanInfoMap);
    				//new BasicCriteria().printCriteria(loanInfoMap);
    				//System.out.println("basicCriteriaLevel is :" + basicCriteriaLevel);

    				//���ݳ�ʼ���Է��ز�ͬ��ѡ��ͬ�Ĳ�����
    				CriteriaGroup criteriaGroup = new CriteriaGroup();    				
					switch(basicCriteriaLevel){
						case PERFECT: 	criteriaGroup = new EduDebtCriteriaGroup(); break;
						case GOOD:		criteriaGroup = new EduCriteriaGroup(); break;
						case OK:		criteriaGroup = new DebtRateCriteriaGroup(); break;
						case SOSO:		criteriaGroup = new BeginCriteriaGroup(); break;
						default:		criteriaGroup = null;break;
					}

    				BidResult successBidResult = null;
    				int bidAmount = 0;
    				//����������Բ�ƥ�䣬���ж�������ӦͶ���
    				if( basicCriteriaLevel > 0){
    					
    					//���ж��Ƿ���Redis���ظ�������Ͷ���ñ��
    					//bidAmount = new BidDetermine().determineCriteriaGroup(criteriaGroup, loanInfoMap);
    					if(!BidDetermine.determineDuplicateId(listingId,jedis)){
    						System.out.println("====== listingId: "+listingId + ", Start bidding ====== ");
    						//logger.info("listingId: " + listingId + ", JSON is: " + loanInfoMap);
    						bidAmount = new BidDetermine().determineCriteriaGroup(criteriaGroup, loanInfoMap);
    						System.out.println("****** listingId: "+listingId + ", total Amount is: " + bidAmount + " ******" );
    					}else{
    						//logger.error("xxxxxx " + listingId + "��Redis���ظ��� xxxxxx");
    						
    					}
    				}				
					if(bidAmount!=0){
						if(balance > bidAmount) {
							//logger.info("�ñ�Ŀ�Ͷ");
							successBidResult = BidService.biddingService(token, openId, listingId,bidAmount);
						}else if(balance > MIN_BID_AMOUNT){
							logger.info("�ñ�ֻ��Ͷ" + MIN_BID_AMOUNT);
							successBidResult = BidService.biddingService(token, openId, listingId,MIN_BID_AMOUNT);
						}else{
							logger.error("����,��ȴ�5���Ӻ�����");
							Thread.sleep(300000);
						}
					}
			    	if(successBidResult != null) {
						//logger.info(listingId+" will be bidden~~~~~~~~~~bidAmount is��"+bidAmount);
						if(!successBidList.contains(successBidResult)) {
				    		successBidList.add(successBidResult);
						}
						jedis.setex(String.valueOf(listingId), 172800, String.valueOf(bidAmount));
					}
    			}
    		}
			//System.out.println(indexNum);
	    	indexNum++;
		}while(loanIdCount == 50);*/
		//System.out.println("*~~~~~~~~~~~~~~~~~~~~���ִ����ϣ�Ͷ�������£�~~~~~~~~~~~~~~~~~~~*");
    	//bidResultsPrint(successBidList,listingIds.size());
		logger = null;
		instance = null;

    }
	private void bidResultsPrint(ArrayList<BidResult> successBidList,int listingIdsSize) {
    	if(listingIdsSize==0){
    		System.out.println("*~~~~~~~~~~~~~~~~~~~~~�ܱ�Ǹ��û�п�Ͷ���~~~~~~~~~~~~~~~~~~~~~~~~~*");
    	}else if(successBidList.isEmpty()){
    		System.out.println("*~~~~~~~~~~~~~~~~~~~�ܱ�Ǹ��û���ҵ����ʱ��~~~~~~~~~~~~~~~~~~~~~~~*");
    	}else{
    		
           	Iterator<BidResult> successBidIt = successBidList.iterator();
        	while(successBidIt.hasNext()){
        		BidResult successResult = (BidResult)successBidIt.next();
        		System.out.println("*~~~~~~~~~~~ BidId:"+successResult.getBidId()+",BidAmount:"+
        				successResult.getBidAmount() + "~~~~~~~~~~~*");
        	}
    	}		
	}
 }
 