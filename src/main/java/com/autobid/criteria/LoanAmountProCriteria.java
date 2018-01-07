package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.util.HashMap;

public class LoanAmountProCriteria implements Criteria, Constants {

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        Integer loanAmount = (Integer) loanInfoMap.get("Amount");
        Integer highestPrincipal = (Integer) loanInfoMap.get("HighestPrincipal");
        Integer owingAmount = (Integer) loanInfoMap.get("OwingAmount");
        Integer highestDebt = (Integer) loanInfoMap.get("HighestDebt");
        Integer totalPrincipal = (Integer) loanInfoMap.get("TotalPrincipal");
        int gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
        int total_limit = Integer.parseInt(cb.getTotalLimit());
        double owing_mrate = Double.parseDouble(cb.getOwingMrate());
        double owing_frate = Double.parseDouble(cb.getOwingFrate());
        int amount_begin = Integer.parseInt(cb.getAmountBegin());
        int amount_end = Integer.parseInt(cb.getAmountEnd());
        double amount_mrate = Double.parseDouble(cb.getAmountMrate());
        double amount_frate = Double.parseDouble(cb.getAmountFrate());
        int owing_limit = Integer.parseInt(cb.getOwingLimit());

        //System.out.println("loanAmount:"+loanAmount);
        boolean criteriaAm = loanAmount >= amount_begin / amount_mrate && loanAmount <= amount_end * amount_mrate &&
                totalPrincipal >= total_limit && owingAmount < highestDebt * owing_mrate && gender == 1;
        boolean criteriaAf = loanAmount >= amount_begin / amount_frate && loanAmount <= amount_end * amount_frate &&
                totalPrincipal >= total_limit && owingAmount < highestDebt * owing_frate && gender == 2;
/*		System.out.println("amount_begin /amount_frate:"+ (double)((double)amount_begin /(double)amount_frate) + ",amount_end * amount_frate:"+
				amount_end * amount_frate);*/
        boolean criteriaBm = owingAmount + loanAmount < highestDebt * amount_mrate && gender == 1;
        boolean criteriaBf = owingAmount + loanAmount < highestDebt * amount_frate && gender == 2;
        //System.out.println("highestPrincipal:"+highestPrincipal+",highestPrincipal * amount_mrate��" + highestPrincipal * amount_mrate);
        boolean criteriaCm = loanAmount < highestPrincipal * amount_mrate && gender == 1;
        boolean criteriaCf = loanAmount < highestPrincipal * amount_frate && gender == 2;
        boolean criteriaD = owingAmount <= owing_limit;
        boolean criteriaE = owingAmount == 0;

        /*
         * ���ν���ȵ���1.5W����������5�����ϡ����ڻ����������30�죩���û�С��ۼƽ����5K���ϡ��������5K���¡��������/��ʷ��߸�ծԽСԽ�ã����Ϊ0�ȵȣ�
         */
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
