package com.autobid.entity;

import com.autobid.util.ConfBean;

import java.util.HashMap;

/**
 * @Author Richard Zeng
 * @ClassName: Criteria
 * @Description: ���Խӿڣ����в��Ծ�ʵ�ּ����Լ���ȡ���Եȼ��ķ���
 * @Date 2017��10��13�� ����5:17:00
 */
public interface Criteria {
    void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception;

    int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception;

    String getCriteriaName();
}
