package com.autobid.criteria;

import com.autobid.entity.Criteria;
import com.autobid.entity.CriteriaGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class EduCriteriaGroup extends CriteriaGroup {

    private ArrayList<Criteria> criteriaList = new  ArrayList<>();;

    public EduCriteriaGroup() {
        initCriteria();
    }

    @Before
    public void initCriteria() {
        this.setCriteriaList(criteriaList);
        addCriteria(new BasicCriteria());
        //addCriteria(new CreditCodeCriteria());
        addCriteria(new DebtRateCriteria());
        addCriteria(new CertificateCriteria());
        addCriteria(new OverdueProCriteria());
        addCriteria(new LoanAmountProCriteria());
        addCriteria(new EducationProCriteria());
    }

    @Test
    public void testCriteriaList() {
        System.out.println("size is " + criteriaList.size());

    }
}
