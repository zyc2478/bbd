package com.autobid.bbd;

import org.junit.Test;

import com.autobid.dbd.DebtService;
import com.autobid.util.ConfUtil;
import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;

import redis.clients.jedis.Jedis;

public class BidServiceTest {
	//private static final int NONE = 0;
	private static int MIN_BID_AMOUNT;
	
	private static String token = "";
    private static String openId;
    private static String redisHost;
    private static int redisPort;
	private static Jedis jedis;
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
	public void testLoanList() throws Exception {
		int indexNum = 1;
		BidService.loanListService(indexNum);
	}
}
