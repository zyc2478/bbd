package com.autobid.bbd;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.autobid.dbd.DebtService;
import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;


public class DebtServiceTest {

	@Before
	public static void initValue() throws Exception {
		AuthInit.init();

		//如果TokenInit配置项不存在，则初始化Token，存储在Redis中
		if (TokenUtil.determineTokenInitExists()) {
			TokenInit.initToken();
		}
		//如果Token快到期，则获取一个新Token
		if (TokenUtil.determineRefreshDate()) {
			TokenUtil.genNewToken();
		}
		//获取Token，配置文件有则优先，没有则获取Redis
		//logger.info("token:" + token);
		//String balanceJson = BidService.queryBalanceService(token);
	}
    
	@Test
	public void testDebtList() throws Exception {
		int indexNum = 1;
		DebtService.debtListService(indexNum);
	}

	@Test
	public void testBatchDebtInfos()  {
		ArrayList<Integer> debtInfos = new ArrayList<>();
		debtInfos.add(45393752);
		//DebtService.BatchDebtInfosService(debtInfos);
	}
}

