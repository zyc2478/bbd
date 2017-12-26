package com.autobid.strategy;

import java.io.IOException;

//import org.apache.log4j.Logger;

import com.autobid.util.ConfBean;



import net.sf.json.JSONObject;

public class BasicDebtStrategy implements DebtStrategy {
	
	//private static Logger logger = Logger.getLogger(BasicDebtStrategy.class); 
	
	@Override
	public boolean determineStrategy(JSONObject debtInfos,ConfBean cb) throws Exception {
		
		//printStrategy(debtInfos, cb);
		
		if(determineOwingNumber(debtInfos) 	&& 
		   determineStatusId(debtInfos)		&&
		   determinePreferenceDegree(debtInfos,cb) &&
		   determineCreditCode(debtInfos,cb)) {
			return true;
		}
		return false;
	}
	
	private boolean determineOwingNumber(JSONObject debtInfos) {
		int lm = debtInfos.getInt("ListingMonths");
		int on = debtInfos.getInt("OwingNumber");
		boolean result = false;
		if(lm==6 && on>=3 && on <=4) result = true;
		if(lm==12 && on>=4 && on<=9) result = true;
		if(lm==9 && on>=3 && on <=6) result = true;
		if(lm==24 && on>=5 && on<=12) result = true;
		return result;
	}
	
	private boolean determineStatusId(JSONObject debtInfos) {
		int status = debtInfos.getInt("StatusId");
		if(status == 1) {
			return true;
		}else{
			return false;
		}
	}
	
	private boolean determinePreferenceDegree(JSONObject debtInfos,ConfBean cb) {
		double pd = debtInfos.getDouble("PreferenceDegree");		
		//�Żݶ�ԽСԽ�Ż�
		if(pd <= Double.parseDouble(cb.getDebtPreferLimit())) {
			return true;
		}else {
			return false;
		}
	}
	
	private boolean determineCreditCode(JSONObject debtInfos,ConfBean cb) throws IOException {
		int currentCredit = 0;
		int creditLimit = 0;
		final int creditAA = 0;
		final int creditA = 1;
		final int creditB = 2;
		final int creditC = 3;
		final int creditD = 4;
		final int creditE = 5;
		final int creditF = 6;
		String currentCode = debtInfos.getString("CurrentCreditCode");
		String debtCodeLimit = cb.getDebtCreditLimit();
		switch(currentCode) {
			case "AA":	currentCredit = creditAA; break;
			case "A" :	currentCredit = creditA; break;
			case "B" :  currentCredit = creditB; break;
			case "C" :  currentCredit = creditC; break;
			case "D" :	currentCredit = creditD; break;
			case "E" :  currentCredit = creditE; break;
			case "F" :	currentCredit = creditF; break;
		}
		switch(debtCodeLimit) {
			case "AA":	creditLimit = creditAA; break;
			case "A" :	creditLimit = creditA; break;
			case "B" :  creditLimit = creditB; break;
			case "C" :  creditLimit = creditC; break;
			case "D" :	creditLimit = creditD; break;
			case "E" :  creditLimit = creditE; break;
			case "F" :	creditLimit = creditF; break;
		}
		if(currentCredit >= creditLimit) {
			return true;
		}else {
			return false;
		}
	}
	
/*	private void printStrategy(JSONObject debtInfos,ConfBean cb) throws IOException {
		logger.info("determineOwingNumber(debtInfos):"+determineOwingNumber(debtInfos)+", determineStatusId(debtInfos):"+
				determineStatusId(debtInfos)+", determinePreferenceDegree(debtInfos):"+determinePreferenceDegree(debtInfos,cb)+
				", determineCreditCode(debtInfos,cb)):"+ determineCreditCode(debtInfos,cb));
	}*/
}