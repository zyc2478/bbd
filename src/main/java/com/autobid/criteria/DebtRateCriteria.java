package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.util.HashMap;

public class DebtRateCriteria implements Criteria, Constants {

    private double debtTotalRate;
    private double debt_mrate;
    private double debt_frate;
    private int gender;
    private int loanAmountLevel;

    @SuppressWarnings("unknown")
    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {

        double totalPrincipal = Double.parseDouble(loanInfoMap.get("TotalPrincipal").toString());
        double owingAmount = Double.parseDouble(loanInfoMap.get("OwingAmount").toString());
        double loanAmount = Double.parseDouble(loanInfoMap.get("Amount").toString());
        gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
        debt_mrate = Double.parseDouble(cb.getDebtMrate());
        debt_frate = Double.parseDouble(cb.getDebtFrate());
        debtTotalRate = totalPrincipal != 0 ? (owingAmount + loanAmount) / totalPrincipal : 1;
        loanAmountLevel = new LoanAmountCriteria().getLevel(loanInfoMap, cb);
/*		System.out.println("debtTotalRate:"+debtTotalRate);
		//System.out.println(debtTotalRate < 0.5 * female_multiple);
		System.out.println(gender == 2 && debtTotalRate < 0.5 * debt_frate);
		//int successCountLevel = successCountCriteria.getLevel(loanInfoMap);
		System.out.println((gender == 1 && debtTotalRate < 0.5 * debt_mrate) || 
				(gender == 2 && debtTotalRate < 0.5 * debt_frate));*/
    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        calc(loanInfoMap, cb);
        if ((gender == 1 && debtTotalRate < 0.15) || (gender == 2 && debtTotalRate < 0.25)) {
            return PERFECT;
        } else if ((gender == 1 && debtTotalRate < 0.25) || (gender == 2 && debtTotalRate < 0.33)) {
            return GOOD;
        } else if ((gender == 1 && debtTotalRate < 0.33) || (gender == 2 && debtTotalRate < 0.5 * debt_frate)) {
            return OK;
        } else if ((gender == 1 && debtTotalRate < 0.5 * debt_mrate && loanAmountLevel > SOSO) ||
                (gender == 2 && debtTotalRate < 0.5 * debt_frate) && loanAmountLevel > SOSO) {
            return OK;
        } else if ((gender == 1 && debtTotalRate < 0.5 * debt_mrate) || (gender == 2 && debtTotalRate < 0.5 * debt_frate)) {
            return SOSO;
        } else {
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "DebtRate";
    }
}
