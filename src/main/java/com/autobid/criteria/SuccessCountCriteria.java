package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.io.IOException;
import java.util.HashMap;

public class SuccessCountCriteria implements Criteria, Constants {

    static int successCount, normalCount, beginCount, gender;
    boolean criteriaSuccessCount, criteriaNormalCount, criteriaBeginCount, criteriaNsRate;
    private double ns_rate, nscount_mrate, nscount_frate;

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) throws NumberFormatException, IOException {
        successCount = (int) loanInfoMap.get("SuccessCount");
        normalCount = (int) loanInfoMap.get("NormalCount");
        gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
        nscount_mrate = Double.parseDouble(cb.getNscountMrate());

        nscount_frate = Double.parseDouble(cb.getNscountFrate());
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

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) throws NumberFormatException, IOException {
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
