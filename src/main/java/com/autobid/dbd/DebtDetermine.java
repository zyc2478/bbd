package com.autobid.dbd;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import com.autobid.entity.Constants;
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;

/** 
* @ClassName: BidDetermine 
* @Description: �жϱ�Ľ��ĳ���
* @author Richard Zeng 
* @date 2017��10��13�� ����5:10:43 
*  
*/
public class DebtDetermine implements Constants {
	
	static int DEBT_MIN_PRICE;
	private static Logger logger = Logger.getLogger(DebtDetermine.class); 
	
	static {
		try {
			ConfBean cb = ConfUtil.readAllToBean();
			DEBT_MIN_PRICE = Integer.parseInt(cb.getDebtMinPrice());

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
    //private static Logger logger = Logger.getLogger(BidDetermine.class);  
	
	public static boolean determineBalance(double queryBalance) throws Exception{
	    if(queryBalance > DEBT_MIN_PRICE){
	    	return true;
	    }else{
	    	logger.error("���㣬�����˳�");
	    	Thread.sleep(300000);
	    	return false;
	    }
	}
	
	
	public static boolean determineDuplicateId(int debtId,Jedis jedis){
		if(jedis.exists(String.valueOf(debtId))){
			//System.out.println(listingId + " ��Redis���ظ�!");
			return true;
		}
		return false;
	}
		

}
