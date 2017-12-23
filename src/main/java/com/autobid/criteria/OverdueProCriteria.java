package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

public class OverdueProCriteria implements Criteria,Constants {

	int overdueLessCount,overdueMoreCount,normalCount,gender;
	boolean criteriaMore,criteriaLess,criteriaLessRate,
		criteriaLessMoreRate,criteriaNormal,criteriaOverdue;
	
	public void calc(HashMap<String, Object> loanInfoMap,ConfBean cb) throws Exception {
		
		overdueLessCount = (int) loanInfoMap.get("OverdueLessCount");
		overdueMoreCount = (int) loanInfoMap.get("OverdueMoreCount");
		normalCount = (int)loanInfoMap.get("NormalCount");
		gender = Integer.parseInt(loanInfoMap.get("Gender").toString());
		criteriaMore = overdueMoreCount==0;
		criteriaLess = overdueLessCount==0;
		criteriaLessRate = normalCount!=0?
				new Integer(overdueLessCount).doubleValue()/normalCount < 
				Double.parseDouble(cb.getOverdueRate()): false;
		criteriaLessMoreRate = normalCount!=0?
				new Integer(overdueLessCount).doubleValue()/normalCount < 
				Double.parseDouble(cb.getOverdueRate()) * 1.5: false;		
		criteriaNormal = normalCount >= Integer.parseInt(cb.getNormalLimit());
	}
	
	public int getLevel(HashMap<String,Object> loanInfoMap,ConfBean cb) throws Exception {
		calc(loanInfoMap,cb);
		if(criteriaMore && criteriaLess && criteriaNormal){
			return GOOD;
		}else if(criteriaMore && criteriaLess && gender == 2){
			return GOOD;
		}else{
			return NONE;
		}
	}
	
	public String getCriteriaName(){
		return "OverduePro";
	}
}
