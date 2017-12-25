package com.autobid.strategy;

import com.autobid.util.ConfBean;

import net.sf.json.JSONObject;

public class OverdueDebtStrategy implements DebtStrategy{

	BasicDebtStrategy bds = new BasicDebtStrategy();
	
	@Override
	public boolean determineStrategy(JSONObject debtInfos,ConfBean cb) throws Exception {
		boolean strategyIsOk = bds.determineStrategy(debtInfos,cb) && determineNoOverdue(debtInfos);
		//System.out.println("ifCanBuy in OverdueDebtStrategy");
		return strategyIsOk;
	}
	
	private boolean determineNoOverdue(JSONObject debtInfos) {
		boolean noOverdueOk = false; 
		int pastDueNumber = debtInfos.getInt("PastDueNumber");
		int pastDueDay = debtInfos.getInt("PastDueDay");
		noOverdueOk = pastDueNumber==1 && pastDueDay <15;
		return noOverdueOk;
	}
}
