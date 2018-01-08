package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class DebtRateProCriteria implements Criteria, Constants {

    private double debtTotalRate;
    private double gender;

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        double totalPrincipal = Double.parseDouble(loanInfoMap.get("TotalPrincipal").toString());
        double owingAmount = Double.parseDouble(loanInfoMap.get("OwingAmount").toString());
        double loanAmount = Double.parseDouble(loanInfoMap.get("Amount").toString());
        gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
        debtTotalRate = totalPrincipal != 0 ? (owingAmount + loanAmount) / totalPrincipal : 1;
    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        calc(loanInfoMap, cb);
        if (debtTotalRate < 0.15 || (gender == 2 && debtTotalRate < 0.25)) {
            return PERFECT;
        } else if (debtTotalRate < 0.25 || (gender == 2 && debtTotalRate < 0.33)) {
            return GOOD;
        } else if (debtTotalRate < 0.33 || (gender == 2 && debtTotalRate < 0.5)) {
            return OK;
        } else {
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "DebtRatePro";
    }
}
