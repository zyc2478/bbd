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
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;
import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



/** 
* @ClassName: BidManager 
* @Description: 自动投标的主程序
* @author Richard Zeng 
* @date 2017年10月13日 下午5:14:02 
* @version 1.0 
*/
public class DebtManager implements Constants {

	private static int MIN_BID_AMOUNT;
	private static String token = "";
    private static String openId;
    private static String redisHost;
    private static int redisPort;
	private static Jedis jedis;
	private static ConfBean confBean;
    
	private static Logger logger = Logger.getLogger(DebtManager.class);  

    //单例
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
			MIN_BID_AMOUNT = Integer.parseInt(confBean.getMinBidAmount());
			openId = confBean.getOpenId();
    		redisHost = confBean.getRedisHost();
    		redisPort = Integer.parseInt(confBean.getRedisPort());
    		
    		jedis = new Jedis(redisHost,redisPort);
			
			//如果TokenInit配置项不存在，则初始化Token，存储在Redis中
			if(!TokenUtil.determineTokenInitExsits()) {
				TokenInit.initToken();
			}
			//如果Token快到期，则获取一个新Token		
			if(TokenUtil.determineRefreshDate()) {
				TokenUtil.genNewToken();
			}
			//获取Token，配置文件有则优先，没有则获取Redis
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
		
		debtNoOverdueExecute(successDebtList);
		
		if(Integer.parseInt(ConfUtil.getProperty("debt_overdue_switch"))==1) {
			debtOverdueExecute(successDebtList);
		}		
		logger = null;
		instance = null;
    }

	private void debtNoOverdueExecute(ArrayList<DebtResult> successDebtList) throws Exception {
		System.out.println("debtNoOverdueExecute");
		execute(successDebtList);
	}
	
	private void debtOverdueExecute(ArrayList<DebtResult> successDebtList) throws Exception {
		System.out.println("debtOverdueExcecute");
		execute(successDebtList);
	}
	
	private void execute(ArrayList<DebtResult> successDebtList) throws Exception {
		String balanceJson = BidService.queryBalanceService(token); 
	    double balance = BidDataParser.getBalance(balanceJson);	    
    	if(!BidDetermine.determineBalance(balance)) {
    		return;
    	}    
		int indexNum = 1;
		int debtCount = 0;
		int debtFCount = 0;
		int totalDebtCount = 0;
		do {
			JSONArray debtListArray = DebtService.debtListService(indexNum);		
			debtCount = debtListArray.size();
			totalDebtCount += debtCount;
			//将获取的debtList按照DebtListFilter中定义的规则过滤
			JSONArray dlFiltered = DebtListFilter.filter(debtListArray);
			
			debtFCount += dlFiltered.size();
			
			System.out.println("debtFCount "+ indexNum + " is: "+ debtFCount);
			
			//将debtList切分为10个一组,再拼接成一个Collector
			ArrayList<JSONArray> daList = DebtDataParser.getDebtsCollector(dlFiltered);
    		
			for(int i=0;i<daList.size();i++) {
							
				//获取债权标的明细
				JSONArray debtInfosList = DebtService.batchDebtInfosService((JSONArray)daList.get(i));
				
				//通过对债权明细数据分析，选择出可投的债权标
				JSONArray dFiltered = DebtInfosListFilter.filter(debtInfosList);
				
				
				
				//通过对债权对应标的数据分析，选择出最终可投的债权标
				JSONArray dbFiltered = BidInfosFilter.filter(dFiltered);
				
				//System.out.println("dbFiltered.size() is :" + dbFiltered.size());
				
				//遍历数组，对每个可投债权标尝试投标
				for(int j=0;j<dbFiltered.size();j++) {
					JSONObject di = dbFiltered.getJSONObject(i);
					DebtResult debtResult = DebtService.buyDebtService(token,openId,di);
					if(debtResult != null) {
						successDebtList.add(debtResult);
					}
				}
			}
			indexNum ++;
		}while(debtCount  == 50); //每页50个元素
		
		System.out.println("Total Debt Count is :"+totalDebtCount);
		
		debtResultsPrint(successDebtList,totalDebtCount);		
	}
	

	private void debtResultsPrint(ArrayList<DebtResult> successDebtList,int successDebtCount) {
    	if(successDebtCount==0){
    		System.out.println("*~~~~~~~~~~~~~~~~~~~~~很抱歉，没有可投债权~~~~~~~~~~~~~~~~~~~~~~~~~*");
    	}else if(successDebtList.isEmpty()){
    		System.out.println("*~~~~~~~~~~~~~~~~~~~很抱歉，没有找到合适债权~~~~~~~~~~~~~~~~~~~~~~~*");
    	}else{
    		for(int i=0;i<successDebtList.size();i++) {
    			DebtResult dr = successDebtList.get(i);
        		System.out.println("*~~~~~~~~~~~ DebtId:"+dr.getDebtId() + "ListingId:"+
        				dr.getListingId() + "Price:" + dr.getPrice()+"~~~~~~~~~~~*");
    		}
    	}		
	}
 }
 