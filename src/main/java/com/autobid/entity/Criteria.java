package com.autobid.entity;

import com.autobid.util.ConfBean;

import java.util.HashMap;

/**
 * @author Richard Zeng
 * @ClassName: Criteria
 * @Description: ���Խӿڣ����в��Ծ�ʵ�ּ����Լ���ȡ���Եȼ��ķ���
 * @date 2017��10��13�� ����5:17:00
 */
public interface Criteria {
    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception;

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception;

    public String getCriteriaName();
}
