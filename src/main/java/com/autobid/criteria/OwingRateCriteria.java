package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.FormatUtil;
import net.sf.json.JSONObject;

public class OwingRateCriteria implements Criteria, Constants {
    private double owingAmount,owingRate,owingRateConf,owingLimitConf;

    public void calc(JSONObject loanInfos, ConfBean cb) throws Exception {
        owingAmount =  Double.parseDouble(loanInfos.get("OwingAmount").toString());
        double highestPrincipal = Double.parseDouble(FormatUtil.nullToStr(loanInfos.get("HighestPrincipal")));
        owingRate = owingAmount / highestPrincipal;
        owingRateConf = Double.parseDouble(cb.getOwingRate());
        owingLimitConf = Integer.parseInt(cb.getOwingLimit());
    }

    public int getLevel(JSONObject loanInfos, ConfBean cb) throws Exception {

        if(owingAmount==0){
            return PERFECT;
        }else if(owingRate < owingRateConf && owingAmount <= owingLimitConf ) {
            return OK;
        }
        return NONE;
    }

    public String getCriteriaName() { return "OwingRate"; }
}
