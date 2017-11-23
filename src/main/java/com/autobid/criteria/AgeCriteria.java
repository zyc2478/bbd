package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Criteria;
import com.autobid.entity.Constants;
import com.autobid.util.ConfUtil;

public class AgeCriteria implements Criteria,Constants{

	boolean criteriaAge;
	@Override
	public void calc(HashMap<String, Object> loanInfoMap) throws Exception {
		int age = Integer.parseInt(loanInfoMap.get("Age").toString());
		criteriaAge = age >= Integer.parseInt(ConfUtil.getProperty("min_age"))
				&& age <= Integer.parseInt(ConfUtil.getProperty("max_age"));
	}
	@Override
	public int getLevel(HashMap<String, Object> loanInfoMap) throws Exception {
		calc(loanInfoMap);
		if(criteriaAge) {
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
