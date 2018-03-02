package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class LoanAmountCriteria implements Criteria, Constants {

    private boolean criteriaAf, criteriaAm, criteriaBm, criteriaBf, criteriaCm, criteriaCf, criteriaD, criteriaE;

    public void calc(JSONObject loanInfos, ConfBean cb) {

        double loanAmount = Double.parseDouble(loanInfos.get("Amount").toString());
        double highestPrincipal = Double.parseDouble(loanInfos.get("HighestPrincipal").toString());
        double owingAmount =  Double.parseDouble(loanInfos.get("OwingAmount").toString());
        double highestDebt = Double.parseDouble(loanInfos.get("HighestDebt").toString());
        double totalPrincipal = Double.parseDouble(loanInfos.get("TotalPrincipal").toString());
        int gender = Integer.parseInt(loanInfos.get("Gender").toString());
        int total_limit = Integer.parseInt(cb.getTotalLimit());
        double owing_mrate = Double.parseDouble(cb.getOwingMrate());
        double owing_frate = Double.parseDouble(cb.getOwingFrate());
        int amount_begin = Integer.parseInt(cb.getAmountBegin());
        int amount_end = Integer.parseInt(cb.getAmountEnd());
        double amount_mrate = Double.parseDouble(cb.getAmountMrate());
        double amount_frate = Double.parseDouble(cb.getAmountFrate());
        int owing_limit = Integer.parseInt(cb.getOwingLimit());

        //System.out.println("loanAmount:"+loanAmount);
        criteriaAm = loanAmount >= amount_begin / amount_mrate && loanAmount <= amount_end * amount_mrate &&
                totalPrincipal >= total_limit && owingAmount < highestDebt * owing_mrate && gender == 1;
        criteriaAf = loanAmount >= amount_begin / amount_frate && loanAmount <= amount_end * amount_frate &&
                totalPrincipal >= total_limit && owingAmount < highestDebt * owing_frate && gender == 2;
/*		System.out.println("amount_begin /amount_frate:"+ (double)((double)amount_begin /(double)amount_frate) + ",amount_end * amount_frate:"+
				amount_end * amount_frate);*/
        criteriaBm = owingAmount + loanAmount < highestDebt * amount_mrate && gender == 1;
        criteriaBf = owingAmount + loanAmount < highestDebt * amount_frate && gender == 2;
        //System.out.println("highestPrincipal:"+highestPrincipal+",highestPrincipal * amount_mrate：" + highestPrincipal * amount_mrate);
        criteriaCm = loanAmount < highestPrincipal * amount_mrate && gender == 1;
        criteriaCf = loanAmount < highestPrincipal * amount_frate && gender == 2;
        criteriaD = owingAmount <= owing_limit;
        criteriaE = owingAmount == 0;

        /*
         * 本次借款额度低于1.5W、正常还款5次以上、逾期还清次数（＞30天）最好没有、累计借款金额5K以上、待还金额5K以下、待还金额/历史最高负债越小越好，最好为0等等，
         */
    }

    public int getLevel(JSONObject loanInfos, ConfBean cb) {
        calc(loanInfos, cb);
/*		System.out.println("LoanAmount: criteriaAm:"+criteriaAm+",criteriaBm:"+criteriaBm + ",criteriaCm:"+criteriaCm+
				",criteriaD:"+criteriaD+",criteriaE:"+criteriaE);*/
/*		System.out.println("criteriaAf:"+criteriaAf+",criteriaBf:"+criteriaBf + ",criteriaCf:"+criteriaCf+
				",criteriaD:"+criteriaD+",criteriaE:"+criteriaE);*/
        if (criteriaAm && criteriaBm && criteriaCm && criteriaE || (criteriaAf && criteriaBf && criteriaCf && criteriaE)) {
            return PERFECT;
        } else if (criteriaAm && criteriaBm && criteriaCm && criteriaD || (criteriaAf && criteriaBf && criteriaCf && criteriaD)) {
            return GOOD;
        } else if (criteriaAm && criteriaBm && criteriaCm || (criteriaAf && criteriaBf && criteriaCf)) {
            return OK;
        } else if (criteriaAm && criteriaBm || (criteriaAf && criteriaBf)) {
            return SOSO;
        }
        return NONE;
    }

    public String getCriteriaName() {
        return "LoanAmount";
    }
}
