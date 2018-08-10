package com.autobid.bbd;

import com.autobid.entity.BidResult;
import com.autobid.entity.LoanListResult;
import com.autobid.util.FormatUtil;
import com.autobid.util.JSONUtil;
import com.autobid.util.RedisUtil;
import com.ppdai.open.core.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Richard Zeng
 * @ClassName: BidService
 * @Description: �����Ĵ��ĸ�Ͷ��API���÷���
 * @Date 2017��10��13�� ����5:15:21
 */

@SuppressWarnings("deprecation")
public class BidService {

    private static Logger logger = Logger.getLogger(BidService.class);

    public static String queryBalanceService(String token) throws Exception {
        //����url
        String url = "https://openapi.ppdai.com/balance/balanceService/QueryBalance";

        Result result = OpenApiClient.send(url, token);
        //System.out.println(result.getContext());
        
        String resultJSON = FormatUtil.filterStrToJSON(result.getContext());
       // String resultJSON = FormatUtil.toJSON(result.getContext());

        if (JSONUtil.decodeUnicode(resultJSON).contains("���Ĳ���̫Ƶ����")) {
            System.out.println("queryBalanceService:���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
            Thread.sleep(60000);
        }/* else if (JSONUtil.decodeUnicode(resultJSON).contains("�û���Ч�������ѹ���Ч��")) {
            logger.info("Error!�û���Ч�������ѹ���Ч��");
            //System.out.println(TokenInit.getInitFlag());
            TokenInit.initToken();
            logger.info("��������������");
        }*/
        return result.getContext();
    }

    public static LoanListResult loanListService(int indexNum) throws Exception {
        //ArrayList<String> loanInfosList = new ArrayList<>();
        String url = "https://openapi.ppdai.com/listing/openapiNoAuth/loanList";
        Result result;
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String startDateTime = sdf.format(new Date());
        result = OpenApiClient.send(url, new PropertyObject("PageIndex", indexNum, ValueTypeEnum.Int32));
        //System.out.println(result.getContext());
        Jedis jedis = new Jedis();
        if(result.isSucess()){
            try{
                jedis = RedisUtil.getJedis();
                jedis.setex("startDateTime", 864000, startDateTime);
            }finally{
                jedis.close();
            }
        }
        return loanListProcess(result,indexNum);
    }

    public static LoanListResult loanListProcess(Result result,int indexNum) throws InterruptedException, IOException {

        String resultJSON = FormatUtil.filterStrToJSON(result.getContext());
        if (JSONUtil.decodeUnicode(resultJSON).contains("���Ĳ���̫Ƶ����")) {
            logger.info("loanListService:���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
            logger.error("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
            Thread.sleep(60000);
        }
        JSONArray loanInfosResultArray = new JSONArray();
        int[] loanIds = null;
        if (result.isSucess()) success:{
            String loanListResult = resultJSON;
            //logger.info("loanListResult is :" + loanListResult);
            if(!JSONUtil.determineJsonHead(loanListResult) || loanListResult.indexOf("LoanInfos")==-1) break success;  //������ǺϷ���JSON��ֱ���������success��if�߼�
            JSONObject loanListJson = JSONObject.fromObject(loanListResult);
            JSONArray loanInfosArray = loanListJson.getJSONArray("LoanInfos");
            //String loanListJson = loanAllList.substring(13);
            //logger.info("loanListArray is :" + loanInfosArray);   //Modified
            int size = loanInfosArray.size();
            loanIds = new int[size];
            int j = 0;
            String creditCode;
            for (int i = 0; i < size; i++) {
                JSONObject loanInfoObj = loanInfosArray.getJSONObject(i);
                int listingId = loanInfoObj.getInt("ListingId");
                creditCode = loanInfoObj.getString("CreditCode");
                if (!creditCode.equals("AA")) {
                    loanIds[i] = listingId;
                    //System.out.println("loanId" + i + " value:" + loanIds[i]);
                    loanInfosResultArray.add(loanInfoObj);
                    j++;
                }
            }
            //System.out.println("��" + indexNum + "��������Ϊ��" + loanIds.length + "���з��ձ����Ϊ��" + j);
            logger.info("��" + indexNum + "��������Ϊ��" + loanIds.length + "���з��ձ����Ϊ��" + j);
            //loanInfosList.add(loanInfosArray.toString());
            //logger.info(loanInfosList);
        } else {
            logger.error(result.getErrorMessage());
        }
        LoanListResult llr = new LoanListResult();
        llr.setIndexNum(indexNum);
        llr.setLoanList(loanInfosResultArray);
        assert loanIds != null;
        llr.setLoanIdCount(loanIds.length);
        return llr;
    }

    public static LoanListResult loanListServiceByTime(int indexNum,String startDateTime) throws Exception {
        int[] loanIds = null;
        //System.out.println("loanListServiceByTime");
        //ArrayList<String> loanInfosList = new ArrayList<>();
        //String url = "https://openapi.ppdai.com/invest/LLoanInfoService/LoanList";
        String url = "https://openapi.ppdai.com/listing/openapiNoAuth/loanList";
        Result result;
        result = OpenApiClient.send(url,
            new PropertyObject("PageIndex", indexNum, ValueTypeEnum.Int32),
            new PropertyObject("StartDateTime", startDateTime, ValueTypeEnum.DateTime));

        if(result.isSucess()){
            Jedis jedis = RedisUtil.getJedis();
            try {
                if(!startDateTime.equals(null)){
                    jedis.setex("startDateTime", 864000, startDateTime);
                }
                //System.out.println("jedis startDateTime:"+jedis.get("startDateTime"));
            }finally {
                jedis.close();
            }
        }
        return loanListProcess(result,indexNum);
    }



    public static ArrayList<String> batchListInfosCollectorService(String token, ArrayList<List<Integer>> listingIdsCollector) throws Exception {
        //System.out.println("--------batchListInfosCollectorService---------");
        //String url = "https://openapi.ppdai.com/invest/LLoanInfoService/BatchListingInfos";
        String url = "https://openapi.ppdai.com/listing/openapiNoAuth/batchListingInfo";
        ArrayList<String> batchListInfosCollector = new ArrayList<>();
        //List<Integer> listingIds = new ArrayList<Integer>();
        //int num=0;
        //System.out.println(listingIdsCollector.size());
        for (List<Integer> listingIds : listingIdsCollector) {
            //System.out.println("listingIds size is " + listingIds.size());
            //System.out.println(listingIds);
            Result result = OpenApiClient.send(url, token, new PropertyObject("ListingIds", listingIds, ValueTypeEnum.Other));

            String resultJSON = FormatUtil.filterStrToJSON(result.getContext());

            if (JSONUtil.decodeUnicode(resultJSON).contains("���Ĳ���̫Ƶ����")) {
                logger.error("batchListInfosCollectorService:���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
                Thread.sleep(60000);
            }
            //System.out.println("result.context: " + result.getContext());
            batchListInfosCollector.add(resultJSON);
        }
        return batchListInfosCollector;
    }

    public static BidResult biddingService(String token, String openId, int listingId, int bidAmount) throws Exception {
        System.out.println("-------------------biddingService----------------------------");
        String useCoupon = "true";
        String url = "https://openapi.ppdai.com/invest/BidService/Bidding";
        Result result = OpenApiClient.send(url, token,
                new PropertyObject("ListingId", listingId, ValueTypeEnum.Int32),
                new PropertyObject("Amount", bidAmount, ValueTypeEnum.Double),
                new PropertyObject("UseCoupon", useCoupon, ValueTypeEnum.String));

        System.out.println("Ͷ������" + result.getContext());
        String resultJSON = FormatUtil.filterStrToJSON(result.getContext());

        if (JSONUtil.decodeUnicode(resultJSON).contains("���Ĳ���̫Ƶ����")) {
            logger.error("xxxxxx biddingService ���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ���Ӱ� ~~~xxxxxx");
            Thread.sleep(60000);
        }
        BidResult successBidResult = null;
        //System.out.println("Success? "+ bidResult.isSuccess());
        //System.out.println(String.format("���ؽ��:%s", result.isSuccess() ? bidResult : result.getErrorMessage()));
        if (JSONUtil.decodeUnicode(resultJSON).contains("����У��ʧ��")) {
            logger.error("xxxxxx Error������У��ʧ�ܣ�xxxxxx");
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("�������Ʋ�����")) {
            logger.error("xxxxxx �������Ʋ����ڣ�xxxxxx");
            AuthInfo authInfo = OpenApiClient.refreshToken(openId, token);
            token = authInfo.getRefreshToken();
            System.out.println("Refresh token = " + token);
            Result retryResult = OpenApiClient.send(url, token,
                    new PropertyObject("ListingId", listingId, ValueTypeEnum.Int32),
                    new PropertyObject("Amount", bidAmount, ValueTypeEnum.Double),
                    new PropertyObject("UseCoupon", useCoupon, ValueTypeEnum.String));
            System.out.println(String.format("���ؽ��:%s", retryResult.isSucess() ? retryResult.getContext() : retryResult.getErrorMessage()));
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("�������ظ�Ͷ��")) {
            logger.error("xxxxxx Error��" + listingId + "�������ظ�Ͷ�꣡ xxxxxx");
            //logger.error("xxxxxx Error��" + listingId + "�������ظ�Ͷ�꣡ xxxxxx");
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("���ȳ�ֵ")) {
            logger.error("Error���˻����㣬���ȳ�ֵ��");
            Thread.sleep(300000);
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("������")) {
            logger.error("xxxxxx Error��" + listingId + "�����꣡xxxxxx");
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("��Ĳ�����")) {
            logger.error("xxxxxx Error��" + listingId + "��Ĳ����ڣ�xxxxxx");
        } else if (resultJSON.contains("\"Result\":0,\"ResultMessage\":null")) {
            successBidResult = new BidResult(listingId, bidAmount);
            logger.info(resultJSON);
        }
        return successBidResult;
    }

    public static JSONArray batchListingInfosService(String token, List<Integer> listingIds) throws Exception {
        String url = "https://openapi.ppdai.com/invest/LLoanInfoService/BatchListingInfos";
        Result result = OpenApiClient.send(url, token, new PropertyObject("ListingIds", listingIds, ValueTypeEnum.Other));

        JSONArray bidInfos = new JSONArray();
        if (result.isSucess()) {
            String resultJSON = FormatUtil.filterStrToJSON(result.getContext());
            if(resultJSON.equals(null)){
                return null;
            }
            JSONObject resultObject = JSONObject.fromObject(resultJSON);
            bidInfos = resultObject.getJSONArray("LoanInfos");
        }
        return bidInfos;
    }


}
