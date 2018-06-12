package com.autobid.bbd;

import com.autobid.util.FormatUtil;
import com.autobid.util.JSONUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @Author Richard Zeng
 * @ClassName: BidDataParser
 * @Description: 标的数据处理程序
 * @Date 2017年10月13日 下午5:11:12
 */

@SuppressWarnings("deprecation")
public class BidDataParser {

    private static Logger logger = Logger.getLogger(BidDataParser.class);

    public static double getBalance(String queryBalanceJson) {

        JSONObject balanceJson = JSONObject.fromObject(queryBalanceJson);
        //logger.info(balanceJson);
        if(JSONUtil.decodeUnicode(queryBalanceJson).contains("令牌校验失败")){
            logger.info("getBalance: 令牌校验失败");
            return 0;
        }
        JSONArray balanceArray = new JSONArray();
        if(queryBalanceJson.contains("Balance")){
           balanceArray = balanceJson.getJSONArray("Balance");
        }else{
            logger.error("未成功查询余额！" + queryBalanceJson);
        }

        double canUseBalance = 0;
        for (int i = 0; i < balanceArray.size(); i++) {
            JSONObject balanceObject = balanceArray.getJSONObject(i);
            String accountCategory = balanceObject.getString("AccountCategory");
            if (JSONUtil.decodeUnicode(accountCategory).contains("用户现金余额")) {
                canUseBalance = balanceObject.getDouble("Balance");
            }
        }
        //logger.info("可用余额为：" + canUseBalance);
        return canUseBalance;
    }

    public static List<Integer> getListingIds(JSONArray loanListArray) {
        //System.out.println("-------------------getListingIds----------------------------");

        List<Integer> listingIds = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < loanListArray.size(); i++) {
            JSONObject loanInfoObj = loanListArray.getJSONObject(i);
            int listingId = loanInfoObj.getInt("ListingId");
            String creditCode = loanInfoObj.getString("CreditCode");
            if (!creditCode.equals("AA")) {
                listingIds.add(listingId);
                j++;
            }
        }
        return listingIds;
    }

/*

    public static Integer[][] getListingIdsParted(List<Integer> listingIds) {
        //System.out.println("-------------------getListingIdsParted----------------------------");
        int size = listingIds.size();
        Integer[][] listingIdsParted;
        if (size % 10 == 0) {
            listingIdsParted = new Integer[size / 10][10];
        } else {
            listingIdsParted = new Integer[size / 10 + 1][10];
        }
        //System.out.println(listingIds);
        Iterator<Integer> listingIdsIt = listingIds.iterator();

        Integer[] listingIdsArray = new Integer[listingIds.size()];
        int s = 0;
        while (listingIdsIt.hasNext()) {
            listingIdsArray[s] = listingIdsIt.next();
            s++;
            //System.out.println("listingIdsArray:"+listingIdsArray[s]);
        }
		*/
/*for(int i=0;i<listingIdsArray.length;i++) {
			System.out.println(listingIdsArray[i]);
		}*//*


        int p = 0;
*/
/*		System.out.println("listingIdsParted length :"+listingIdsParted.length);
		System.out.println("listingIdsParted[0] length :"+listingIdsParted[0].length);*//*


        for (int i = 0; i < listingIdsParted.length; i++) {
            for (int j = 0; j < listingIdsParted[0].length && i * listingIdsParted[0].length + j < listingIdsArray.length; j++) {
                listingIdsParted[i][j] = listingIdsArray[p];
                //System.out.println("listingIdsParted["+i+"]+["+j+"]:"+listingIdsParted[i][j]);
                p++;
            }
        }
        return listingIdsParted;
    }
*/
/*
    public static ArrayList<List<Integer>> getListingIdsCollector(Integer[][] listingIdsParted) {
        //System.out.println("-------getListingIdsCollector------");

        ArrayList<List<Integer>> listingIdsCollector = new ArrayList<>();
        for (Integer[] aListingIdsParted : listingIdsParted) {
            List<Integer> listingIdsProcessed = new ArrayList<>(listingIdsParted[0].length);
            for (int i = 0; i < listingIdsParted[0].length && aListingIdsParted[i] != null; i++) {
                listingIdsProcessed.add(aListingIdsParted[i]);
            }
            listingIdsCollector.add(listingIdsProcessed);
        }
        return listingIdsCollector;
    }*/

    private static JSONArray getLoanInfos(String batchListInfos) {
        //batchListInfos = FormatUtil.filterStrToJSON(batchListInfos);
        //传进来的JSON都是经过判定的，可以保障，所以无需再格式化
        JSONObject batchListInfosObject = JSONObject.fromObject(batchListInfos);
        //System.out.println(batchListInfosObject);
    	/*if(JsonUtil.decodeUnicode(batchListInfos).contains("您的操作太频繁啦")) {
    		System.out.println("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
    		Thread.sleep(60000);
    	}*/
        return batchListInfosObject.getJSONArray("LoanInfos");
    }

    public static ArrayList<JSONArray> getLoanInfosCollector(ArrayList<String> batchListInfosCollector) {
        ArrayList<JSONArray> loanInfosCollector = new ArrayList<>();
        //System.out.println("batchListInfosCollector size :" + batchListInfosCollector);
        for (String aBatchListInfosCollector : batchListInfosCollector) {
            if( JSONUtil.determineJsonHead(aBatchListInfosCollector) && aBatchListInfosCollector.indexOf("LoanInfos")!=-1){
                JSONArray loanInfosArray = getLoanInfos(aBatchListInfosCollector);//如果是合法的JSON，才可以获取其中的loanInfos
                loanInfosCollector.add(loanInfosArray);
            }
        }
        return loanInfosCollector;
    }

/*    public static HashMap<String, Object> getLoanInfoMap(JSONObject loanInfoObj) {

        HashMap<String, Object> loanInfoMap = new HashMap<>();
        loanInfoMap.put("Amount", loanInfoObj.getInt("Amount"));                            //借款金额
        loanInfoMap.put("CreditCode", loanInfoObj.getString("CreditCode"));                //标的等级
        loanInfoMap.put("RemainFunding", loanInfoObj.getInt("RemainFunding"));            //剩余可投金额
        loanInfoMap.put("Gender", loanInfoObj.getInt("Gender"));                            //性别
        loanInfoMap.put("EducationDegree", loanInfoObj.getString("EducationDegree"));    //学历
        loanInfoMap.put("StudyStyle", loanInfoObj.getString("StudyStyle"));                //学习形式
        loanInfoMap.put("Age", loanInfoObj.getInt("Age"));                                //年龄
        loanInfoMap.put("SuccessCount", loanInfoObj.getInt("SuccessCount"));                //成功借款次数
        loanInfoMap.put("NormalCount", loanInfoObj.getInt("NormalCount"));                //正常还清次数
        loanInfoMap.put("OverdueLessCount", loanInfoObj.getInt("OverdueLessCount"));        //逾期(1-15)还清次数
        loanInfoMap.put("OverdueMoreCount", loanInfoObj.getInt("OverdueMoreCount"));        //逾期(15天以上)还清次数
        loanInfoMap.put("OwingAmount", loanInfoObj.getInt("OwingAmount"));                //待还金额
        loanInfoMap.put("HighestDebt", loanInfoObj.getInt("HighestDebt"));                //历史最高负债
        if(loanInfoObj.get("HighestPrincipal").equals(null)){loanInfoMap.put("HighestPrincipal",0);}else{
            loanInfoMap.put("HighestPrincipal", JSONUtil.parseInt(loanInfoObj.get("HighestPrincipal")));} //单笔最高借款金额
        if(loanInfoObj.get("TotalPrincipal").equals(null)){loanInfoMap.put("TotalPrincipal",0);}else{
            loanInfoMap.put("TotalPrincipal", JSONUtil.parseInt(loanInfoObj.get("TotalPrincipal")));} //单笔最高借款金额
        loanInfoMap.put("TotalPrincipal", JSONUtil.parseInt(loanInfoObj.get("TotalPrincipal")));    //累计借款金额
        loanInfoMap.put("LastSuccessBorrowTime", loanInfoObj.getString("LastSuccessBorrowTime"));    //上次成功借款时间
        loanInfoMap.put("CertificateValidate", loanInfoObj.getInt("CertificateValidate"));            //学历认证0 未认证 1已认证
        loanInfoMap.put("NciicIdentityCheck", loanInfoObj.getInt("NciicIdentityCheck"));    //户籍认证0 未认证 1已认证
        loanInfoMap.put("VideoValidate", loanInfoObj.getInt("VideoValidate"));            //视频认证0 未认证 1已认证
        loanInfoMap.put("CreditValidate", loanInfoObj.getInt("CreditValidate"));            //征信认证0 未认证 1已认证
        loanInfoMap.put("EducateValidate", loanInfoObj.getInt("EducateValidate"));        //学籍认证0 未认证 1已认证
        return loanInfoMap;
    }*/

    public static ArrayList<List<Integer>> getListingIdsCollector(List<Integer> listingIds) {
        ArrayList<List<Integer>> dll = new ArrayList<>();
        int size = listingIds.size();
        int partSize = 10;
        int m = size % partSize;
        int partCount;

        if (m > 0) {
            partCount = size / partSize + 1;
        } else {
            partCount = size / partSize;
        }

        for (int i = 0; i < partCount; i++) {
            /*
             * 情况1：比如对于35个元素最后一轮，partCount = 4,m!=0,i=3时，fromIndex为30,toindex为size-1即34
             * 情况2：比如对于30个元素（m==0)最后一轮，m==0,partCount=3,fromIndex为20，toindex为30
             * 情况3：比如对于35个元素第二轮，i=1,fromIndex=10,toindex=20
             */
            if(m!=0 && partCount==1 && size <= partSize){
                List<Integer> subDebtList = listingIds;
                dll.add(subDebtList);
            }else if (m != 0 && i == partCount - 1) {
                List<Integer> subDebtList = listingIds.subList(i * partSize, size - 1);
                dll.add(subDebtList);
            }else {
                List<Integer> subDebtList = listingIds.subList(i * partSize, (i + 1) * partSize);
                dll.add(subDebtList);
            }
        }
        return dll;
    }
}
