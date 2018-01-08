package com.autobid.bbd;

import org.junit.Before;
import org.junit.Test;

import com.autobid.dbd.DebtService;
import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;

@SuppressWarnings("deprecation")
public class DebtServiceTest {

	@Before
	public static void initValue() throws Exception {
		AuthInit.init();

		//���TokenInit��������ڣ����ʼ��Token���洢��Redis��
		if (TokenUtil.determineTokenInitExists()) {
			TokenInit.initToken();
		}
		//���Token�쵽�ڣ����ȡһ����Token
		if (TokenUtil.determineRefreshDate()) {
			TokenUtil.genNewToken();
		}
		//��ȡToken�������ļ��������ȣ�û�����ȡRedis
		//logger.info("token:" + token);
		//String balanceJson = BidService.queryBalanceService(token);
	}
    
	@Test
	public void testDebtList() throws Exception {
		int indexNum = 1;
		DebtService.debtListService(indexNum);
	}
}

