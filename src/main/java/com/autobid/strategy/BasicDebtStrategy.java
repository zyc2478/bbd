package com.autobid.strategy;

import java.io.IOException;

import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;

import net.sf.json.JSONObject;

public class BasicDebtStrategy implements DebtStrategy {
	
	@Override
	public boolean determineStrategy(JSONObject debtInfos,ConfBean cb) throws Exception {
		if(determineOwingNumber(debtInfos) 	&& 
		   determineStatusId(debtInfos)		&&
		   determinePreferenceDegree(debtInfos) &&
		   determineCreditCode(debtInfos)) {
			return true;
		}
		return false;
	}
	
	private boolean determineOwingNumber(JSONObject debtInfos) {
		int lm = debtInfos.getInt("ListingMonths");
		int on = debtInfos.getInt("OwingNumber");
		boolean result = false;
		
		if(lm <= 12 ) {
			if(lm==6 && on>=3 && on <=4) result = true;
			if(lm==12 && on>=4 && on<=9) result = true;
			if(lm==9 && on>=3 && on <=6) result = true;
		}else {
			result = false;
		}		
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
	
	private boolean determinePreferenceDegree(JSONObject debtInfos) {
		double pd = debtInfos.getDouble("PreferenceDegree");		
		//优惠度越小越优惠
		if(pd < 0) {
			return true;
		}else {
			return false;
		}
	}
	
	private boolean determineCreditCode(JSONObject debtInfos) throws IOException {
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
		String debtCodeLimit = ConfUtil.getProperty("debt_credit_limit");
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
}
