package com.autobid.entity;

import java.util.ArrayList;

/**
 * @author Richard Zeng
 * @ClassName: CriteriaGroup
 * @Description: �����飬���Ŀ�Ͷ�����Ψһƥ��һ��������
 * @date 2017��10��13�� ����5:18:05
 */
public class CriteriaGroup {

    ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();

    public void addCriteria(Criteria criteria) {
        criteriaList.add(criteria);
    }

    public ArrayList<Criteria> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(ArrayList<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }
}
