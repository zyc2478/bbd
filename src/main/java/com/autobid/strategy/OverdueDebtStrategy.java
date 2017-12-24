package com.autobid.strategy;

import net.sf.json.JSONObject;

public class OverdueDebtStrategy implements DebtStrategy{

	BasicDebtStrategy bds = new BasicDebtStrategy();
	
	@Override
	public boolean determineStrategy(JSONObject debtInfos) throws Exception {
		boolean strategyIsOk = bds.determineStrategy(debtInfos) && determineNoOverdue(debtInfos);
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
