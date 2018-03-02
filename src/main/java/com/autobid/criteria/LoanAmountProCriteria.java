package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;


public class LoanAmountProCriteria implements Criteria, Constants {

    public void calc(JSONObject loanInfos, ConfBean cb) {

    }

    public int getLevel(JSONObject loanInfos, ConfBean cb) {
        calc(loanInfos, cb);
/*		System.out.println("LoanAmount: criteriaAm:"+criteriaAm+",criteriaBm:"+criteriaBm + ",criteriaCm:"+criteriaCm+
				",criteriaD:"+criteriaD+",criteriaE:"+criteriaE);*/
/*		System.out.println("criteriaAf:"+criteriaAf+",criteriaBf:"+criteriaBf + ",criteriaCf:"+criteriaCf+
				",criteriaD:"+criteriaD+",criteriaE:"+criteriaE);*/
        int loanAmountLevel = new LoanAmountCriteria().getLevel(loanInfos, cb);
        if(loanAmountLevel==SOSO) return NONE;

        return loanAmountLevel;
    }

    public String getCriteriaName() {
        return "LoanAmountPro";
    }
}
