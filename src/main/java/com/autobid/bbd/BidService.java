package com.autobid.bbd;

import com.autobid.entity.BidResult;
import com.autobid.entity.LoanListResult;
import com.autobid.util.JSONUtil;
import com.autobid.util.StringUtil;
import com.ppdai.open.core.*;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.autobid.util.TokenInit;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Richard Zeng
 * @ClassName: BidService
 * @Description: 与拍拍贷的各投标API调用服务
 * @Date 2017年10月13日 下午5:15:21
 */

@SuppressWarnings("deprecation")
public class BidService {

    private static Logger logger = Logger.getLogger(BidService.class);

    public static String queryBalanceService(String token) throws Exception {
        //请求url
        String url = "http://gw.open.ppdai.com/balance/balanceService/QueryBalance";

        Result result = OpenApiClient.send(url, token);
        System.out.println(result.getContext());
        
        String resultJSON = StringUtil.filterStrToJSON(result.getContext());
       // String resultJSON = StringUtil.toJSON(result.getContext());

        if (JSONUtil.decodeUnicode(resultJSON).contains("您的操作太频繁啦")) {
            System.out.println("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
            Thread.sleep(60000);
        }/* else if (JSONUtil.decodeUnicode(resultJSON).contains("用户无效或令牌已过有效期")) {
            logger.info("Error!用户无效或令牌已过有效期");
            //System.out.println(TokenInit.getInitFlag());
            TokenInit.initToken();
            logger.info("已重置最新令牌");
        }*/
        return result.getContext();
    }

    public static LoanListResult loanListService(int indexNum) throws Exception {
        int[] loanIds = null;
        //ArrayList<String> loanInfosList = new ArrayList<>();
        String url = "http://gw.open.ppdai.com/invest/LLoanInfoService/LoanList";
        Result result;
        String creditCode;
        result = OpenApiClient.send(url, new PropertyObject("PageIndex", indexNum, ValueTypeEnum.Int32));

        String resultJSON = StringUtil.filterStrToJSON(result.getContext());

        if (JSONUtil.decodeUnicode(resultJSON).contains("您的操作太频繁啦")) {
            logger.info("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
            logger.error("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
            Thread.sleep(60000);
        }
        JSONArray loanInfosResultArray = new JSONArray();
        if (result.isSucess()) {
            String loanListResult = resultJSON;
            //logger.info("loanListResult is :" + loanListResult);
            JSONObject loanListJson = JSONObject.fromObject(loanListResult);
            JSONArray loanInfosArray = loanListJson.getJSONArray("LoanInfos");

            //String loanListJson = loanAllList.substring(13);
            //logger.info("loanListArray is :" + loanInfosArray);   //Modified
            int size = loanInfosArray.size();
            loanIds = new int[size];
            int j = 0;
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
            System.out.println("第" + indexNum + "组标的总数为：" + loanIds.length + "，中风险标的数为：" + j);
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

    public static ArrayList<String> batchListInfosCollectorService(String token, ArrayList<List<Integer>> listingIdsCollector) throws Exception {
        //System.out.println("--------batchListInfosCollectorService---------");
        String url = "http://gw.open.ppdai.com/invest/LLoanInfoService/BatchListingInfos";
        ArrayList<String> batchListInfosCollector = new ArrayList<>();
        //List<Integer> listingIds = new ArrayList<Integer>();
        //int num=0;
        //System.out.println(listingIdsCollector.size());
        for (List<Integer> listingIds : listingIdsCollector) {
            //System.out.println("listingIds size is " + listingIds.size());
            //System.out.println(listingIds);
            Result result = OpenApiClient.send(url, token, new PropertyObject("ListingIds", listingIds, ValueTypeEnum.Other));

            String resultJSON = StringUtil.filterStrToJSON(result.getContext());

            if (JSONUtil.decodeUnicode(resultJSON).contains("您的操作太频繁啦")) {
                logger.error("您的操作太频繁啦！先喝杯茶吧，歇一分钟~~");
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
        String url = "http://gw.open.ppdai.com/invest/BidService/Bidding";
        Result result = OpenApiClient.send(url, token,
                new PropertyObject("ListingId", listingId, ValueTypeEnum.Int32),
                new PropertyObject("Amount", bidAmount, ValueTypeEnum.Double),
                new PropertyObject("UseCoupon", useCoupon, ValueTypeEnum.String));

        String resultJSON = StringUtil.filterStrToJSON(result.getContext());

        if (JSONUtil.decodeUnicode(resultJSON).contains("您的操作太频繁啦")) {
            logger.error("xxxxxx 您的操作太频繁啦！先喝杯茶吧，歇一分钟吧 ~~~xxxxxx");
            Thread.sleep(60000);
        }
        BidResult successBidResult = null;
        //System.out.println("Success? "+ bidResult.isSuccess());
        //System.out.println(String.format("返回结果:%s", result.isSuccess() ? bidResult : result.getErrorMessage()));
        if (JSONUtil.decodeUnicode(resultJSON).contains("令牌校验失败")) {
            logger.error("xxxxxx Error！令牌校验失败！xxxxxx");
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("访问令牌不存在")) {
            logger.error("xxxxxx 访问令牌不存在！xxxxxx");
            AuthInfo authInfo = OpenApiClient.refreshToken(openId, token);
            token = authInfo.getRefreshToken();
            System.out.println("Refresh token = " + token);
            Result retryResult = OpenApiClient.send(url, token,
                    new PropertyObject("ListingId", listingId, ValueTypeEnum.Int32),
                    new PropertyObject("Amount", bidAmount, ValueTypeEnum.Double),
                    new PropertyObject("UseCoupon", useCoupon, ValueTypeEnum.String));
            System.out.println(String.format("返回结果:%s", retryResult.isSucess() ? retryResult.getContext() : retryResult.getErrorMessage()));
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("不允许重复投标")) {
            logger.error("xxxxxx Error！" + listingId + "不允许重复投标！ xxxxxx");
            //logger.error("xxxxxx Error！" + listingId + "不允许重复投标！ xxxxxx");
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("请先充值")) {
            logger.error("Error！账户余额不足，请先充值！");
            Thread.sleep(300000);
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("已满标")) {
            logger.error("xxxxxx Error！" + listingId + "已满标！xxxxxx");
        } else if (JSONUtil.decodeUnicode(resultJSON).contains("标的不存在")) {
            logger.error("xxxxxx Error！" + listingId + "标的不存在！xxxxxx");
        } else if (resultJSON.contains("\"Result\":0,\"ResultMessage\":null")) {
            successBidResult = new BidResult(listingId, bidAmount);
            logger.info(resultJSON);
        }
        return successBidResult;
    }

    public static JSONArray batchListingInfosService(String token, List<Integer> listingIds) throws Exception {
        String url = "http://gw.open.ppdai.com/invest/LLoanInfoService/BatchListingInfos";
        Result result = OpenApiClient.send(url, token, new PropertyObject("ListingIds", listingIds, ValueTypeEnum.Other));

        JSONArray bidInfos = new JSONArray();
        if (result.isSucess()) {

            String resultJSON = StringUtil.filterStrToJSON(result.getContext());

            JSONObject resultObject = JSONObject.fromObject(resultJSON);
            bidInfos = resultObject.getJSONArray("LoanInfos");
        }
        return bidInfos;
    }
}
