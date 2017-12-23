package com.autobid.filter;

import java.io.IOException;

import com.autobid.util.ConfUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DebtListFilter extends ListFilter{

	public static JSONArray filter(JSONArray debtListArray) throws Exception{
		
		JSONArray debtListFiltered = new JSONArray(); 
		
		for(int i=0;i<debtListArray.size();i++) {
			JSONObject dl = debtListArray.getJSONObject(i);
			String creditCode = dl.getString("CreditCode");
			Double priceForSale = dl.getDouble("PriceforSale");
			//String priceForSaleRate = dl.getString("PriceforSaleRate");

			Double priceForSaleRate = dl.getDouble("PriceforSaleRate");
			//System.out.println(priceForSaleRate);
				
			if(determineCreditCode(creditCode) && determinePrice(priceForSale) && determinePriceForSaleRate(priceForSaleRate)) {
				debtListFiltered.add(dl);
			}
		}
		return debtListFiltered;	
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
