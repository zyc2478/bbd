package com.autobid.criteria;

import com.autobid.entity.Criteria;
import com.autobid.entity.CriteriaGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class EduDebtCriteriaGroup extends CriteriaGroup {

    private ArrayList<Criteria> criteriaList = new ArrayList<>();


    public EduDebtCriteriaGroup() {
        initCriteria();
    }

    @Before
    public void initCriteria() {
        this.setCriteriaList(criteriaList);
        addCriteria(new BasicCriteria());
        addCriteria(new EduDebtProCriteria());
        //addCriteria(new CreditCodeCriteria());
        addCriteria(new CertificateCriteria());
        addCriteria(new OverdueProCriteria());
        //addCriteria(new EducationProCriteria());
    }

    @Test
    public void testCriteriaList() {
        System.out.println("size is " + criteriaList.size());

    }

}
