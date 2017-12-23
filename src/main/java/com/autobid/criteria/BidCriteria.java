package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Criteria;

public abstract class BidCriteria implements Criteria {

	public static void calc(HashMap<String, Object> loanInfoMap) throws Exception {}

	public static int getLevel(HashMap<String, Object> loanInfoMap) throws Exception {
		return 0;
	}

	@Override
	public String getCriteriaName() {
		return null;
	}

}
