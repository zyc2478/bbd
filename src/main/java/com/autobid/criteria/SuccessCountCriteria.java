package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.util.HashMap;

public class SuccessCountCriteria implements Criteria, Constants {

    private boolean criteriaSuccessCount, criteriaNormalCount, criteriaBeginCount, criteriaNsRate;

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) throws NumberFormatException {
        int successCount = (int) loanInfoMap.get("SuccessCount");
        int normalCount = (int) loanInfoMap.get("NormalCount");
        int gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
        double nscount_mrate = Double.parseDouble(cb.getNscountMrate());

        double nscount_frate = Double.parseDouble(cb.getNscountFrate());
        double ns_rate;
        if (successCount != 0) {
            ns_rate = (double) normalCount / (double) successCount;
        } else {
            ns_rate = 0;
        }
        //System.out.println("ns_rate="+ns_rate+",nscount_mrate="+nscount_mrate);
        criteriaNsRate = ns_rate >= nscount_mrate && gender == 1 ||
                ns_rate >= nscount_frate && gender == 2;
        criteriaSuccessCount = successCount >= Integer.parseInt(cb.getSuccessLimit());
        criteriaNormalCount = normalCount >= Integer.parseInt(cb.getNormalLimit());
        criteriaBeginCount = successCount >= Integer.parseInt(cb.getBeginLimit());

    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) throws NumberFormatException {
        calc(loanInfoMap, cb);
        if (criteriaSuccessCount && criteriaNormalCount && criteriaNsRate) {
            return GOOD;
        } else if (criteriaSuccessCount && criteriaNsRate) {
            return OK;
        } else if (criteriaBeginCount && criteriaNsRate) {
            return SOSO;
        } else {
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "SuccessCount";
    }
}
