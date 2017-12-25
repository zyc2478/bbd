package com.autobid.bbd;

import org.junit.Test;

import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;

public class BidServiceTest {
	//private static final int NONE = 0;
	
	static{
		try{
			AuthInit.init();
    		
			//如果TokenInit配置项不存在，则初始化Token，存储在Redis中
			if(!TokenUtil.determineTokenInitExsits()) {
				TokenInit.initToken();
			}
			//如果Token快到期，则获取一个新Token		
			if(TokenUtil.determineRefreshDate()) {
				TokenUtil.genNewToken();
			}
			//获取Token，配置文件有则优先，没有则获取Redis
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
