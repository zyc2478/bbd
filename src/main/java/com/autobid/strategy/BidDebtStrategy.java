package com.autobid.strategy;

//import org.apache.log4j.Logger;

import com.autobid.util.ConfBean;

import net.sf.json.JSONObject;

public class BidDebtStrategy implements DebtStrategy {

	//private static Logger logger = Logger.getLogger(BidDebtStrategy.class);  
	@Override
	public boolean determineStrategy(JSONObject loanInfo,ConfBean cb) throws Exception {

		boolean strategyOk = false;
		
		//printStrategy(loanInfo, cb);
		
		if(determineDebtRate(loanInfo) &&
				determineOverdue(loanInfo) &&
				determineSuccessCount(loanInfo, cb) &&
				determineSmallerThanHighDebt(loanInfo, cb)) {
			strategyOk = true;
		}
		
		return strategyOk;
	}
		
	private boolean determineDebtRate(JSONObject loanInfo) {
		
		boolean debtRateOk = false;
		
		double totalPrincipal = loanInfo.getDouble("TotalPrincipal");
		double owingAmount = loanInfo.getDouble("OwingAmount");
		int certificateValidate = loanInfo.getInt("CertificateValidate");
		//int certificateValidate = loanInfo.getInt("CertificateValidate");
		
		double debtRate = owingAmount / totalPrincipal;
		
		//System.out.println("debtRate:"+debtRate+", TotalPrincipal:"+totalPrincipal+", OwingAmount:"+owingAmount);
		if(debtRate <= 0.333) {
			debtRateOk = true;
		}
		
		if(debtRate <=0.4 && certificateValidate==1 ) {
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
		double successCount = loanInfo.getDouble("SuccessCount");
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
	
	private boolean determineSmallerThanHighDebt(JSONObject loanInfo,ConfBean cb){
		int gender = loanInfo.getInt("Gender");
		double owingAmount = loanInfo.getDouble("OwingAmount");
		double highestDebt = loanInfo.getDouble("HighestDebt");
		double oh_rate = owingAmount/highestDebt;
		double owing_mrate = Double.parseDouble(cb.getOwingMrate());
		double owing_frate = Double.parseDouble(cb.getOwingFrate());		
		if(gender==1 && oh_rate <= owing_mrate ) {
			return true;
		}else if(gender==2 && oh_rate <= owing_frate ) {
			return true;
		}else {
			return false;
		}
	}
	
	private boolean determineLastSuccessBorrow(JSONObject loanInfo,ConfBean cb){
		boolean lastSuccessBorrowOk = false;
		int gender = loanInfo.getInt("Gender");
		double owingAmount = loanInfo.getDouble("OwingAmount");
		double highestDebt = loanInfo.getDouble("HighestDebt");
		double oh_rate = owingAmount/highestDebt;
		double owing_mrate = Double.parseDouble(cb.getOwingMrate());
		double owing_frate = Double.parseDouble(cb.getOwingFrate());		
		if(gender==1 && oh_rate <= owing_mrate ) {
			return true;
		}else if(gender==2 && oh_rate <= owing_frate ) {
			return true;
		}else {
			return false;
		}
	}
	
/*	private void printStrategy(JSONObject loanInfo,ConfBean cb) throws Exception {
		logger.info("determineDebtRate(loanInfo):" + determineDebtRate(loanInfo) + 
				", determineOverdue(loanInfo):"+ determineOverdue(loanInfo) + 
				", determineSuccessCount(loanInfo, cb):"+ determineSuccessCount(loanInfo, cb));
	}*/
	


}
