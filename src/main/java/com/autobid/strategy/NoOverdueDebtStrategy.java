package com.autobid.strategy;

import net.sf.json.JSONObject;

public class NoOverdueDebtStrategy implements DebtStrategy {

	@Override
	public boolean ifCanBuy(JSONObject debtInfos) {
		boolean canBuy = new BasicDebtStrategy().ifCanBuy(debtInfos) && determineOverdue(debtInfos);
		return canBuy;
	}		

	private boolean determineOverdue(JSONObject debtInfos) {
		return false;
	}
}
