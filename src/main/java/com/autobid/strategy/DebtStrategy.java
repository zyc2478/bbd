package com.autobid.strategy;

import net.sf.json.JSONObject;

public interface DebtStrategy {
	public boolean ifCanBuy(JSONObject debtInfos) throws Exception;
}
