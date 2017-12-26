package com.autobid.bbd;

import java.util.ArrayList;
import org.junit.Test;

import com.autobid.dbd.DebtService;
import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;


public class DebtServiceTest {
	
	static{
		try{
			AuthInit.init();
    		
			
			//���TokenInit��������ڣ����ʼ��Token���洢��Redis��
			if(!TokenUtil.determineTokenInitExsits()) {
				TokenInit.initToken();
			}
			//���Token�쵽�ڣ����ȡһ����Token		
			if(TokenUtil.determineRefreshDate()) {
				TokenUtil.genNewToken();
			}
			//��ȡToken�������ļ��������ȣ�û�����ȡRedis
			//logger.info("token:" + token);
			//String balanceJson = BidService.queryBalanceService(token); 

		}catch(Exception e){
			e.printStackTrace();
		}
	}
    
	@Test
	public void testDebtList() throws Exception {
		int indexNum = 1;
		DebtService.debtListService(indexNum);
	}

	@Test
	public void testBatchDebtInfos() throws Exception {
		ArrayList<Integer> debtInfos = new ArrayList<Integer>();
		debtInfos.add(45393752);
		//DebtService.BatchDebtInfosService(debtInfos);
	}
}
