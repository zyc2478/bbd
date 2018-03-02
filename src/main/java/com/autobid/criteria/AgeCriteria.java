package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class AgeCriteria implements Criteria, Constants {

    private boolean criteriaM, criteriaF;

    public void calc(JSONObject loanInfos, ConfBean confBean) {
        int age = Integer.parseInt(loanInfos.get("Age").toString());
        int gender = Integer.parseInt(loanInfos.get("Gender").toString());
        criteriaM = age >= Integer.parseInt(confBean.getMinMage())
                && age <= Integer.parseInt(confBean.getMaxMage())
                && gender == 1;
        criteriaF = age >= Integer.parseInt(confBean.getMinFage())
                && age <= Integer.parseInt(confBean.getMaxFage())
                && gender == 2;
    }

    public int getLevel(JSONObject loanInfos, ConfBean confBean) {
        calc(loanInfos, confBean);
        if (criteriaM || criteriaF) {
            return OK;
        } else {
            return NONE;
        }
    }

    @Override
    public String getCriteriaName() {
        return "Age";
    }

}
