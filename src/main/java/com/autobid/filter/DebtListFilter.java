package com.autobid.filter;

import java.io.IOException;
import com.autobid.util.ConfBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DebtListFilter implements ListFilter{

	public JSONArray filter(JSONArray debtListArray,ConfBean cb) throws Exception{
		
		JSONArray debtListFiltered = new JSONArray(); 
		
		for(int i=0;i<debtListArray.size();i++) {
			JSONObject dl = debtListArray.getJSONObject(i);
			String creditCode = dl.getString("CreditCode");
			Double priceForSale = dl.getDouble("PriceforSale");
			Double priceForSaleRate = dl.getDouble("PriceforSaleRate");

			if(determineCreditCode(creditCode) && determinePrice(priceForSale,cb) && determinePriceForSaleRate(priceForSaleRate,cb)) {
				debtListFiltered.add(dl);
			}
		}
		return debtListFiltered;	
	}
	
	private static boolean determinePriceForSaleRate(Double priceForSaleRate,ConfBean cb) throws NumberFormatException, IOException {

		if(priceForSaleRate >= Double.parseDouble(cb.getDebtSaleRate())) {
			return true;
		}else {
			return false;
		}
	}

	private static boolean determinePrice(Double price,ConfBean cb) throws NumberFormatException, IOException {
		
		if(price>=Double.parseDouble(cb.getDebtMinPrice()) && price <= Double.parseDouble(cb.getDebtPriceLimit())) {
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
