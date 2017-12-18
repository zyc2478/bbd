package com.autobid.dbd;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//import org.apache.log4j.Logger;



import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.entity.CriteriaBid;
import com.autobid.entity.CriteriaGroup;
import com.autobid.util.ConfUtil;

/** 
* @ClassName: BidDetermine 
* @Description: 判断标的金额的程序
* @author Richard Zeng 
* @date 2017年10月13日 下午5:10:43 
*  
*/
public class DebtDetermine implements Constants {
	
	static int MIN_BID_AMOUNT,BID_LEVEL_AMOUNT,MULTIPLE,MORE_MULTIPLE;
	static float MINI_MULTIPLE,MEDIUM_MULTIPLE;
	private static Logger logger = Logger.getLogger(DebtDetermine.class); 
	
	static {
		try {
			MIN_BID_AMOUNT = Integer.parseInt(ConfUtil.getProperty("min_bid_amount"));
			BID_LEVEL_AMOUNT = Integer.parseInt(ConfUtil.getProperty("bid_level_amount"));
			MORE_MULTIPLE = Integer.parseInt(ConfUtil.getProperty("more_multiple"));
			MINI_MULTIPLE = Float.parseFloat(ConfUtil.getProperty("mini_multiple"));
			MEDIUM_MULTIPLE = Float.parseFloat(ConfUtil.getProperty("medium_multiple"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
    //private static Logger logger = Logger.getLogger(BidDetermine.class);  
	
	public static boolean determineBalance(double queryBalance) throws Exception{
	    if(queryBalance > MIN_BID_AMOUNT){
	    	return true;
	    }else{
	    	logger.error("余额不足，程序退出");
	    	Thread.sleep(300000);
	    	return false;
	    }
	}
	
	public static boolean determineDuplicateId(int listingId,Jedis jedis){
		if(jedis.exists(String.valueOf(listingId))){
			//System.out.println(listingId + " 在Redis中重复!");
			return true;
		}
		return false;
	}
		
	public static int determineLoanAmountPro(int loanAmountLevelPro) throws Exception{
		switch(loanAmountLevelPro){
			case PERFECT: return (int)(BID_LEVEL_AMOUNT * MEDIUM_MULTIPLE);
			case GOOD: 	return BID_LEVEL_AMOUNT * MULTIPLE;
			case OK: 	return BID_LEVEL_AMOUNT;
			default:		return NONE;
		}
	}
	
	public static int determineCertificate(int certificateLevel) throws Exception{
		switch(certificateLevel){
			case PERFECT: return BID_LEVEL_AMOUNT * MULTIPLE;
			case GOOD: 	return (int)(BID_LEVEL_AMOUNT * MEDIUM_MULTIPLE);
			case OK: 	return BID_LEVEL_AMOUNT;
			default:	return NONE;
		}
	}
	
	public static int determineSuccessCount(int successCountLevel) throws Exception{
		switch(successCountLevel){
			case GOOD: 	return (int) (BID_LEVEL_AMOUNT * MINI_MULTIPLE);
			case OK: 	return BID_LEVEL_AMOUNT;
			default:	return NONE;
		}
	}
	
	public static int determineOverduePro(int overdueLevelPro) throws Exception{
		switch(overdueLevelPro){
			case GOOD: 	return BID_LEVEL_AMOUNT;
			default:	return NONE;
		}
	}

	public static int determineDebtRate(int debtRateLevel) throws Exception{
		switch(debtRateLevel){
			case PERFECT: 	return BID_LEVEL_AMOUNT * MULTIPLE;
			case GOOD: 		return (int)(BID_LEVEL_AMOUNT * MEDIUM_MULTIPLE);
			case OK:		return BID_LEVEL_AMOUNT;
			default:	return NONE;
		}
	}
	
	public static int determineDebtRatePro(int debtRateProLevel) throws Exception{
		switch(debtRateProLevel){
			case PERFECT: 	return (int)(BID_LEVEL_AMOUNT * MEDIUM_MULTIPLE);
			case GOOD: 		return BID_LEVEL_AMOUNT;
			default:	return NONE;
		}
	}
		
	public static int determineEducation(int educationLevel) throws Exception{
		switch(educationLevel){
			case PERFECT: 	return BID_LEVEL_AMOUNT * MORE_MULTIPLE;
			case GOOD: 		return BID_LEVEL_AMOUNT * MULTIPLE;
			case OK:		return BID_LEVEL_AMOUNT;
			default:	return NONE;
		}
	}
	public static int determineEducationPro(int educationProLevel) throws Exception{
		switch(educationProLevel){
			case PERFECT: 	return BID_LEVEL_AMOUNT * MULTIPLE;
			case GOOD: 		return BID_LEVEL_AMOUNT;
			default:	return NONE;
		}
	}

	public static int determineBeginPro(int educationProLevel) throws Exception{
		switch(educationProLevel){
			case PERFECT: 	return BID_LEVEL_AMOUNT * MORE_MULTIPLE;
			case GOOD: 		return BID_LEVEL_AMOUNT * MULTIPLE;
			case OK:		return BID_LEVEL_AMOUNT;
			default:	return NONE;
		}
	}
	public static int determineBasic(int basicLevel) throws Exception{
		switch(basicLevel){
			case PERFECT: 	return MIN_BID_AMOUNT + BID_LEVEL_AMOUNT * MULTIPLE;
			case GOOD: 		return MIN_BID_AMOUNT + BID_LEVEL_AMOUNT;
			case OK: 		return MIN_BID_AMOUNT;
			case SOSO:		return MIN_BID_AMOUNT;
			default:		return NONE;
		}
	}
	
	public static int determineEduDebtPro(int eduDebtProLevel) throws Exception{
		switch(eduDebtProLevel){
			case PERFECT: 	return BID_LEVEL_AMOUNT * MORE_MULTIPLE;
			case GOOD: 		return BID_LEVEL_AMOUNT * MULTIPLE;
			case OK:		return (int)(BID_LEVEL_AMOUNT * MEDIUM_MULTIPLE);
			default:	return NONE;
		}
	}
	
	public static int determineLastSuccessBorrow(int lastSuccessBorrowLevel) throws Exception{
		switch(lastSuccessBorrowLevel){
			case GOOD: 		return BID_LEVEL_AMOUNT;
			case OK:		return NONE;
			default:		return NONE;
		}
	}
	
	public int determineCriteriaGroup(CriteriaGroup criteriaGroup,HashMap<String,Object> loanInfoMap) 
			throws Exception{
		//System.out.println("eduCriteriaGroup's size " + eduCriteriaGroup.getCriteriaList().size());
		int totalAmount = 0;
		//System.out.println("criteriaGroup is : " + criteriaGroup.getCriteriaList());
		ArrayList<Criteria> criteriaList = criteriaGroup.getCriteriaList();
		Iterator<Criteria> criteriaIt = criteriaList.iterator();
		while(criteriaIt.hasNext()){
			Criteria c = criteriaIt.next();
			String criteriaName = c.getCriteriaName();
			String methodName =  "determine" + criteriaName;
			int criteriaLevel = c.getLevel(loanInfoMap);

			//注意，static方法不能使用this.getClass(),getDeclaredMethod(name, parameterTypes)
			//int本身不是对象，只能用如下方法调用 new Class[]{int.class}
			Method method = this.getClass().getDeclaredMethod(methodName,new Class[]{int.class});
			int amount = Integer.parseInt(method.invoke(this.getClass(),criteriaLevel).toString());
			CriteriaBid criteriaBid = new CriteriaBid();
			criteriaBid.setAmount(amount);
			criteriaBid.setCriteriaName(criteriaName);
			criteriaBid.setLevel(criteriaLevel);
			//logger.info(criteriaBid.print());
/*			if(criteriaLevel>0) {
				System.out.println(c.getCriteriaName() + "："+ c.getLevel(loanInfoMap)+
						"; amount="+amount);
			}*/
			totalAmount += amount;
			
		}
		return totalAmount;
	}
}
