package com.autobid.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;


public class JSONTest {


    @Test
    public void name() {
        String htmlStr = "<!DOCTYPE html>\n" +
                "<!--\n" +
                "To change this license header, choose License Headers in Project Properties.\n" +
                "To change this template file, choose Tools | Templates\n" +
                "and open the template in the editor.\n" +
                "-->\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title></title>\n" +
                "</head>\n" +
                "<body>";
        String jsonStr = " {\n" +
                "  \"DebtInfos\": [\n" +
                        "    {\n" +
                        "      \"DebtId\": 2594108,\n" +
                        "      \"Seller\": \"zhangsan\",\n" +
                        "      \"StatusId\": 1,\n" +
                        "      \"Lender\": \"zhangsan\",\n" +
                        "      \"BidDateTime\": \"2016-09-24T11:22:35.533\",\n" +
                        "      \"OwingNumber\": 11,\n" +
                        "      \"OwingPrincipal\": 275.8,\n" +
                        "      \"OwingInterest\": 9.75,\n" +
                        "      \"Days\": -5,\n" +
                        "      \"PriceforSaleRate\": 7.06,\n" +
                        "      \"PriceforSale\": 276.16,\n" +
                        "      \"PreferenceDegree\": 0,\n" +
                        "      \"ListingId\": 20658281,\n" +
                        "      \"CurrentCreditCode\":\"B\",\n" +
                        "      \"CreditCode\": \"AAA\",\n" +
                        "      \"ListingAmount\": 5000,\n" +
                        "      \"ListingTime\": \"2016-09-24T07:41:02.733\",\n" +
                        "      \"ListingMonths\": 12,\n" +
                        "      \"ListingRate\": 7.01,\n" +
                        "      \"PastDueNumber\": 0,\n" +
                        "      \"AllowanceRadio\": 10,\n" +
                        "      \"PastDueDay\": 0\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"Result\": 1,\n" +
                        "  \"ResultMessage\": \"\",\n" +
                        "  \"ResultCode\": null\n" +
                        "}";
        String resultJSON = jsonStr;
        StringBuffer resultBuffer = new StringBuffer();
        resultBuffer.append(htmlStr);
        resultBuffer.append(jsonStr);
        resultJSON = StringUtil.filterStrToJSON(resultBuffer.toString());
        System.out.println(resultJSON);

        JSONObject jo = JSONObject.fromObject(resultJSON);
        System.out.println(jo);
        JSONArray jsonArray = jo.getJSONArray("DebtInfos");
        System.out.println(jsonArray);
    }
}
