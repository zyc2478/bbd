package com.autobid.dbd;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import com.autobid.entity.Constants;
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;

/** 
* @ClassName: BidDetermine 
* @Description: 判断标的金额的程序
* @author Richard Zeng 
* @date 2017年10月13日 下午5:10:43 
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
	    	logger.error("余额不足，程序退出");
	    	Thread.sleep(300000);
	    	return false;
	    }
	}
	
	
	public static boolean determineDuplicateId(int debtId,Jedis jedis){
		if(jedis.exists(String.valueOf(debtId))){
			//System.out.println(listingId + " 在Redis中重复!");
			return true;
		}
		return false;
	}
		

}
