package com.autobid.bbd;

import org.junit.Test;

import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;

public class BidServiceTest {
	//private static final int NONE = 0;
	
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
	public void testLoanList() throws Exception {
		int indexNum = 1;
		BidService.loanListService(indexNum);
	}
}
