package com.autobid.criteria;

import java.io.IOException;
import java.util.HashMap;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfUtil;

public class LoanAmountCriteria implements Criteria,Constants {

	private boolean criteriaA,criteriaAf,criteriaBm,criteriaBf,
			criteriaC,criteriaCm,criteriaCf,criteriaD,criteriaE;
	private double amount_mrate,amount_frate;
	private int gender;

	public void calc(HashMap<String,Object> loanInfoMap) throws NumberFormatException, IOException {
		Integer loanAmount = (Integer)loanInfoMap.get("Amount") ;
		Integer highestPrincipal = (Integer)loanInfoMap.get("HighestPrincipal");
		Integer owingAmount = (Integer)loanInfoMap.get("OwingAmount");
		Integer highestDebt = (Integer)loanInfoMap.get("HighestDebt");
		Integer totalPrincipal = (Integer)loanInfoMap.get("TotalPrincipal");
		gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
		amount_mrate = Double.parseDouble(ConfUtil.getProperty("amount_mrate"));
		amount_frate = Double.parseDouble(ConfUtil.getProperty("amount_frate"));
		//System.out.println("loanAmount:"+loanAmount);
		criteriaA = loanAmount >= Integer.parseInt(ConfUtil.getProperty("amount_begin")) && 
				loanAmount <= Integer.parseInt(ConfUtil.getProperty("amount_end")) && 
				totalPrincipal >= Integer.parseInt(ConfUtil.getProperty("total_limit")) &&
				owingAmount <= Integer.parseInt(ConfUtil.getProperty("owing_limit"));
		criteriaAf = loanAmount >= Integer.parseInt(ConfUtil.getProperty("amount_begin"))/amount_frate && 
				loanAmount <= Integer.parseInt(ConfUtil.getProperty("amount_end")) * amount_frate && 
				totalPrincipal >= Integer.parseInt(ConfUtil.getProperty("total_limit")) / amount_frate &&
				owingAmount <= Integer.parseInt(ConfUtil.getProperty("owing_limit")) * amount_frate;

/*		criteriaB = owingAmount + loanAmount < highestDebt &&  
				owingAmount <= Integer.parseInt(ConfUtil.getProperty("owing_limit"));*/

		criteriaBm = owingAmount + loanAmount < highestDebt * amount_mrate;
		criteriaBf = owingAmount + loanAmount < highestDebt * 
				Double.parseDouble(ConfUtil.getProperty("amount_frate"));
		criteriaC = loanAmount < highestPrincipal;
		criteriaCm = loanAmount < highestPrincipal * amount_mrate;
		criteriaCf = loanAmount < highestPrincipal * amount_frate;
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
		System.out.println("criteriaB:" + criteriaBm);
		System.out.println("criteriaC:" + criteriaC);
		System.out.println("criteriaD:" + criteriaD);*/
		if(criteriaA && criteriaD && criteriaE || (criteriaD && criteriaE && gender ==2)) {
			return PERFECT;
		}else if(criteriaA && criteriaC && criteriaD || (criteriaC && criteriaD && gender == 2)){
			return GOOD;
		}else if(criteriaA && criteriaBm && criteriaCm || (criteriaBf && criteriaCf && gender ==2) ){
			return OK;
		}else if(criteriaA && criteriaBm || (criteriaAf && criteriaBf && gender == 2)) {
			return SOSO;
		}
		return NONE;
	}

	public String getCriteriaName(){
		return "LoanAmount";
	}
}
