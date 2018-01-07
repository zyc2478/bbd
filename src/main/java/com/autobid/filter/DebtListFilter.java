package com.autobid.filter;

import com.autobid.util.ConfBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DebtListFilter implements ListFilter {

    private static boolean determinePriceForSaleRate(Double priceForSaleRate, ConfBean cb) throws NumberFormatException {

        return priceForSaleRate >= Double.parseDouble(cb.getDebtSaleRate());
    }

    private static boolean determinePrice(Double price, ConfBean cb) throws NumberFormatException {

        return price >= Double.parseDouble(cb.getDebtMinPrice()) && price <= Double.parseDouble(cb.getDebtPriceLimit());
    }

    private static boolean determineCreditCode(String creditCode) {
        return !creditCode.equals("AA");
    }

    public JSONArray filter(JSONArray debtListArray, ConfBean cb) {

        JSONArray debtListFiltered = new JSONArray();

        for (int i = 0; i < debtListArray.size(); i++) {
            JSONObject dl = debtListArray.getJSONObject(i);
            String creditCode = dl.getString("CreditCode");
            Double priceForSale = dl.getDouble("PriceforSale");
            Double priceForSaleRate = dl.getDouble("PriceforSaleRate");

            if (determineCreditCode(creditCode) && determinePrice(priceForSale, cb) && determinePriceForSaleRate(priceForSaleRate, cb)) {
                debtListFiltered.add(dl);
            }
        }
        return debtListFiltered;
    }

}
