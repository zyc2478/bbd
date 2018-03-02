package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class OverdueProCriteria implements Criteria, Constants {

    private int gender;
    private boolean criteriaMore;
    private boolean criteriaLess;
    private boolean criteriaNormal;

    public void calc(JSONObject loanInfos, ConfBean cb) {

        int overdueLessCount = (int) loanInfos.get("OverdueLessCount");
        int overdueMoreCount = (int) loanInfos.get("OverdueMoreCount");
        int normalCount = (int) loanInfos.get("NormalCount");
        gender = Integer.parseInt(loanInfos.get("Gender").toString());
        criteriaMore = overdueMoreCount == 0;
        criteriaLess = overdueLessCount == 0;
        criteriaNormal = normalCount >= Integer.parseInt(cb.getNormalLimit());
    }

    public int getLevel(JSONObject loanInfos, ConfBean cb) {
        calc(loanInfos, cb);
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
