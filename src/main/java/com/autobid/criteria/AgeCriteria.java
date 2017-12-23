package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Criteria;
import com.autobid.entity.Constants;
import com.autobid.util.ConfBean;

public class AgeCriteria implements Criteria,Constants{

	boolean criteriaM,criteriaF;

	public void calc(HashMap<String, Object> loanInfoMap,ConfBean confBean) throws Exception {
		int age = Integer.parseInt(loanInfoMap.get("Age").toString());
		int gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
		criteriaM = age >= Integer.parseInt(confBean.getMinMage())
				&& age <= Integer.parseInt(confBean.getMaxMage())
				&& gender == 1;
		criteriaF = age >= Integer.parseInt(confBean.getMinFage())
				&& age <= Integer.parseInt(confBean.getMaxFage())
				&& gender == 2;
	}

	public int getLevel(HashMap<String, Object> loanInfoMap,ConfBean confBean) throws Exception {
		calc(loanInfoMap,confBean);
		if(criteriaM || criteriaF) {
			return OK;
		}else {
			return NONE;
		}
	}
	@Override
	public String getCriteriaName() {
		return "Age";
	}

}
