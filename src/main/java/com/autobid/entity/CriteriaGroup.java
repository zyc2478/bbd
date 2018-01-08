package com.autobid.entity;

import java.util.ArrayList;

/**
 * @Author Richard Zeng
 * @ClassName: CriteriaGroup
 * @Description: 策略组，如标的可投，则会唯一匹配一个策略组
 * @Date 2017年10月13日 下午5:18:05
 */
public class CriteriaGroup {

    private ArrayList<Criteria> criteriaList = new ArrayList<>();

    protected void addCriteria(Criteria criteria) {
        criteriaList.add(criteria);
    }

    public ArrayList<Criteria> getCriteriaList() {
        return criteriaList;
    }

    protected void setCriteriaList(ArrayList<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }
}
