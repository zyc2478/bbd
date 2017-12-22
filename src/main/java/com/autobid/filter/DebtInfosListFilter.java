package com.autobid.filter;

import java.util.ArrayList;
import java.util.List;

import com.autobid.strategy.NoOverdueDebtStrategy;
import com.autobid.strategy.OverdueDebtStrategy;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DebtInfosListFilter extends ListFilter{
	
	public static JSONArray filter(JSONArray debtList) throws Exception {
		JSONArray dlFiltered = new JSONArray();
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		String callParentMethod=stack[2].getMethodName();
		boolean ifOverdue = !callParentMethod.contains("No");
		for(int i=0;i<debtList.size();i++) {
			JSONObject debtInfos = debtList.getJSONObject(i);
			if(ifOverdue==false) {
				if(new NoOverdueDebtStrategy().ifCanBuy(debtInfos)) {
					dlFiltered.add(debtInfos);
				}
			}else {
				if(new OverdueDebtStrategy().ifCanBuy(debtInfos)) {
					dlFiltered.add(debtInfos);
				}
			}
		}
		return dlFiltered;
	}	
}
