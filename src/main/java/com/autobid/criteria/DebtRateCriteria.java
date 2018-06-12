package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.FormatUtil;
import net.sf.json.JSONObject;

public class DebtRateCriteria implements Criteria, Constants {

    private double debtTotalRate;
    private double debt_mrate;
    private double debt_frate;
    private int gender;
    private int loanAmountLevel;

    @SuppressWarnings("deprecation")
    public void calc(JSONObject loanInfos, ConfBean cb) {

        double totalPrincipal = Double.parseDouble(FormatUtil.nullToStr(loanInfos.get("TotalPrincipal")));
        double owingAmount = Double.parseDouble(loanInfos.get("OwingAmount").toString());
        double loanAmount = Double.parseDouble(loanInfos.get("Amount").toString());
        gender = Integer.parseInt(loanInfos.get("Gender").toString());
        debt_mrate = Double.parseDouble(cb.getDebtMrate());
        debt_frate = Double.parseDouble(cb.getDebtFrate());
        debtTotalRate = totalPrincipal != 0 ? (owingAmount + loanAmount) / totalPrincipal : 1;
//		System.out.println("debtTotalRate:"+debtTotalRate);
        //System.out.println(debtTotalRate < 0.5 * female_multiple);
        /*		System.out.println(gender == 2 && debtTotalRate < 0.5 * debt_frate);*/
        //int successCountLevel = successCountCriteria.getLevel(loanInfoMap);
/*		System.out.println((gender == 1 && debtTotalRate < 0.5 * debt_mrate) ||
				(gender == 2 && debtTotalRate < 0.5 * debt_frate));*/
    }

    public int getLevel(JSONObject loanInfos, ConfBean cb) {
        calc(loanInfos, cb);
        if (gender == 2) {                            //针对女性的策略，如果比要求的比率还低一倍，加投
            if (debtTotalRate < debt_frate/2 ) {
                return PERFECT;
            } else if (debtTotalRate < debt_frate)
                return OK;
        }else if(gender ==1 && debtTotalRate < debt_mrate){    //针对男性的策略，没有加投考虑
                return OK;
        }
        return NONE;
    }

/*    public int getLevel(JSONObject loanInfos, ConfBean cb) {
        calc(loanInfos, cb);
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
    }*/
    public String getCriteriaName() {
        return "DebtRate";
    }
}
