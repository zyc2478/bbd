package com.autobid.criteria;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import com.autobid.entity.Criteria;
import com.autobid.entity.CriteriaGroup;

public class BeginCriteriaGroup extends CriteriaGroup {
	
	ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();
	

	public BeginCriteriaGroup(){
		initCriteria();
	}
	
	@Before
	public void initCriteria(){
		this.setCriteriaList(criteriaList);
		addCriteria(new BasicCriteria());
		//addCriteria(new CreditCodeCriteria());
		addCriteria(new DebtRateCriteria());
		addCriteria(new CertificateCriteria());
		addCriteria(new OverdueProCriteria());
		addCriteria(new LoanAmountProCriteria());
		addCriteria(new BeginProCriteria());
	}
	
	@Test
	public void testCriteriaList(){
		System.out.println("size is " + criteriaList.size());
		
	}

}
