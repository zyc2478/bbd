package com.autobid.filter;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.autobid.entity.DebtInfo;
import com.autobid.util.ConfUtil;

public class DebtListFilter {

	public static ArrayList<DebtInfo> debtListFilter(JSONArray debtListArray) throws NumberFormatException, IOException {
		
		ArrayList<DebtInfo> dList = new ArrayList<DebtInfo>();
		for(int i=0;i<debtListArray.length();i++) {
			JSONObject dl = debtListArray.getJSONObject(i);
			String creditCode = dl.getString("CreditCode");
			Double priceForSale = dl.getDouble("PriceforSale");
			Double priceForSaleRate = dl.getDouble("PriceForSaleRate");
				
			if(determineCreditCode(creditCode) && determinePrice(priceForSale) && determinePriceForSaleRate(priceForSaleRate)) {
				DebtInfo di = new DebtInfo();
				di.setCreditCode(creditCode);
				di.setPriceForSale(priceForSale);
				di.setDebtId(dl.getInt("DebtdealId"));
				di.setListingId(dl.getInt("ListingId"));
				di.setOwingNumber(dl.getInt("OwingNumber"));
				di.setPriceForSaleRate(priceForSaleRate);
				
				dList.add(di);
			}
		}
		return dList;		
	} 
	
	private static boolean determinePriceForSaleRate(Double priceForSaleRate) throws NumberFormatException, IOException {

		if(priceForSaleRate >= Double.parseDouble(ConfUtil.getProperty("debt_sale_rate"))) {
			return true;
		}else {
			return false;
		}
	}

	private static boolean determinePrice(Double price) throws NumberFormatException, IOException {
		
		if(price <= Double.parseDouble(ConfUtil.getProperty("debt_price_limit"))) {
			return true;
		}else {
			return false;
		}
	}

	private static boolean determineCreditCode(String creditCode) {
		if(creditCode.equals("AA")) {
			return false;
		}else {
			return true;
		}

	}
}
