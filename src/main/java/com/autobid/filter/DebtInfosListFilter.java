package com.autobid.filter;

import com.autobid.strategy.NoOverdueDebtStrategy;
import com.autobid.strategy.OverdueDebtStrategy;
import com.autobid.util.ConfBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DebtInfosListFilter extends ListFilter{
	
	public static JSONArray filter(JSONArray debtList,ConfBean cb) throws Exception {
		JSONArray dlFiltered = new JSONArray();
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		String callParentMethod=stack[3].getMethodName();
/*		System.out.println("stack[1]:"+stack[1]);
		System.out.println("stack[2]:"+stack[2]);
		System.out.println("stack[3]:"+stack[3]);
		System.out.println("callParentMethod:"+callParentMethod);*/
		
		//如果调用的第三层方法不包含No,就说明是overdue的
		boolean ifOverdue = !callParentMethod.contains("No");
		//System.out.print("ifOverdue:"+ifOverdue);
		NoOverdueDebtStrategy nos = new NoOverdueDebtStrategy();
		OverdueDebtStrategy os = new OverdueDebtStrategy();
		
		for(int i=0;i<debtList.size();i++) {
			JSONObject debtInfos = debtList.getJSONObject(i);
			if(ifOverdue==false) {
				if(nos.determineStrategy(debtInfos,cb)) {
					dlFiltered.add(debtInfos);
				}
			}else {
				if(os.determineStrategy(debtInfos,cb)) {
					dlFiltered.add(debtInfos);
				}
			}
		}
		return dlFiltered;
	}	
}
