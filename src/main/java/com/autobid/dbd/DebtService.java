package com.autobid.dbd;

import com.autobid.entity.DebtResult;
import com.autobid.util.FormatUtil;
import com.autobid.util.JSONUtil;
import com.ppdai.open.core.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Richard Zeng
 * @ClassName: DebtService
 * @Description: 与拍拍贷的债转标API调用服务
 * @Date 2018年2月14日 下午5:15:21
 */

@SuppressWarnings("deprecation")
public class DebtService {

    private static Logger logger = Logger.getLogger(DebtService.class);

    public static JSONArray debtListService(int indexNum) throws Exception {

        String url = "http://gw.open.ppdai.com/invest/LLoanInfoService/DebtListNew";
        Result result = OpenApiClient.send(url, new PropertyObject("PageIndex", indexNum, ValueTypeEnum.Int32));

        String resultJSON = FormatUtil.filterStrToJSON(result.getContext());

        if (JSONUtil.decodeUnicode(resultJSON).contains("您的操作太频繁啦")) {
            //logger.info("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
            System.out.println("debtListService:您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
            logger.error("debtListService:您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
            Thread.sleep(60000);
        }
        JSONArray debtListArray = null;
        if (result.isSucess()) {
            String debtListResult = resultJSON;
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

//        System.out.println(result.getContext());
        String resultJSON = FormatUtil.filterStrToJSON(result.getContext());

        JSONArray debtInfosArray = null;
        if (JSONUtil.decodeUnicode(resultJSON).contains("您的操作太频繁啦")) {
            logger.info("batchDebtInfosService: 您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
            logger.error("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
            System.out.println("result.getContext()" + result.getContext());
            Thread.sleep(60000);
        }
        if (result.isSucess()) {
            String batchDebtInfosResult = resultJSON;
            JSONObject batchDebtInfosJson = JSONObject.fromObject(FormatUtil.filterStrToJSON(batchDebtInfosResult));
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
        System.out.println("=====================开始投标 " + debtId + "======================");
        Result result = OpenApiClient.send(url, token,
                new PropertyObject("debtDealId", debtId, ValueTypeEnum.Int32));

        String resultJSON = FormatUtil.filterStrToJSON(result.getContext());

        logger.info(resultJSON);

        if (JSONUtil.decodeUnicode(resultJSON).contains("您的操作太频繁啦")) {
            logger.error("xxxxxx buyDebtService:您的操作太频繁啦！先喝杯茶吧，歇一分钟吧 ~~~xxxxxx");
            Thread.sleep(60000);
        }
        DebtResult successBidResult = null;
        //System.out.println("Success? "+ bidResult.isSuccess());
        //System.out.println(String.format("返回结果:%s", result.isSuccess() ? bidResult : result.getErrorMessage()));
        if (JSONUtil.decodeUnicode(resultJSON).contains("令牌校验失败")) {
            logger.error("xxxxxx Error！令牌校验失败！xxxxxx");
        } else if (resultJSON.contains("\"Result\":0,\"ResultMessage\":恭喜您")) {
            successBidResult = new DebtResult(debtId, debtInfo.getInt("ListingId"), debtInfo.getDouble("PriceforSale"));
            logger.info(resultJSON);
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("已访问令牌不存在")) {
            logger.error("xxxxxx 访问令牌不存在 ！xxxxxx");
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("不允许重复投标")) {
            logger.error("xxxxxx Error！" + debtId + "不允许重复投标！ xxxxxx");
            //logger.error("xxxxxx Error！" + listingId + "不允许重复投标！ xxxxxx");
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("请先充值")) {
            logger.error("Error！账户余额不足，请先充值！");
            Thread.sleep(300000);
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("已满标")) {
            logger.error("xxxxxx Error！" + debtId + "已满标！xxxxxx");
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("标的不存在")) {
            logger.error("xxxxxx Error！" + debtId + "标的不存在！xxxxxx");
        }
        return successBidResult;
    }

}
