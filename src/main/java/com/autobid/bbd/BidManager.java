package com.autobid.bbd;

import com.autobid.criteria.*;
import com.autobid.entity.BidResult;
import com.autobid.entity.Constants;
import com.autobid.entity.CriteriaGroup;
import com.autobid.entity.LoanListResult;
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;
import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.*;


/**
 * @author Richard Zeng
 * @version 1.0
 * @ClassName: BidManager
 * @Description: 自动投标的主程序
 * @Date 2017年10月13日 下午5:14:02
 */

@SuppressWarnings("deprecation")
public class BidManager implements Constants {

    //private static final int NONE = 0;
    private static int MIN_BID_AMOUNT;

    private static String token = "";
    private static String openId;
    private static Jedis jedis;
    private static ConfBean confBean;

    private static Logger logger = Logger.getLogger(BidManager.class);

    //单例
    private volatile static BidManager instance;

    //static double balance;
    static {
        try {
            AuthInit.init();
            confBean = ConfUtil.readAllToBean();
            MIN_BID_AMOUNT = Integer.parseInt(confBean.getMinBidAmount());
            openId = confBean.getOpenId();
            String redisHost = confBean.getRedisHost();
            int redisPort = Integer.parseInt(confBean.getRedisPort());

            jedis = new Jedis(redisHost, redisPort);

            //如果TokenInit配置项不存在，则初始化Token，存储在Redis中
            if (TokenUtil.determineTokenInitExists()) {
                TokenInit.initToken();
            }
            //如果Token快到期，则获取一个新Token
            if (TokenUtil.determineRefreshDate()) {
                TokenUtil.genNewToken();
            }
            //获取Token，配置文件有则优先，没有则获取Redis
            token = TokenUtil.getToken();
            //logger.info("token:" + token);
            //String balanceJson = BidService.queryBalanceService(token);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //private BidByDebt(){}
    public BidManager() {
    }
    //HashMap<Integer,String> bidResultMap = new HashMap<Integer,String>();
    //int bidAmount = 0;

    public static BidManager getInstance() {
        if (instance == null) {
            synchronized (BidManager.class) {
                if (instance == null) {
                    instance = new BidManager();
                }
            }
        }
        return instance;
    }

    @Test
    public void bidExecute() throws Exception {
        logger.info("bidExecute");
        double balance = BidDataParser.getBalance(BidService.queryBalanceService(token));

        if (BidDetermine.determineBalance(balance)) {
            logger.error("余额不足，程序退出，1分钟后将再次尝试");
            Thread.sleep(60000);
            return;
        }
        ArrayList<BidResult> successBidList = new ArrayList<>();
        int indexNum = 1;
        int loanIdCount;
        List<Integer> listingIds;
        BasicCriteria basicCriteria = new BasicCriteria();
        do {
            balance = BidDataParser.getBalance(BidService.queryBalanceService(token));
            if (BidDetermine.determineBalance(balance)) {
                logger.error("余额不足，程序退出，1分钟后将再次尝试");
                Thread.sleep(60000);
                return;
            }
            LoanListResult loanListResult = BidService.loanListService(indexNum);
            loanIdCount = loanListResult.getLoanIdCount();
            //请求服务获取ListingIds
            listingIds = BidDataParser.getListingIds(loanListResult.getLoanList());
/*    		listingIds = new ArrayList<Integer>();
    		listingIds.add(86084296);*/
            //System.out.println(listingIds);

            //将ListingIds切分成10个一组，再拼接成一个Collector
            //Integer[][] listingIdsParted = BidDataParser.getListingIdsParted(listingIds);
            //ArrayList<List<Integer>> listingIdsCollector = BidDataParser.getListingIdsCollector(listingIdsParted);

            ArrayList<List<Integer>> listingIdsCollector = BidDataParser.getListingIdsCollector(listingIds);

            //System.out.println(listingIdsCollector);

            //循环遍历，每组传入接口 BatchListingInfos,并将结果合并到Collector
            ArrayList<String> batchListInfosCollector = BidService.batchListInfosCollectorService(
                    token, listingIdsCollector);
            //System.out.println(batchListInfosCollector);


            //将合并后的Collector处理成JSONArray数组，并再合并为新的Collector
            ArrayList<JSONArray> loanInfosCollector = BidDataParser.getLoanInfosCollector(batchListInfosCollector);

            //System.out.println(loanInfosCollector);

            //循环遍历得出JSONArray，将每个JSONArray再分拆为多个标的JSONObject

            for (JSONArray loanInfosArray : loanInfosCollector) {
                for (int i = 0; i < loanInfosArray.size(); i++) {
                    JSONObject loanInfoObj = loanInfosArray.getJSONObject(i);

                    //System.out.println(loanInfoObj);

                    //将得到的每个标的loanInfo解析为每个字段，将结果合并为HashMap
                    HashMap<String, Object> loanInfoMap = BidDataParser.getLoanInfoMap(loanInfoObj);

                    int listingId = loanInfoObj.getInt("ListingId");
                    //运行初始策略判断
                    int basicCriteriaLevel = basicCriteria.getLevel(loanInfoMap, confBean);
                    //new BasicCriteria().printCriteria(loanInfoMap);
                    //System.out.println("basicCriteriaLevel is :" + basicCriteriaLevel);

                    //根据初始策略返回不同，选择不同的策略组
                    CriteriaGroup criteriaGroup;
                    switch (basicCriteriaLevel) {
                        case PERFECT:
                            criteriaGroup = new EduDebtCriteriaGroup();
                            break;
                        case GOOD:
                            criteriaGroup = new EduCriteriaGroup();
                            break;
                        case OK:
                            criteriaGroup = new DebtRateCriteriaGroup();
                            break;
                        case SOSO:
                            criteriaGroup = new BeginCriteriaGroup();
                            break;
                        default:
                            criteriaGroup = null;
                            break;
                    }

                    BidResult successBidResult = null;
                    int bidAmount = 0;
                    //如果基础策略不匹配，则将判定策略组应投金额
                    if (basicCriteriaLevel > 0) {

                        //先判定是否在Redis中重复，即已投过该标的
                        //bidAmount = new BidDetermine().determineCriteriaGroup(criteriaGroup, loanInfoMap);
                        if (!BidDetermine.determineDuplicateId(listingId, jedis)) {
                            System.out.println("====== listingId: " + listingId + ", Start bidding ====== ");
                            //logger.info("listingId: " + listingId + ", JSON is: " + loanInfoMap);
                            //bidAmount = new BidDetermine().determineCriteriaGroup(criteriaGroup, loanInfoMap);
                            assert criteriaGroup != null;
                            bidAmount = BidDetermine.determineCriteriaGroup(Objects.requireNonNull(criteriaGroup), confBean, loanInfoMap);
                            System.out.println("****** listingId: " + listingId + ", total Amount is: " + bidAmount + " ******");
                        } /*else {//logger.error("xxxxxx " + listingId + "在Redis中重复！ xxxxxx");}*/
                    }
                    if (bidAmount != 0) {
                        if (balance > bidAmount) {
                            //logger.info("该标的可投");
                            successBidResult = BidService.biddingService(token, openId, listingId, bidAmount);
                        } else if (balance > MIN_BID_AMOUNT) {
                            logger.info("该标只能投" + MIN_BID_AMOUNT);
                            successBidResult = BidService.biddingService(token, openId, listingId, MIN_BID_AMOUNT);
                        } else {
                            logger.error("余额不足,请等待5分钟后再试");
                            Thread.sleep(300000);
                        }
                    }
                    if (successBidResult != null) {
                        //logger.info(listingId+" will be bidden~~~~~~~~~~bidAmount is："+bidAmount);
                        if (!successBidList.contains(successBidResult)) {
                            successBidList.add(successBidResult);
                        }
                        jedis.setex(String.valueOf(listingId), 172800, String.valueOf(bidAmount));
                    }
                }
            }
            //System.out.println(indexNum);
            indexNum++;
        } while (loanIdCount == 200);
        System.out.println("*~~~~~~~~~~~~~~~~~~~~标的执行完毕，投标结果如下：~~~~~~~~~~~~~~~~~~~*");

        bidResultsPrint(successBidList, listingIds.size());
    	
/*    	if(Integer.parseInt(confBean.getBidMode())==2) {
    		DebtManager.getInstance().debtExcecute();
    	}*/
    	
/*		logger = null;
		instance = null;*/
    }

    private void bidResultsPrint(ArrayList<BidResult> successBidList, int listingIdsSize) {
        if (listingIdsSize == 0) {
            System.out.println("*~~~~~~~~~~~~~~~~~~~~~很抱歉，没有可投标的~~~~~~~~~~~~~~~~~~~~~~~~~*");
        } else if (successBidList.isEmpty()) {
            System.out.println("*~~~~~~~~~~~~~~~~~~~很抱歉，没有找到合适标的~~~~~~~~~~~~~~~~~~~~~~~*");
        } else {

            for (BidResult successResult : successBidList) {
                System.out.println("*~~~~~~~~~~~ BidId:" + successResult.getBidId() + ",BidAmount:" +
                        successResult.getBidAmount() + "~~~~~~~~~~~*");
            }
        }
    }
}
 