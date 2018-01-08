package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class OverdueCriteria implements Criteria, Constants {

    private int gender;
    private boolean criteriaMore;
    private boolean criteriaLess;
    private boolean criteriaLessRate;
    private boolean criteriaLessFRate;
    private boolean criteriaNormal;

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception {

        int overdueLessCount = (int) loanInfoMap.get("OverdueLessCount");
        int overdueMoreCount = (int) loanInfoMap.get("OverdueMoreCount");
        int normalCount = (int) loanInfoMap.get("NormalCount");
        gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
        criteriaMore = overdueMoreCount == 0;
        criteriaLess = overdueLessCount == 0;
        criteriaLessRate = (normalCount != 0) && ((new Integer(overdueLessCount).doubleValue() / normalCount) <
                Double.parseDouble(cb.getOverdueRate()));
        criteriaLessFRate = normalCount != 0 && new Integer(overdueLessCount).doubleValue() / normalCount <
                Double.parseDouble(cb.getOverdueRate()) * Double.parseDouble(cb.getOverdueFrate());

/*		System.out.println(new Integer(overdueLessCount).doubleValue()/normalCount);
		System.out.println(	Double.parseDouble(ConfUtil.getProperty("overdue_rate")) * 
				Double.parseDouble(ConfUtil.getProperty("overdue_multiple")));*/
        criteriaNormal = normalCount >= Integer.parseInt(ConfUtil.getProperty("normal_limit"));
    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception {
        calc(loanInfoMap, cb);
        if (criteriaMore && criteriaLess && criteriaNormal) {
            return GOOD;
        } else if (criteriaMore && criteriaLess && gender == 2) {
            return GOOD;
        } else if (criteriaMore && criteriaLessRate && gender == 1) {
            return OK;
        } else if (criteriaMore && criteriaLessFRate && gender == 2) {
            return OK;
        } else {
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "Overdue";
    }
}
