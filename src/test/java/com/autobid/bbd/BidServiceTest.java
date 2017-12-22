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
	public void testLoanList() throws Exception {
		int indexNum = 1;
		BidService.loanListService(indexNum);
	}
}
