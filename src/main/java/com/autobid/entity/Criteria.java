package com.autobid.entity;

import java.util.HashMap;

import com.autobid.util.ConfBean;

/** 
* @ClassName: Criteria 
* @Description: ���Խӿڣ����в��Ծ�ʵ�ּ����Լ���ȡ���Եȼ��ķ���
* @author Richard Zeng 
* @date 2017��10��13�� ����5:17:00 
*  
*/
public interface Criteria {
	public void calc(HashMap<String,Object> loanInfoMap,ConfBean cb) throws Exception;
	public int getLevel(HashMap<String,Object> loanInfoMap,ConfBean cb) throws Exception;
	public String getCriteriaName();
}
