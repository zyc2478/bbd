package com.autobid.filter;

import com.autobid.strategy.BidDebtStrategy;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BidInfosFilter {


	public static JSONArray filter(JSONArray loanInfos) throws Exception {
		
		JSONArray bidFiltered = new JSONArray();
		BidDebtStrategy bds = new BidDebtStrategy();
		
		for(int i=0;i<loanInfos.size();i++) {
			JSONObject loanInfo = loanInfos.getJSONObject(i);
			boolean strategyOk = bds.determineStrategy(loanInfo);
			if(strategyOk) {
				bidFiltered.add(loanInfo);
			}
		}
		return bidFiltered;
	}
	

}
