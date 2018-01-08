package com.autobid.entity;

import java.util.ArrayList;

/**
 * @Author Richard Zeng
 * @ClassName: CriteriaGroup
 * @Description: �����飬���Ŀ�Ͷ�����Ψһƥ��һ��������
 * @Date 2017��10��13�� ����5:18:05
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
