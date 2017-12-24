package com.autobid.strategy;

import net.sf.json.JSONObject;

public interface DebtStrategy {
	public boolean determineStrategy(JSONObject debtInfos) throws Exception;
}
