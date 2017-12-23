package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Criteria;
import com.autobid.entity.Constants;

/** 
* @ClassName: BasicCriteria 
* @Description: 最基础的策略，需要通过该策略才可以投标
* @author Richard Zeng 
* @date 2017年10月13日 下午5:16:24 
*  
*/
public class BasicCriteria extends BidCriteria implements Constants {
	
	static boolean eduBasicCriteria;
	static boolean debtBasicCriteria;
	static boolean beginBasicCriteria;
	
	
	public static void calc(HashMap<String, Object> loanInfoMap) throws Exception {
		int creditCodeLevel = new CreditCodeCriteria().getLevel(loanInfoMap);
		int debtRateLevel = new DebtRateCriteria().getLevel(loanInfoMap);
		int educationLevel = new EducationCriteria().getLevel(loanInfoMap);
		int successCountLevel = new SuccessCountCriteria().getLevel(loanInfoMap);
		int loanAmountLevel = new LoanAmountCriteria().getLevel(loanInfoMap);
		int overdueLevel = new OverdueCriteria().getLevel(loanInfoMap);
		int ageLevel = new AgeCriteria().getLevel(loanInfoMap);
		int lastSuccessBorrowLevel = new LastSuccessBorrowCriteria().getLevel(loanInfoMap);

		
		//学历认证优先的策略集合
		eduBasicCriteria =  educationLevel > NONE 		&& 
							debtRateLevel > NONE 		&&
							creditCodeLevel > NONE		&&
							successCountLevel > SOSO 	&&
							loanAmountLevel > NONE 		&&
							overdueLevel > NONE			&&
							ageLevel > NONE 			&&
							lastSuccessBorrowLevel > NONE;
		//负债策略优先的策略集合
		debtBasicCriteria = debtRateLevel > SOSO		&&
							creditCodeLevel > NONE 		&&
							successCountLevel > SOSO	&&
							loanAmountLevel > NONE	 	&&
							overdueLevel 	> NONE	 	&&
							ageLevel > NONE 			&&
							lastSuccessBorrowLevel > NONE;		
		//信用好的文化菜鸟的策略集合
		beginBasicCriteria = educationLevel > NONE     	&&
							 creditCodeLevel > NONE    	&&
							 successCountLevel > NONE  	&&
							 loanAmountLevel > SOSO 	&&
							 overdueLevel > NONE		&&
							 ageLevel > NONE			&&
							 lastSuccessBorrowLevel > NONE;
		//合规的女借款者策略集合
		
							
		//System.out.println("eduBasicCriteria:"+eduBasicCriteria);
	}
	
	public static int getLevel(HashMap<String, Object> loanInfoMap) throws Exception {
		calc(loanInfoMap);
		//printCriteria(loanInfoMap);
		if(eduBasicCriteria && debtBasicCriteria){
			//System.out.println("PERFECT");
			return PERFECT;
		}else if(eduBasicCriteria){
			//System.out.println("GOOD");
			return GOOD;
		}else if(debtBasicCriteria){
			//System.out.println("OK");
			return OK;
		}else if(beginBasicCriteria) {
			//System.out.println("SOSO");
			return SOSO;
		}
		return NONE;
	}

	public String getCriteriaName() {
		return "Basic";
	}
	
	public void printCriteria(HashMap<String, Object> loanInfoMap) throws Exception {
		int creditCodeLevel = new CreditCodeCriteria().getLevel(loanInfoMap);
		int debtRateLevel = new DebtRateCriteria().getLevel(loanInfoMap);
		int educationLevel = new EducationCriteria().getLevel(loanInfoMap);
		int successCountLevel = new SuccessCountCriteria().getLevel(loanInfoMap);
		int loanAmountLevel = new LoanAmountCriteria().getLevel(loanInfoMap);
		int overdueLevel = new OverdueCriteria().getLevel(loanInfoMap);
		int ageLevel = new AgeCriteria().getLevel(loanInfoMap);
		int lastSuccessBorrowLevel = new LastSuccessBorrowCriteria().getLevel(loanInfoMap);
		System.out.println("creditCodeLevel:"+creditCodeLevel+",debtRateLevel:"+debtRateLevel+
				",educationLevel:"+educationLevel+",successCountLevel:"+successCountLevel+
				",loanAmountLevel:"+loanAmountLevel+",overdueLevel:"+overdueLevel+
				",ageLevel:"+ageLevel+",lastSuccessBorrowLevel:"+lastSuccessBorrowLevel);
	}
}
