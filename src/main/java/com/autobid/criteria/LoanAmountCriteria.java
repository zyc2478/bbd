package com.autobid.criteria;

import java.io.IOException;
import java.util.HashMap;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfUtil;

public class LoanAmountCriteria implements Criteria,Constants {

	private boolean criteriaAf,criteriaAm,criteriaBm,criteriaBf,
			criteriaCm,criteriaCf,criteriaD,criteriaEm,criteriaEf;
	private double amount_mrate,amount_frate,owing_mrate,owing_frate;
	private int amount_begin,amount_end,owing_mlimit,owing_flimit,total_limit;
	private int gender;

	public void calc(HashMap<String,Object> loanInfoMap) throws NumberFormatException, IOException {
		Integer loanAmount = (Integer)loanInfoMap.get("Amount") ;
		Integer highestPrincipal = (Integer)loanInfoMap.get("HighestPrincipal");
		Integer owingAmount = (Integer)loanInfoMap.get("OwingAmount");
		Integer highestDebt = (Integer)loanInfoMap.get("HighestDebt");
		Integer totalPrincipal = (Integer)loanInfoMap.get("TotalPrincipal");
		gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
		total_limit = Integer.parseInt(loanInfoMap.get("total_limit").toString());
		owing_mrate = Double.parseDouble(loanInfoMap.get("owing_mrate").toString());
		owing_frate = Double.parseDouble(loanInfoMap.get("owing_frate").toString());
		amount_mrate = Integer.parseInt(ConfUtil.getProperty("amount_mrate"));
		amount_frate = Integer.parseInt(ConfUtil.getProperty("amount_frate"));
		owing_mlimit = Integer.parseInt(ConfUtil.getProperty("owing_mlimit"));
		owing_flimit = Integer.parseInt(ConfUtil.getProperty("owing_flimit"));

		//System.out.println("loanAmount:"+loanAmount);
		criteriaAm = loanAmount >= amount_begin /amount_mrate && loanAmount <= amount_end * amount_mrate &&
					owingAmount >=  owing_mlimit && totalPrincipal >= total_limit && gender == 1;
		criteriaAf = loanAmount >= amount_begin /amount_frate && loanAmount <= amount_end * amount_frate &&
					owingAmount >=  owing_flimit && totalPrincipal >= total_limit && gender == 2;
		criteriaBm = owingAmount + loanAmount < highestDebt * amount_mrate;
		criteriaBf = owingAmount + loanAmount < highestDebt * amount_frate;
				Double.parseDouble(ConfUtil.getProperty("amount_frate"));
		criteriaCm = loanAmount < highestPrincipal * amount_mrate;
		criteriaCf = loanAmount < highestPrincipal * amount_frate;
		criteriaD = owingAmount==0 ;
		criteriaEm = owingAmount < highestDebt * owing_mrate ;
		criteriaEf = owingAmount < highestDebt * owing_frate ;
		/*
		 * 本次借款额度低于1.5W、正常还款5次以上、逾期还清次数（＞30天）最好没有、累计借款金额5K以上、待还金额5K以下、待还金额/历史最高负债越小越好，最好为0等等，
		 */
	}
	public int getLevel(HashMap<String,Object> loanInfoMap) throws NumberFormatException, IOException {
		calc(loanInfoMap);
		if(criteriaAm && criteriaCm && criteriaD && criteriaEm || (criteriaAf && criteriaCf && criteriaD && criteriaEf)) {
			return PERFECT;
		}else if(criteriaAm && criteriaCm && criteriaD || (criteriaAf && criteriaCf && criteriaD)){
			return GOOD;
		}else if(criteriaAm && criteriaBm && criteriaCm || (criteriaAf && criteriaBf && criteriaCf) ){
			return OK;
		}else if(criteriaAm && criteriaBm || (criteriaAf && criteriaBf)) {
			return SOSO;
		}
		return NONE;
	}

	public String getCriteriaName(){
		return "LoanAmount";
	}
}
