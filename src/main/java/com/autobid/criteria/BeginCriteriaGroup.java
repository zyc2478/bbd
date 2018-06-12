package com.autobid.criteria;

import com.autobid.entity.Criteria;
import com.autobid.entity.CriteriaGroup;
import org.junit.Before;

import java.util.ArrayList;

public class BeginCriteriaGroup extends CriteriaGroup {

    private ArrayList<Criteria> criteriaList = new ArrayList<>();


    public BeginCriteriaGroup() {
        initCriteria();
    }

    @Before
    public void initCriteria() {
        this.setCriteriaList(criteriaList);
        addCriteria(new BasicCriteria());
        //addCriteria(new CreditCodeCriteria());
        addCriteria(new DebtRateCriteria());
        addCriteria(new OwingRateCriteria());
        addCriteria(new CertificateCriteria());
        addCriteria(new OverdueProCriteria());
        addCriteria(new EducationProCriteria());
    }
}
