package com.autobid.criteria;

import java.io.IOException;
import java.util.HashMap;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfUtil;

public class LoanAmountProCriteria implements Criteria,Constants {

	private boolean criteriaAf,criteriaAm,criteriaBm,criteriaBf,criteriaCm,criteriaCf,criteriaD,criteriaE;
	private double amount_mrate,amount_frate,owing_mrate,owing_frate;
	private int amount_begin,amount_end,owing_limit,total_limit;
	private int gender;

	public void calc(HashMap<String,Object> loanInfoMap) throws NumberFormatException, IOException {
		Integer loanAmount = (Integer)loanInfoMap.get("Amount") ;
		Integer highestPrincipal = (Integer)loanInfoMap.get("HighestPrincipal");
		Integer owingAmount = (Integer)loanInfoMap.get("OwingAmount");
		Integer highestDebt = (Integer)loanInfoMap.get("HighestDebt");
		Integer totalPrincipal = (Integer)loanInfoMap.get("TotalPrincipal");
		gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
		total_limit = Integer.parseInt(ConfUtil.getProperty("total_limit"));
		owing_mrate = Double.parseDouble(ConfUtil.getProperty("owing_mrate"));
		owing_frate = Double.parseDouble(ConfUtil.getProperty("owing_frate"));
		amount_begin = Integer.parseInt(ConfUtil.getProperty("amount_begin"));
		amount_end = Integer.parseInt(ConfUtil.getProperty("amount_end"));
		amount_mrate = Double.parseDouble(ConfUtil.getProperty("amount_mrate"));
		amount_frate = Double.parseDouble(ConfUtil.getProperty("amount_frate"));
		owing_limit = Integer.parseInt(ConfUtil.getProperty("owing_limit"));

		//System.out.println("loanAmount:"+loanAmount);
		criteriaAm = loanAmount >= amount_begin /amount_mrate && loanAmount <= amount_end * amount_mrate &&
					totalPrincipal >= total_limit && owingAmount < highestDebt * owing_mrate && gender == 1;
		criteriaAf = loanAmount >= amount_begin /amount_frate && loanAmount <= amount_end * amount_frate &&
					totalPrincipal >= total_limit && owingAmount < highestDebt * owing_frate && gender == 2;
/*		System.out.println("amount_begin /amount_frate:"+ (double)((double)amount_begin /(double)amount_frate) + ",amount_end * amount_frate:"+
				amount_end * amount_frate);*/
		criteriaBm = owingAmount + loanAmount < highestDebt * amount_mrate && gender == 1;
		criteriaBf = owingAmount + loanAmount < highestDebt * amount_frate && gender == 2;
		//System.out.println("highestPrincipal:"+highestPrincipal+",highestPrincipal * amount_mrate：" + highestPrincipal * amount_mrate);
		criteriaCm = loanAmount < highestPrincipal * amount_mrate && gender == 1;
		criteriaCf = loanAmount < highestPrincipal * amount_frate && gender == 2;
		criteriaD = owingAmount <=  owing_limit;
		criteriaE = owingAmount==0 ;
		
		/*
		 * 本次借款额度低于1.5W、正常还款5次以上、逾期还清次数（＞30天）最好没有、累计借款金额5K以上、待还金额5K以下、待还金额/历史最高负债越小越好，最好为0等等，
		 */
	}
	public int getLevel(HashMap<String,Object> loanInfoMap) throws NumberFormatException, IOException {
		calc(loanInfoMap);
/*		System.out.println("LoanAmount: criteriaAm:"+criteriaAm+",criteriaBm:"+criteriaBm + ",criteriaCm:"+criteriaCm+
				",criteriaD:"+criteriaD+",criteriaE:"+criteriaE);*/
/*		System.out.println("criteriaAf:"+criteriaAf+",criteriaBf:"+criteriaBf + ",criteriaCf:"+criteriaCf+
				",criteriaD:"+criteriaD+",criteriaE:"+criteriaE);*/
		if(criteriaAm && criteriaBm && criteriaCm && criteriaE || (criteriaAf && criteriaBf && criteriaCf && criteriaE)) {
			return PERFECT;
		}else if(criteriaAm && criteriaBm && criteriaCm && criteriaD || (criteriaAf && criteriaBf && criteriaCf && criteriaD)){
			return GOOD;
		}else if(criteriaAm && criteriaBm && criteriaCm || (criteriaAf && criteriaBf && criteriaCf) ){
			return OK;
		}
		return NONE;
	}

	public String getCriteriaName(){
		return "LoanAmountPro";
	}
}
