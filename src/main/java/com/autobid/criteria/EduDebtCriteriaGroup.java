package com.autobid.criteria;

import com.autobid.entity.Criteria;
import com.autobid.entity.CriteriaGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class EduDebtCriteriaGroup extends CriteriaGroup {

    ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();


    public EduDebtCriteriaGroup() {
        initCriteria();
    }

    @Before
    public void initCriteria() {
        this.setCriteriaList(criteriaList);
        addCriteria(new BasicCriteria());
        addCriteria(new EduDebtProCriteria());
        //addCriteria(new CreditCodeCriteria());
        addCriteria(new DebtRateProCriteria());
        addCriteria(new CertificateCriteria());
        addCriteria(new OverdueProCriteria());
        addCriteria(new LoanAmountProCriteria());
        //addCriteria(new EducationProCriteria());
    }

    @Test
    public void testCriteriaList() {
        System.out.println("size is " + criteriaList.size());

    }

}
