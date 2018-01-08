package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.util.HashMap;


public class LoanAmountProCriteria implements Criteria, Constants {

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {

    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        calc(loanInfoMap, cb);
/*		System.out.println("LoanAmount: criteriaAm:"+criteriaAm+",criteriaBm:"+criteriaBm + ",criteriaCm:"+criteriaCm+
				",criteriaD:"+criteriaD+",criteriaE:"+criteriaE);*/
/*		System.out.println("criteriaAf:"+criteriaAf+",criteriaBf:"+criteriaBf + ",criteriaCf:"+criteriaCf+
				",criteriaD:"+criteriaD+",criteriaE:"+criteriaE);*/
        int loanAmountLevel = new LoanAmountCriteria().getLevel(loanInfoMap, cb);
        if(loanAmountLevel==SOSO) return NONE;

        return loanAmountLevel;
    }

    public String getCriteriaName() {
        return "LoanAmountPro";
    }
}
