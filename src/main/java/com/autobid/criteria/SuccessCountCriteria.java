package com.autobid.criteria;

import java.io.IOException;
import java.util.HashMap;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfUtil;

public class SuccessCountCriteria implements Criteria,Constants {

	static int successCount,normalCount,beginCount,gender;
	private double ns_rate,nscount_mrate,nscount_frate;
	boolean criteriaSuccessCount,criteriaNormalCount,criteriaBeginCount,criteriaNsRate;
	public void calc(HashMap<String, Object> loanInfoMap) throws NumberFormatException, IOException {
		successCount = (int)loanInfoMap.get("SuccessCount");
		normalCount = (int)loanInfoMap.get("NormalCount");
		gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
		nscount_mrate = Double.parseDouble(ConfUtil.getProperty("nscount_mrate"));

		nscount_frate = Double.parseDouble(ConfUtil.getProperty("nscount_frate"));	
		if(successCount!=0) {
			ns_rate = (double)normalCount/(double)successCount;
		}else {
			ns_rate = 0;
		}
		//System.out.println("ns_rate="+ns_rate+",nscount_mrate="+nscount_mrate);
		criteriaNsRate = ns_rate >= nscount_mrate && gender == 1 ||
						ns_rate >= nscount_frate && gender == 2;
		criteriaSuccessCount = successCount >= Integer.parseInt(ConfUtil.getProperty("success_limit"));
		criteriaNormalCount = normalCount >= Integer.parseInt(ConfUtil.getProperty("normal_limit"));
		criteriaBeginCount = successCount >= Integer.parseInt(ConfUtil.getProperty("begin_limit"));
		
	}

	public int getLevel(HashMap<String,Object> loanInfoMap) throws NumberFormatException, IOException {
		calc(loanInfoMap);
		if(criteriaSuccessCount && criteriaNormalCount && criteriaNsRate){
			return GOOD;
		}else if(criteriaSuccessCount && criteriaNsRate){
			return OK;
		}else if(criteriaBeginCount && criteriaNsRate ) {
			return SOSO;
		}else{
			return NONE;
		}
	}
	
	public String getCriteriaName(){
		return "SuccessCount";
	}
}
