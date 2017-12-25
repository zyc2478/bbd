package com.autobid.filter;

import com.autobid.strategy.BidDebtStrategy;
import com.autobid.util.ConfBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BidInfosFilter {


	public static JSONArray filter(JSONArray loanInfos,ConfBean cb) throws Exception {
		
		JSONArray bidFiltered = new JSONArray();
		BidDebtStrategy bds = new BidDebtStrategy();
		
		for(int i=0;i<loanInfos.size();i++) {
			JSONObject loanInfo = loanInfos.getJSONObject(i);
			boolean strategyOk = bds.determineStrategy(loanInfo,cb);
			if(strategyOk) {
				bidFiltered.add(loanInfo);
			}
		}
		return bidFiltered;
	}
	

}
