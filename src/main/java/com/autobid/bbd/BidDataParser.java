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
 * @Description: ������ݴ������
 * @Date 2017��10��13�� ����5:11:12
 */

@SuppressWarnings("deprecation")
public class BidDataParser {

    private static Logger logger = Logger.getLogger(BidDataParser.class);

    public static double getBalance(String queryBalanceJson) {

        JSONObject balanceJson = JSONObject.fromObject(queryBalanceJson);
        //logger.info(balanceJson);
        if(JSONUtil.decodeUnicode(queryBalanceJson).contains("����У��ʧ��")){
            logger.info("getBalance: ����У��ʧ��");
            return 0;
        }
        JSONArray balanceArray = new JSONArray();
        if(queryBalanceJson.contains("Balance")){
           balanceArray = balanceJson.getJSONArray("Balance");
        }else{
            logger.error("δ�ɹ���ѯ��" + queryBalanceJson);
        }

        double canUseBalance = 0;
        for (int i = 0; i < balanceArray.size(); i++) {
            JSONObject balanceObject = balanceArray.getJSONObject(i);
            String accountCategory = balanceObject.getString("AccountCategory");
            if (JSONUtil.decodeUnicode(accountCategory).contains("�û��ֽ����")) {
                canUseBalance = balanceObject.getDouble("Balance");
            }
        }
        //logger.info("�������Ϊ��" + canUseBalance);
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
        //��������JSON���Ǿ����ж��ģ����Ա��ϣ����������ٸ�ʽ��
        JSONObject batchListInfosObject = JSONObject.fromObject(batchListInfos);
        //System.out.println(batchListInfosObject);
    	/*if(JsonUtil.decodeUnicode(batchListInfos).contains("���Ĳ���̫Ƶ����")) {
    		System.out.println("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
    		Thread.sleep(60000);
    	}*/
        return batchListInfosObject.getJSONArray("LoanInfos");
    }

    public static ArrayList<JSONArray> getLoanInfosCollector(ArrayList<String> batchListInfosCollector) {
        ArrayList<JSONArray> loanInfosCollector = new ArrayList<>();
        //System.out.println("batchListInfosCollector size :" + batchListInfosCollector);
        for (String aBatchListInfosCollector : batchListInfosCollector) {
            if( JSONUtil.determineJsonHead(aBatchListInfosCollector) && aBatchListInfosCollector.indexOf("LoanInfos")!=-1){
                JSONArray loanInfosArray = getLoanInfos(aBatchListInfosCollector);//����ǺϷ���JSON���ſ��Ի�ȡ���е�loanInfos
                loanInfosCollector.add(loanInfosArray);
            }
        }
        return loanInfosCollector;
    }

/*    public static HashMap<String, Object> getLoanInfoMap(JSONObject loanInfoObj) {

        HashMap<String, Object> loanInfoMap = new HashMap<>();
        loanInfoMap.put("Amount", loanInfoObj.getInt("Amount"));                            //�����
        loanInfoMap.put("CreditCode", loanInfoObj.getString("CreditCode"));                //��ĵȼ�
        loanInfoMap.put("RemainFunding", loanInfoObj.getInt("RemainFunding"));            //ʣ���Ͷ���
        loanInfoMap.put("Gender", loanInfoObj.getInt("Gender"));                            //�Ա�
        loanInfoMap.put("EducationDegree", loanInfoObj.getString("EducationDegree"));    //ѧ��
        loanInfoMap.put("StudyStyle", loanInfoObj.getString("StudyStyle"));                //ѧϰ��ʽ
        loanInfoMap.put("Age", loanInfoObj.getInt("Age"));                                //����
        loanInfoMap.put("SuccessCount", loanInfoObj.getInt("SuccessCount"));                //�ɹ�������
        loanInfoMap.put("NormalCount", loanInfoObj.getInt("NormalCount"));                //�����������
        loanInfoMap.put("OverdueLessCount", loanInfoObj.getInt("OverdueLessCount"));        //����(1-15)�������
        loanInfoMap.put("OverdueMoreCount", loanInfoObj.getInt("OverdueMoreCount"));        //����(15������)�������
        loanInfoMap.put("OwingAmount", loanInfoObj.getInt("OwingAmount"));                //�������
        loanInfoMap.put("HighestDebt", loanInfoObj.getInt("HighestDebt"));                //��ʷ��߸�ծ
        if(loanInfoObj.get("HighestPrincipal").equals(null)){loanInfoMap.put("HighestPrincipal",0);}else{
            loanInfoMap.put("HighestPrincipal", JSONUtil.parseInt(loanInfoObj.get("HighestPrincipal")));} //������߽����
        if(loanInfoObj.get("TotalPrincipal").equals(null)){loanInfoMap.put("TotalPrincipal",0);}else{
            loanInfoMap.put("TotalPrincipal", JSONUtil.parseInt(loanInfoObj.get("TotalPrincipal")));} //������߽����
        loanInfoMap.put("TotalPrincipal", JSONUtil.parseInt(loanInfoObj.get("TotalPrincipal")));    //�ۼƽ����
        loanInfoMap.put("LastSuccessBorrowTime", loanInfoObj.getString("LastSuccessBorrowTime"));    //�ϴγɹ����ʱ��
        loanInfoMap.put("CertificateValidate", loanInfoObj.getInt("CertificateValidate"));            //ѧ����֤0 δ��֤ 1����֤
        loanInfoMap.put("NciicIdentityCheck", loanInfoObj.getInt("NciicIdentityCheck"));    //������֤0 δ��֤ 1����֤
        loanInfoMap.put("VideoValidate", loanInfoObj.getInt("VideoValidate"));            //��Ƶ��֤0 δ��֤ 1����֤
        loanInfoMap.put("CreditValidate", loanInfoObj.getInt("CreditValidate"));            //������֤0 δ��֤ 1����֤
        loanInfoMap.put("EducateValidate", loanInfoObj.getInt("EducateValidate"));        //ѧ����֤0 δ��֤ 1����֤
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
             * ���1���������35��Ԫ�����һ�֣�partCount = 4,m!=0,i=3ʱ��fromIndexΪ30,toindexΪsize-1��34
             * ���2���������30��Ԫ�أ�m==0)���һ�֣�m==0,partCount=3,fromIndexΪ20��toindexΪ30
             * ���3���������35��Ԫ�صڶ��֣�i=1,fromIndex=10,toindex=20
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
