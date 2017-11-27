package com.autobid.entity;

import java.util.ArrayList;

/** 
* @ClassName: CriteriaGroup 
* @Description: 策略组，如标的可投，则会唯一匹配一个策略组
* @author Richard Zeng 
* @date 2017年10月13日 下午5:18:05 
*  
*/
public class CriteriaGroup {
	
	ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();
	public void addCriteria(Criteria criteria){
		criteriaList.add(criteria);
	}
	public void setCriteriaList(ArrayList<Criteria> criteriaList){
		this.criteriaList = criteriaList;
	}
	public ArrayList<Criteria> getCriteriaList(){
		return criteriaList;
	}
}
