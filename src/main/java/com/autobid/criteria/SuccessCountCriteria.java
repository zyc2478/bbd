package com.autobid.criteria;

import java.io.IOException;
import java.util.HashMap;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfUtil;

public class SuccessCountCriteria implements Criteria,Constants {

	static int successCount,normalCount,beginCount;
	boolean criteriaSuccessCount,criteriaNormalCount,criteriaBeginCount;
	public void calc(HashMap<String, Object> loanInfoMap) throws NumberFormatException, IOException {
		successCount = (int)loanInfoMap.get("SuccessCount");
		normalCount = (int)loanInfoMap.get("NormalCount");
		criteriaSuccessCount = successCount >= Integer.parseInt(ConfUtil.getProperty("success_limit"));
		criteriaNormalCount = normalCount >= Integer.parseInt(ConfUtil.getProperty("normal_limit"));
		criteriaBeginCount = successCount >= Integer.parseInt(ConfUtil.getProperty("begin_limit"));
	}

	public int getLevel(HashMap<String,Object> loanInfoMap) throws NumberFormatException, IOException {
		calc(loanInfoMap);
		if(criteriaSuccessCount && criteriaNormalCount){
			return GOOD;
		}else if(criteriaSuccessCount){
			return OK;
		}else if(criteriaBeginCount) {
			return SOSO;
		}else{
			return NONE;
		}
	}
	
	public String getCriteriaName(){
		return "SuccessCount";
	}
}
