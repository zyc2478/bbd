package com.autobid.bbd;

import com.autobid.criteria.*;
import com.autobid.entity.BidResult;
import com.autobid.entity.Constants;
import com.autobid.entity.CriteriaGroup;
import com.autobid.entity.LoanListResult;
import com.autobid.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private static ConfBean confBean;
    private static LocalConfBean localConfBean;
    private static String localHost,confHost;

    private static Logger logger = Logger.getLogger(BidManager.class);

    BidDetermine bidDetermine = new BidDetermine();

    //单例
    private volatile static BidManager instance;

    //static double balance;
    static {
        try {
            AuthInit.init();
            confBean = ConfUtil.readAllToBean();
            localConfBean = ConfUtil.readAllToLocalBean();
            MIN_BID_AMOUNT = Integer.parseInt(confBean.getMinBidAmount());
            openId = confBean.getOpenId();
            //如果TokenInit配置项不存在，则初始化Token，存储在Redis中--需要新code，没有意义
            if (ConfUtil.getProperty("init_flag").equals("0")) {
                TokenInit.initToken();
            }
            //每次运行获取一个新Token
            //TokenUtil.genNewToken();
            //如果Token快到期，则获取一个新Token
            TokenUtil tokenUtil = new TokenUtil();
            if (tokenUtil.determineRefreshDate()) {
                tokenUtil.genNewToken();
            }
/*            localHost = HostUtil.getLocalHost();
            confHost = HostUtil.getConfHost();*/
            //获取Token，配置文件有则优先，没有则获取Redis

            //logger.info("token:" + token);
            //String balanceJson = BidService.queryBalanceService(token);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String redisHost;
    {
        try {
            redisHost = ConfUtil.getProperty("redis_host");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
    public synchronized void bidExecute() throws Exception {

/*        if(localHost==confHost && ConfUtil.getProperty("init_flag").equals("1")) {
            //如果不是在本机第一次运行，则直接获取一个新Token
            TokenUtil.genNewToken();
        }*/
        TokenUtil tokenUtil = new TokenUtil();
        token = tokenUtil.getToken();
        //ConfUtil.setLocalProperty("host_name",localHost);
//        token = TokenUtil.getToken();
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
        int bbdGroups = Integer.parseInt(confBean.getBbdGroups());
        int bidByTime = Integer.parseInt(confBean.getBidByTime());
        String timeInterval = confBean.getTimeInterval();
        System.out.println("timeInterval: " + timeInterval);
        String startDateTime = DateTimeUtil.calcStartDateTime(timeInterval);
        //System.out.println("StartDate = " + startDateTime);
        String loanListFetchTime = "";
        do {
            balance = BidDataParser.getBalance(BidService.queryBalanceService(token));
            if (BidDetermine.determineBalance(balance)) {
                logger.error("余额不足，程序退出，1分钟后将再次尝试");
                Thread.sleep(60000);
                return;
            }
            LoanListResult loanListResult;
            if(bidByTime==1){
                loanListResult = BidService.loanListServiceByTime(indexNum,startDateTime);
            }else if(bidByTime==0){
                loanListResult = BidService.loanListService(indexNum);
            }else{
                logger.error("非法的bid_by_time参数");
                return;
            }
            if(indexNum==1){
                SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                loanListFetchTime = sdf.format(new Date());
            }
            loanIdCount = loanListResult.getLoanIdCount();
            System.out.println("loanIdCount：" + loanIdCount);
            //请求服务获取ListingIds
            listingIds = BidDataParser.getListingIds(loanListResult.getLoanList());
/*    		listingIds = new ArrayList<Integer>();
    		listingIds.add(116172058);
            System.out.println(listingIds);*/

            //将ListingIds切分成10个一组，再拼接成一个Collector
            //Integer[][] listingIdsParted = BidDataParser.getListingIdsParted(listingIds);
            //ArrayList<List<Integer>> listingIdsCollector = BidDataParser.getListingIdsCollector(listingIdsParted);

            ArrayList<List<Integer>> listingIdsCollector = BidDataParser.getListingIdsCollector(listingIds);

            //循环遍历，每组传入接口 BatchListingInfos,并将结果合并到Collector
            ArrayList<String> batchListInfosCollector = BidService.batchListInfosCollectorService(
                    token, listingIdsCollector);

            //将合并后的Collector处理成JSONArray数组，并再合并为新的Collector
            ArrayList<JSONArray> loanInfosCollector = BidDataParser.getLoanInfosCollector(batchListInfosCollector);

            //循环遍历得出JSONArray，将每个JSONArray再分拆为多个标的JSONObject

            for (JSONArray loanInfosArray : loanInfosCollector) {
                for (int i = 0; i < loanInfosArray.size(); i++) {
                    JSONObject loanInfos = loanInfosArray.getJSONObject(i);

                    //System.out.println(loanInfos);

                    //将得到的每个标的loanInfo解析为每个字段，将结果合并为HashMap
//                    HashMap<String, Object> loanInfoMap = BidDataParser.getLoanInfoMap(loanInfoObj);

                    int listingId = loanInfos.getInt("ListingId");
                    //运行初始策略判断
                    int basicCriteriaLevel = basicCriteria.getLevel(loanInfos, confBean);
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
                        if (!bidDetermine.determineDuplicateId(listingId)) {
                            System.out.println("====== listingId: " + listingId + ", Start bidding ====== ");
                            //logger.info("listingId: " + listingId + ", JSON is: " + loanInfoMap);
                            //bidAmount = new BidDetermine().determineCriteriaGroup(criteriaGroup, loanInfoMap);
                            //basicCriteria.printCriteria(loanInfos,confBean);              //调试入口，投过的标查看详细条件
                            assert criteriaGroup != null;
                            bidAmount = bidDetermine.determineCriteriaGroup(Objects.requireNonNull(criteriaGroup), confBean, loanInfos);
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
                        Jedis jedis = RedisUtil.getJedis();
                        try {
                            jedis.setex(String.valueOf(listingId), 864000, String.valueOf(bidAmount));
                        }finally {
                            jedis.close();
                        }
                    }
                }
            }
            //System.out.println(indexNum);
            indexNum++;
        } while ( /*indexNum ==1 */ loanIdCount == 200 && indexNum <= bbdGroups);
        System.out.println("*~~~~~~~~~~~~~~~~~~~~标的执行完毕，投标结果如下：~~~~~~~~~~~~~~~~~~~*");

        if(!loanListFetchTime.equals("") && successBidList.isEmpty()){
            //System.out.println("loanListFetchTime; " + loanListFetchTime);
            Jedis jedis = RedisUtil.getJedis();
            try {
                jedis.setex("startDateTime", 864000,loanListFetchTime);
            }finally {
                jedis.close();
            }
        }
        bidResultsPrint(successBidList, listingIds.size());
    	
/*    	if(Integer.parseInt(confBean.getBidMode())==2) {
    		DebtManager.getInstance().debtExcecute();
    	}*/
    	
/*		logger = null;
		instance = null;*/
        //ConfUtil.setProperty("host_name",localHost);
//        pool.close();
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
 