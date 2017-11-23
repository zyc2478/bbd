package com.autobid.criteria;

import java.io.IOException;
import java.util.HashMap;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfUtil;

public class LoanAmountProCriteria implements Criteria,Constants {

	private boolean criteriaA,criteriaB,criteriaC,criteriaD,criteriaE;
	private int gender;

	public void calc(HashMap<String,Object> loanInfoMap) throws NumberFormatException, IOException {
		Integer loanAmount = (Integer)loanInfoMap.get("Amount") ;
		Integer highestPrincipal = (Integer)loanInfoMap.get("HighestPrincipal");
		Integer owingAmount = (Integer)loanInfoMap.get("OwingAmount");
		Integer highestDebt = (Integer)loanInfoMap.get("HighestDebt");
		Integer totalPrincipal = (Integer)loanInfoMap.get("TotalPrincipal");
		gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
		//System.out.println("loanAmount:"+loanAmount);
		criteriaA = loanAmount >= Integer.parseInt(ConfUtil.getProperty("amount_begin")) && 
				loanAmount <= Integer.parseInt(ConfUtil.getProperty("amount_end")) && 
				totalPrincipal >= Integer.parseInt(ConfUtil.getProperty("total_limit"));
		criteriaB = owingAmount + loanAmount < highestDebt && 
				owingAmount <= Integer.parseInt(ConfUtil.getProperty("owing_limit"));
		criteriaC = loanAmount < highestPrincipal;
		criteriaD = owingAmount==0 ;
		criteriaE = owingAmount < highestDebt/2 && loanAmount < highestDebt/2;
		//criteraD = loanAmount > 2000;
		/*
		 * 本次借款额度低于1.5W、正常还款5次以上、逾期还清次数（＞30天）最好没有、累计借款金额5K以上、待还金额5K以下、待还金额/历史最高负债越小越好，最好为0等等，
		 */
	}

	public int getLevel(HashMap<String,Object> loanInfoMap) throws NumberFormatException, IOException {
		calc(loanInfoMap);
/*		System.out.println("criteriaA:" + criteriaA);
		System.out.println("criteriaB:" + criteriaB);
		System.out.println("criteriaC:" + criteriaC);
		System.out.println("criteriaD:" + criteriaD);*/
		if(criteriaA && criteriaD && criteriaE || (criteriaD && criteriaE && gender ==2)) {
			return PERFECT;
		}else if(criteriaA && criteriaC && criteriaD || (criteriaC && criteriaD && gender == 2)){
			return GOOD;
		}else if(criteriaA && criteriaB && criteriaC || (criteriaB && criteriaC && gender ==2) ){
			return OK;
		}else {
			return NONE;
		}
	}

	public String getCriteriaName(){
		return "LoanAmountPro";
	}
}
