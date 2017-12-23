package com.autobid.strategy;

import net.sf.json.JSONObject;

public class OverdueDebtStrategy implements DebtStrategy{

	@Override
	public boolean ifCanBuy(JSONObject debtInfos) throws Exception {
		boolean canBuy = new BasicDebtStrategy().ifCanBuy(debtInfos) && determineOverdue(debtInfos);
		//System.out.println("ifCanBuy in OverdueDebtStrategy");
		return canBuy;
	}
	
	private boolean determineOverdue(JSONObject debtInfos) {
		return false;
	}
}
