package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class GenderCriteria implements Criteria, Constants {

    private boolean criteriaGender;

    public void calc(JSONObject loanInfos, ConfBean confBean) {
        int gender_config = Integer.parseInt(confBean.getGender());
        int gender = Integer.parseInt(loanInfos.get("Gender").toString());
        if(gender == gender_config || gender_config == 3){
            criteriaGender = true;
        }else{
            criteriaGender = false;
        }
    }

    public int getLevel(JSONObject loanInfos, ConfBean confBean) {
        calc(loanInfos, confBean);
        if (criteriaGender) {
            return OK;
        } else {
            return NONE;
        }
    }

    @Override
    public String getCriteriaName() {
        return "Gender";
    }

}