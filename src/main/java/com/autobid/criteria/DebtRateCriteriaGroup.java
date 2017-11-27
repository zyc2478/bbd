package com.autobid.criteria;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import com.autobid.entity.Criteria;
import com.autobid.entity.CriteriaGroup;

public class DebtRateCriteriaGroup extends CriteriaGroup {
	
	ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();
	

	public DebtRateCriteriaGroup(){
		initCriteria();
	}
	
	@Before
	public void initCriteria(){
		this.setCriteriaList(criteriaList);
		addCriteria(new BasicCriteria());
		//addCriteria(new CreditCodeCriteria());
		addCriteria(new DebtRateProCriteria());
		addCriteria(new CertificateCriteria());
		addCriteria(new OverdueProCriteria());
		addCriteria(new LoanAmountProCriteria());
		addCriteria(new EducationCriteria());
	}
	
	@Test
	public void testCriteriaList(){
		System.out.println("size is " + criteriaList.size());
		
	}
}
