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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * @author Richard Zeng
 * @version 1.0
 * @ClassName: BidManager
 * @Description: �Զ�Ͷ���������
 * @Date 2017��10��13�� ����5:14:02
 */

@SuppressWarnings("deprecation")
public class BidManagerbak implements Constants {

    //private static final int NONE = 0;
    private static int MIN_BID_AMOUNT;

    private static String token = "";
    private static String openId;
    private static Jedis jedis;
    private static ConfBean confBean;

    private static Logger logger = Logger.getLogger(BidManagerbak.class);

    //����
    private volatile static BidManagerbak instance;

    //static double balance;
    static {
        try {
            AuthInit.init();
            confBean = ConfUtil.readAllToBean();
            MIN_BID_AMOUNT = Integer.parseInt(confBean.getMinBidAmount());
            openId = confBean.getOpenId();
            String redisHost = confBean.getRedisHost();
            int redisPort = Integer.parseInt(confBean.getRedisPort());

//            jedis = new Jedis(redisHost, redisPort);
            jedis = RedisUtil.getJedis();
            //���TokenInit��������ڣ����ʼ��Token���洢��Redis��--��Ҫ��code��û������
            if (ConfUtil.getProperty("init_flag").equals("0")) {
                TokenInit.initToken();
            }
            //ÿ�����л�ȡһ����Token
            //TokenUtil.genNewToken();
            //���Token�쵽�ڣ����ȡһ����Token
            if (TokenUtil.determineRefreshDate()) {
                TokenUtil.genNewToken();
            }
            //��ȡToken�������ļ��������ȣ�û�����ȡRedis

            //logger.info("token:" + token);
            //String balanceJson = BidService.queryBalanceService(token);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //private BidByDebt(){}
    public BidManagerbak() {
    }
    //HashMap<Integer,String> bidResultMap = new HashMap<Integer,String>();
    //int bidAmount = 0;

    public static BidManagerbak getInstance() {
        if (instance == null) {
            synchronized (BidManagerbak.class) {
                if (instance == null) {
                    instance = new BidManagerbak();
                }
            }
        }
        return instance;
    }

    @Test
    public void bidExecute() throws Exception {
        //ÿ�����л�ȡһ����Token
//        TokenUtil.genNewToken();
        token = TokenUtil.getToken();
        logger.info("bidExecute");
        double balance = BidDataParser.getBalance(BidService.queryBalanceService(token));

        if (BidDetermine.determineBalance(balance)) {
            logger.error("���㣬�����˳���1���Ӻ��ٴγ���");
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
                logger.error("���㣬�����˳���1���Ӻ��ٴγ���");
                Thread.sleep(60000);
                return;
            }
            LoanListResult loanListResult = BidService.loanListService(indexNum);
            loanIdCount = loanListResult.getLoanIdCount();
            //��������ȡListingIds
            listingIds = BidDataParser.getListingIds(loanListResult.getLoanList());
/*    		listingIds = new ArrayList<Integer>();
    		listingIds.add(86084296);*/
            //System.out.println(listingIds);

            //��ListingIds�зֳ�10��һ�飬��ƴ�ӳ�һ��Collector
            //Integer[][] listingIdsParted = BidDataParser.getListingIdsParted(listingIds);
            //ArrayList<List<Integer>> listingIdsCollector = BidDataParser.getListingIdsCollector(listingIdsParted);

            ArrayList<List<Integer>> listingIdsCollector = BidDataParser.getListingIdsCollector(listingIds);

            //System.out.println(listingIdsCollector);

            //ѭ��������ÿ�鴫��ӿ� BatchListingInfos,��������ϲ���Collector
            ArrayList<String> batchListInfosCollector = BidService.batchListInfosCollectorService(
                    token, listingIdsCollector);
            //System.out.println(batchListInfosCollector);


            //���ϲ����Collector�����JSONArray���飬���ٺϲ�Ϊ�µ�Collector
            ArrayList<JSONArray> loanInfosCollector = BidDataParser.getLoanInfosCollector(batchListInfosCollector);

            //System.out.println(loanInfosCollector);

            //ѭ�������ó�JSONArray����ÿ��JSONArray�ٷֲ�Ϊ������JSONObject

            for (JSONArray loanInfosArray : loanInfosCollector) {
                for (int i = 0; i < loanInfosArray.size(); i++) {
                    JSONObject loanInfoObj = loanInfosArray.getJSONObject(i);

                    //System.out.println(loanInfoObj);

                    //���õ���ÿ�����loanInfo����Ϊÿ���ֶΣ�������ϲ�ΪHashMap
                    HashMap<String, Object> loanInfoMap = BidDataParser.getLoanInfoMap(loanInfoObj);

                    int listingId = loanInfoObj.getInt("ListingId");
                    //���г�ʼ�����ж�
                    int basicCriteriaLevel = basicCriteria.getLevel(loanInfoMap, confBean);
                    //new BasicCriteria().printCriteria(loanInfoMap);
                    //System.out.println("basicCriteriaLevel is :" + basicCriteriaLevel);

                    //���ݳ�ʼ���Է��ز�ͬ��ѡ��ͬ�Ĳ�����
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
                    //����������Բ�ƥ�䣬���ж�������ӦͶ���
                    if (basicCriteriaLevel > 0) {

                        //���ж��Ƿ���Redis���ظ�������Ͷ���ñ��
                        //bidAmount = new BidDetermine().determineCriteriaGroup(criteriaGroup, loanInfoMap);
                        if (!BidDetermine.determineDuplicateId(listingId, jedis)) {
                            System.out.println("====== listingId: " + listingId + ", Start bidding ====== ");
                            //logger.info("listingId: " + listingId + ", JSON is: " + loanInfoMap);
                            //bidAmount = new BidDetermine().determineCriteriaGroup(criteriaGroup, loanInfoMap);
                            assert criteriaGroup != null;
                            bidAmount = BidDetermine.determineCriteriaGroup(Objects.requireNonNull(criteriaGroup), confBean, loanInfoMap);
                            System.out.println("****** listingId: " + listingId + ", total Amount is: " + bidAmount + " ******");
                        } /*else {//logger.error("xxxxxx " + listingId + "��Redis���ظ��� xxxxxx");}*/
                    }
                    if (bidAmount != 0) {
                        if (balance > bidAmount) {
                            //logger.info("�ñ�Ŀ�Ͷ");
                            successBidResult = BidService.biddingService(token, openId, listingId, bidAmount);
                        } else if (balance > MIN_BID_AMOUNT) {
                            logger.info("�ñ�ֻ��Ͷ" + MIN_BID_AMOUNT);
                            successBidResult = BidService.biddingService(token, openId, listingId, MIN_BID_AMOUNT);
                        } else {
                            logger.error("����,��ȴ�5���Ӻ�����");
                            Thread.sleep(300000);
                        }
                    }
                    if (successBidResult != null) {
                        //logger.info(listingId+" will be bidden~~~~~~~~~~bidAmount is��"+bidAmount);
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
        System.out.println("*~~~~~~~~~~~~~~~~~~~~���ִ����ϣ�Ͷ�������£�~~~~~~~~~~~~~~~~~~~*");

        bidResultsPrint(successBidList, listingIds.size());
    	
/*    	if(Integer.parseInt(confBean.getBidMode())==2) {
    		DebtManager.getInstance().debtExcecute();
    	}*/
    	
/*		logger = null;
		instance = null;*/
    }

    private void bidResultsPrint(ArrayList<BidResult> successBidList, int listingIdsSize) {
        if (listingIdsSize == 0) {
            System.out.println("*~~~~~~~~~~~~~~~~~~~~~�ܱ�Ǹ��û�п�Ͷ���~~~~~~~~~~~~~~~~~~~~~~~~~*");
        } else if (successBidList.isEmpty()) {
            System.out.println("*~~~~~~~~~~~~~~~~~~~�ܱ�Ǹ��û���ҵ����ʱ��~~~~~~~~~~~~~~~~~~~~~~~*");
        } else {

            for (BidResult successResult : successBidList) {
                System.out.println("*~~~~~~~~~~~ BidId:" + successResult.getBidId() + ",BidAmount:" +
                        successResult.getBidAmount() + "~~~~~~~~~~~*");
            }
        }
    }
}
 