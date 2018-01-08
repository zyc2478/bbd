package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.util.HashMap;

public class OverdueProCriteria implements Criteria, Constants {

    private int gender;
    private boolean criteriaMore;
    private boolean criteriaLess;
    private boolean criteriaNormal;

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {

        int overdueLessCount = (int) loanInfoMap.get("OverdueLessCount");
        int overdueMoreCount = (int) loanInfoMap.get("OverdueMoreCount");
        int normalCount = (int) loanInfoMap.get("NormalCount");
        gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
        criteriaMore = overdueMoreCount == 0;
        criteriaLess = overdueLessCount == 0;
        criteriaNormal = normalCount >= Integer.parseInt(cb.getNormalLimit());
    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        calc(loanInfoMap, cb);
        if (criteriaMore && criteriaLess && criteriaNormal) {
            return GOOD;
        } else if (criteriaMore && criteriaLess && gender == 2) {
            return GOOD;
        } else {
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "OverduePro";
    }
}
