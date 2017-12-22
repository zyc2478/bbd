package com.autobid.strategy;

import net.sf.json.JSONObject;

public class BasicDebtStrategy implements DebtStrategy {

	@Override
	public boolean ifCanBuy(JSONObject debtInfos) {
		return false;
	}
}
