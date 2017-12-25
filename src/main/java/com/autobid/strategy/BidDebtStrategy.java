package com.autobid.strategy;

import java.io.IOException;
import java.util.HashMap;

import com.autobid.util.ConfBean;

import net.sf.json.JSONObject;

public class BidDebtStrategy implements DebtStrategy {

	@Override
	public boolean determineStrategy(JSONObject loanInfo,ConfBean cb) throws Exception {

		boolean strategyOk = false;
		
		if(determineDebtRate(loanInfo) &&
				determineOverdue(loanInfo) &&
				determineSuccessCount(loanInfo, cb)) {
			strategyOk = true;
		}
		
		return strategyOk;
	}
		
	private boolean determineDebtRate(JSONObject loanInfo) {
		
		boolean debtRateOk = false;
		
		double totalPrincipal = loanInfo.getDouble("TotalPrincipal");
		double owingAmount = loanInfo.getDouble("OwingAmount");
		double debtRate = owingAmount / totalPrincipal;
		if(debtRate < 1/3) {
			debtRateOk = true;
		}
		return debtRateOk;
	}
	
	private boolean determineOverdue(JSONObject loanInfo) {
		boolean overdueOk = false;
		int overdueMore = loanInfo.getInt("OverdueMoreCount");
		int overdueLess = loanInfo.getInt("OverdueLessCount");
		int normalCount = loanInfo.getInt("NormalCount");
		
		if(overdueLess <= normalCount/10 && overdueMore == 0) {
			overdueOk = true;
		}
		
		return overdueOk;
	}
	
	private boolean determineSuccessCount(JSONObject loanInfo,ConfBean cb){
		boolean successCountOk = false;
		int gender = loanInfo.getInt("Gender");
		double normalCount = loanInfo.getDouble("NormalCount");
		double successCount = loanInfo.getDouble("successCount");
		double ns_rate = normalCount/successCount;
		double ns_mrate = Double.parseDouble(cb.getNscountMrate());
		double ns_frate = Double.parseDouble(cb.getNscountFrate());
		int successLimit = Integer.parseInt(cb.getSuccessLimit());
		if(successCount!=0) {
			ns_rate = normalCount/successCount;
		}
		if(successCount >= successLimit) {
			if(gender==1 && ns_rate >= ns_mrate) {
				successCountOk = true;
			}else if(gender==2 && ns_rate >= ns_frate) {
				successCountOk = true;
			}
		}
		return successCountOk;
	}
	


}
