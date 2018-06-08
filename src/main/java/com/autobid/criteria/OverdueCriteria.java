package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class OverdueCriteria implements Criteria, Constants {

    private int gender;
    private boolean criteriaMore;
    private boolean criteriaLess;
    private boolean criteriaLessRate;
    private boolean criteriaLessFRate;
    private boolean criteriaNormal;
    private boolean criteriaShortLimit;

    public void calc(JSONObject loanInfos, ConfBean cb) throws Exception {

        int overdueLessCount = (int) loanInfos.get("OverdueLessCount");
        int overdueMoreCount = (int) loanInfos.get("OverdueMoreCount");
        int normalCount = (int) loanInfos.get("NormalCount");
        gender = Integer.parseInt(loanInfos.get("Gender").toString());
        int overdueShortLimit = Integer.parseInt(cb.getOverdueShortLimit());
        criteriaShortLimit = overdueLessCount <= overdueShortLimit;
        criteriaMore = overdueMoreCount == 0;
        criteriaLess = overdueLessCount == 0;
        criteriaLessRate = (normalCount != 0) && ((new Integer(overdueLessCount).doubleValue() / normalCount) <
                Double.parseDouble(cb.getOverdueRate()));
        criteriaLessFRate = normalCount != 0 && new Integer(overdueLessCount).doubleValue() / normalCount <
                Double.parseDouble(cb.getOverdueRate()) * Double.parseDouble(cb.getOverdueFrate());

/*        System.out.println("criteriaLessFRate:"+criteriaLessFRate);*/
/*        System.out.println("new Integer(overdueLessCount).doubleValue() / normalCount = " + new Integer(overdueLessCount).doubleValue() / normalCount);
        System.out.println("Double.parseDouble(cb.getOverdueRate()) * Double.parseDouble(cb.getOverdueFrate())"+Double.parseDouble(cb.getOverdueRate()) * Double.parseDouble(cb.getOverdueFrate()));*/
/*		System.out.println(new Integer(overdueLessCount).doubleValue()/normalCount);
		System.out.println(	Double.parseDouble(ConfUtil.getProperty("overdue_rate")) * 
				Double.parseDouble(ConfUtil.getProperty("overdue_multiple")));*/
        criteriaNormal = normalCount >= Integer.parseInt(ConfUtil.getProperty("normal_limit"));
    }

    public int getLevel(JSONObject loanInfos, ConfBean cb) throws Exception {
        calc(loanInfos, cb);
        if (criteriaMore && criteriaLess && criteriaNormal && criteriaShortLimit) {
            return GOOD;
        } else if (criteriaMore && criteriaLess && criteriaShortLimit && gender == 2 ) {
            return GOOD;
        } else if (criteriaMore && criteriaLessRate && criteriaShortLimit && gender == 1) {
            return OK;
        } else if (criteriaMore && criteriaLessFRate && criteriaShortLimit && gender == 2) {
            return OK;
        } else {
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "Overdue";
    }
}
