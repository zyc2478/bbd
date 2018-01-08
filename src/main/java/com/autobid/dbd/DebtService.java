package com.autobid.dbd;

import com.autobid.entity.DebtResult;
import com.autobid.util.JsonUtil;
import com.ppdai.open.core.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Richard Zeng
 * @ClassName: BidService
 * @Description: �����Ĵ��ĸ�Ͷ��API���÷���
 * @Date 2017��10��13�� ����5:15:21
 */

@SuppressWarnings("deprecation")
public class DebtService {

    private static Logger logger = Logger.getLogger(DebtService.class);


    public static JSONArray debtListService(int indexNum) throws Exception {

        String url = "http://gw.open.ppdai.com/invest/LLoanInfoService/DebtListNew";
        Result result = OpenApiClient.send(url, new PropertyObject("PageIndex", indexNum, ValueTypeEnum.Int32));
        if (JsonUtil.decodeUnicode(result.getContext()).contains("���Ĳ���̫Ƶ����")) {
            //logger.info("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
            System.out.println("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
            logger.error("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
            Thread.sleep(60000);
        }
        JSONArray debtListArray = null;
        if (result.isSucess()) {
            String debtListResult = result.getContext();
            JSONObject debtListJson = JSONObject.fromObject(debtListResult);
            //logger.info(balanceJson);
            if (debtListJson.containsKey("DebtInfos")) {
                debtListArray = debtListJson.getJSONArray("DebtInfos");
            }
        } else {
            logger.error(result.getErrorMessage());
        }
        return debtListArray;
    }

    public static JSONArray batchDebtInfosService(JSONArray diArray) throws Exception {
        String url = "https://openapi.ppdai.com/invest/LLoanInfoService/BatchDebtInfos";
        List<Integer> debtIds = new ArrayList<>();
        //System.out.println(diArray);
        for (Object aDiArray : diArray) {
            //debtIds.add(new Integer();
            debtIds.add(Integer.valueOf(((JSONObject) aDiArray).getString("DebtdealId")));
        }
        Result result = OpenApiClient.send(url, new PropertyObject("DebtIds", debtIds, ValueTypeEnum.Other));
        JSONArray debtInfosArray = null;
        if (JsonUtil.decodeUnicode(result.getContext()).contains("���Ĳ���̫Ƶ����")) {
            logger.info("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
            logger.error("���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ����~~");
            System.out.println("result.getContext()" + result.getContext());
            Thread.sleep(60000);
        }
        if (result.isSucess()) {
            String batchDebtInfosResult = result.getContext();

            JSONObject batchDebtInfosJson = JSONObject.fromObject(batchDebtInfosResult);

            //logger.info(batchDebtInfosJson);

            debtInfosArray = batchDebtInfosJson.getJSONArray("DebtInfos");
            //logger.info(debtInfosArray);
        } else {
            logger.error(result.getErrorMessage());
        }
        return debtInfosArray;
    }

    public static DebtResult buyDebtService(String token, String openId, JSONObject debtInfo) throws Exception {
        System.out.println("-------------------buyDebtService----------------------------");
        String url = "https://openapi.ppdai.com/invest/BidService/BuyDebt";
        int debtId = debtInfo.getInt("DebtId");
        System.out.println("=====================��ʼͶ�� " + debtId + "======================");
        Result result = OpenApiClient.send(url, token,
                new PropertyObject("debtDealId", debtId, ValueTypeEnum.Int32));
        String debtResult = result.getContext();
        logger.info(debtResult);

        if (JsonUtil.decodeUnicode(debtResult).contains("���Ĳ���̫Ƶ����")) {
            logger.error("xxxxxx ���Ĳ���̫Ƶ�������Ⱥȱ���ɣ�Ъһ���Ӱ� ~~~xxxxxx");
            Thread.sleep(60000);
        }
        DebtResult successBidResult = null;
        //System.out.println("Success? "+ bidResult.isSuccess());
        //System.out.println(String.format("���ؽ��:%s", result.isSuccess() ? bidResult : result.getErrorMessage()));
        if (JsonUtil.decodeUnicode(debtResult).contains("����У��ʧ��")) {
            logger.error("xxxxxx Error������У��ʧ�ܣ�xxxxxx");
        } else if (debtResult.contains("\"Result\":0,\"ResultMessage\":��ϲ��")) {
            successBidResult = new DebtResult(debtId, debtInfo.getInt("ListingId"), debtInfo.getDouble("PriceforSale"));
            logger.info(debtResult);
        } else if (JsonUtil.decodeUnicode(debtResult).contains("�ѷ������Ʋ�����")) {
            logger.error("xxxxxx �������Ʋ����� ��xxxxxx");
            AuthInfo authInfo = OpenApiClient.refreshToken(openId, token);
            token = authInfo.getRefreshToken();
            System.out.println("Refresh token = " + token);
            Result retryResult = OpenApiClient.send(url, token,
                    new PropertyObject("debtDealId", debtId, ValueTypeEnum.Int32));
            System.out.println(String.format("���ؽ��:%s", retryResult.isSucess() ? retryResult.getContext() : retryResult.getErrorMessage()));
        } else if (JsonUtil.decodeUnicode(debtResult).contains("�������ظ�Ͷ��")) {
            logger.error("xxxxxx Error��" + debtId + "�������ظ�Ͷ�꣡ xxxxxx");
            //logger.error("xxxxxx Error��" + listingId + "�������ظ�Ͷ�꣡ xxxxxx");
        } else if (JsonUtil.decodeUnicode(debtResult).contains("���ȳ�ֵ")) {
            logger.error("Error���˻����㣬���ȳ�ֵ��");
            Thread.sleep(300000);
        } else if (JsonUtil.decodeUnicode(debtResult).contains("������")) {
            logger.error("xxxxxx Error��" + debtId + "�����꣡xxxxxx");
        } else if (JsonUtil.decodeUnicode(debtResult).contains("��Ĳ�����")) {
            logger.error("xxxxxx Error��" + debtId + "��Ĳ����ڣ�xxxxxx");
        }

        return successBidResult;
    }

}
