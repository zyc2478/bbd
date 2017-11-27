package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Criteria;
import com.autobid.entity.Constants;
import com.autobid.util.ConfUtil;

public class AgeCriteria implements Criteria,Constants{

	boolean criteriaM,criteriaF;
	@Override
	public void calc(HashMap<String, Object> loanInfoMap) throws Exception {
		int age = Integer.parseInt(loanInfoMap.get("Age").toString());
		int gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
		criteriaM = age >= Integer.parseInt(ConfUtil.getProperty("min_mage"))
				&& age <= Integer.parseInt(ConfUtil.getProperty("max_mage"))
				&& gender == 1;
		criteriaF = age >= Integer.parseInt(ConfUtil.getProperty("min_fage"))
				&& age <= Integer.parseInt(ConfUtil.getProperty("max_fage"))
				&& gender == 2;
	}
	@Override
	public int getLevel(HashMap<String, Object> loanInfoMap) throws Exception {
		calc(loanInfoMap);
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
