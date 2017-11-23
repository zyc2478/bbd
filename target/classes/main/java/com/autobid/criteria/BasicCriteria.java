package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Criteria;
import com.autobid.entity.Constants;

/** 
* @ClassName: BasicCriteria 
* @Description: ������Ĳ��ԣ���Ҫͨ���ò��Բſ���Ͷ��
* @author Richard Zeng 
* @date 2017��10��13�� ����5:16:24 
*  
*/
public class BasicCriteria implements Criteria,Constants {
	
	boolean eduBasicCriteria,debtBasicCriteria,beginBasicCriteria;
	
	
	public void calc(HashMap<String, Object> loanInfoMap) throws Exception {
		int creditCodeLevel = new CreditCodeCriteria().getLevel(loanInfoMap);
		int debtRateLevel = new DebtRateCriteria().getLevel(loanInfoMap);
		int educationLevel = new EducationCriteria().getLevel(loanInfoMap);
		int successCountLevel = new SuccessCountCriteria().getLevel(loanInfoMap);
		int loanAmountLevel = new LoanAmountCriteria().getLevel(loanInfoMap);
		int overdueLevel = new OverdueCriteria().getLevel(loanInfoMap);
		int ageLevel = new AgeCriteria().getLevel(loanInfoMap);
		int lastSuccessBorrowLevel = new LastSuccessBorrowCriteria().getLevel(loanInfoMap);

		
		//ѧ����֤���ȵĲ��Լ���
		eduBasicCriteria =  educationLevel > NONE 		&& 
							debtRateLevel > NONE 		&&
							creditCodeLevel > NONE		&&
							successCountLevel > SOSO 	&&
							loanAmountLevel > NONE 		&&
							overdueLevel > NONE			&&
							ageLevel > NONE 			&&
							lastSuccessBorrowLevel > NONE;
		//��ծ�������ȵĲ��Լ���
		debtBasicCriteria = debtRateLevel > SOSO		&&
							creditCodeLevel > NONE 		&&
							successCountLevel > SOSO	&&
							loanAmountLevel > NONE	 	&&
							overdueLevel 	> NONE	 	&&
							ageLevel > NONE 			&&
							lastSuccessBorrowLevel > NONE;		
		//���úõ��Ļ�����Ĳ��Լ���
		beginBasicCriteria = educationLevel > NONE     	&&
							 creditCodeLevel > NONE    	&&
							 successCountLevel > NONE  	&&
							 loanAmountLevel > SOSO 	&&
							 overdueLevel > NONE		&&
							 ageLevel > NONE			&&
							 lastSuccessBorrowLevel > NONE;
		//�Ϲ��Ů����߲��Լ���
		
							
		//System.out.println("eduBasicCriteria:"+eduBasicCriteria);
	}
	
	public int getLevel(HashMap<String, Object> loanInfoMap) throws Exception {
		calc(loanInfoMap);
		printCriteria(loanInfoMap);
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
