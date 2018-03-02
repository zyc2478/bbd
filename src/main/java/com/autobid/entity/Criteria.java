package com.autobid.entity;

import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

/**
 * @Author Richard Zeng
 * @ClassName: Criteria
 * @Description: 策略接口，所有策略均实现计算以及获取策略等级的方法
 * @Date 2017年10月13日 下午5:17:00
 */
public interface Criteria {
    void calc(JSONObject loanInfos, ConfBean cb) throws Exception;

    int getLevel(JSONObject loanInfos, ConfBean cb) throws Exception;

    String getCriteriaName();
}
