package com.autobid.strategy;

import com.autobid.util.ConfBean;

import net.sf.json.JSONObject;

public class NoOverdueDebtStrategy implements DebtStrategy {

	BasicDebtStrategy bds = new BasicDebtStrategy();
	
	@Override
	public boolean determineStrategy(JSONObject debtInfos,ConfBean cb) throws Exception {
		boolean strategyIsOk = bds.determineStrategy(debtInfos,cb) && determineOverdue(debtInfos);
		//System.out.println("ifCanBuy in NoOverdueDebtStrategy");
		return strategyIsOk;
	}		

	private boolean determineOverdue(JSONObject debtInfos) {
		boolean overdueOk = false; 
		int pastDueNumber = debtInfos.getInt("PastDueNumber");
		int pastDueDay = debtInfos.getInt("PastDueDay");
		overdueOk = pastDueNumber==0 && pastDueDay == 0;
		return overdueOk;
	}
	
}
