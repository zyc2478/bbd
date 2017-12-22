package com.autobid.strategy;

import net.sf.json.JSONObject;

public class OverdueDebtStrategy implements DebtStrategy{

	@Override
	public boolean ifCanBuy(JSONObject debtInfos) {
		boolean canBuy = new BasicDebtStrategy().ifCanBuy(debtInfos);
		return canBuy;
	}
}
