package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.entity.Constants;

/** 
* @ClassName: BasicCriteria 
* @Description: ������Ĳ��ԣ���Ҫͨ���ò��Բſ���Ͷ��
* @author Richard Zeng 
* @date 2017��10��13�� ����5:16:24 
*  
*/
public class BasicCriteria implements Criteria,Constants {
	
	boolean eduBasicCriteria;
	boolean debtBasicCriteria;
	boolean beginBasicCriteria;
	CreditCodeCriteria creditCodeCriteria = new CreditCodeCriteria();
	DebtRateCriteria debtRateCriteria = new DebtRateCriteria();
	EducationCriteria educationCriteria = new EducationCriteria();
	SuccessCountCriteria successCountCriteria = new SuccessCountCriteria();
	LoanAmountCriteria loanAmountCriteria = new LoanAmountCriteria();
	OverdueCriteria overdueCriteria = new OverdueCriteria();
	AgeCriteria ageCriteria = new AgeCriteria();
	LastSuccessBorrowCriteria lastSuccessBorrowCriteria = new LastSuccessBorrowCriteria();
	
	public void calc(HashMap<String, Object> loanInfoMap,ConfBean cb) throws Exception {
		
		int creditCodeLevel = creditCodeCriteria.getLevel(loanInfoMap,cb);
		int debtRateLevel = debtRateCriteria.getLevel(loanInfoMap,cb);
		int educationLevel = educationCriteria.getLevel(loanInfoMap,cb);
		int successCountLevel = successCountCriteria.getLevel(loanInfoMap,cb);
		int loanAmountLevel = loanAmountCriteria.getLevel(loanInfoMap,cb);
		int overdueLevel = overdueCriteria.getLevel(loanInfoMap,cb);
		int ageLevel = ageCriteria.getLevel(loanInfoMap,cb);
		int lastSuccessBorrowLevel = lastSuccessBorrowCriteria.getLevel(loanInfoMap,cb);

		
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
							 
		//System.out.println("eduBasicCriteria:"+eduBasicCriteria);
	}
	
	public int getLevel(HashMap<String, Object> loanInfoMap,ConfBean cb) throws Exception {
		calc(loanInfoMap,cb);
		//printCriteria(loanInfoMap,cb);
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
	
	public void printCriteria(HashMap<String, Object> loanInfoMap,ConfBean cb) throws Exception {
		int creditCodeLevel = creditCodeCriteria.getLevel(loanInfoMap,cb);
		int debtRateLevel = debtRateCriteria.getLevel(loanInfoMap,cb);
		int educationLevel = educationCriteria.getLevel(loanInfoMap,cb);
		int successCountLevel = successCountCriteria.getLevel(loanInfoMap,cb);
		int loanAmountLevel = loanAmountCriteria.getLevel(loanInfoMap,cb);
		int overdueLevel = overdueCriteria.getLevel(loanInfoMap,cb);
		int ageLevel = ageCriteria.getLevel(loanInfoMap,cb);
		int lastSuccessBorrowLevel = lastSuccessBorrowCriteria.getLevel(loanInfoMap,cb);
		System.out.println("creditCodeLevel:"+creditCodeLevel+",debtRateLevel:"+debtRateLevel+
				",educationLevel:"+educationLevel+",successCountLevel:"+successCountLevel+
				",loanAmountLevel:"+loanAmountLevel+",overdueLevel:"+overdueLevel+
				",ageLevel:"+ageLevel+",lastSuccessBorrowLevel:"+lastSuccessBorrowLevel);
	}
}
