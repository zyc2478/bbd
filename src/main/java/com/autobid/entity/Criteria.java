package com.autobid.entity;

import java.util.HashMap;

/** 
* @ClassName: Criteria 
* @Description: 策略接口，所有策略均实现计算以及获取策略等级的方法
* @author Richard Zeng 
* @date 2017年10月13日 下午5:17:00 
*  
*/
public interface Criteria {
	public void calc(HashMap<String,Object> loanInfoMap) throws Exception;
	public int getLevel(HashMap<String,Object> loanInfoMap) throws Exception;
	public String getCriteriaName();
}
