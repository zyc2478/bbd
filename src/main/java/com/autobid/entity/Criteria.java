package com.autobid.entity;

import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

/**
 * @Author Richard Zeng
 * @ClassName: Criteria
 * @Description: ���Խӿڣ����в��Ծ�ʵ�ּ����Լ���ȡ���Եȼ��ķ���
 * @Date 2017��10��13�� ����5:17:00
 */
public interface Criteria {
    void calc(JSONObject loanInfos, ConfBean cb) throws Exception;

    int getLevel(JSONObject loanInfos, ConfBean cb) throws Exception;

    String getCriteriaName();
}
