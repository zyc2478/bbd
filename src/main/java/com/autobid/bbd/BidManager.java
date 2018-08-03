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
 * @Description: �Զ�Ͷ���������
 * @Date 2017��10��13�� ����5:14:02
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

    //����
    private volatile static BidManager instance;

    //static double balance;
    static {
        try {
            AuthInit.init();
            confBean = ConfUtil.readAllToBean();
            localConfBean = ConfUtil.readAllToLocalBean();
            MIN_BID_AMOUNT = Integer.parseInt(confBean.getMinBidAmount());
            openId = confBean.getOpenId();
            //���TokenInit��������ڣ����ʼ��Token���洢��Redis��--��Ҫ��code��û������
            if (ConfUtil.getProperty("init_flag").equals("0")) {
                TokenInit.initToken();
            }
            //ÿ�����л�ȡһ����Token
            //TokenUtil.genNewToken();
            //���Token�쵽�ڣ����ȡһ����Token
            TokenUtil tokenUtil = new TokenUtil();
            if (tokenUtil.determineRefreshDate()) {
                tokenUtil.genNewToken();
            }
/*            localHost = HostUtil.getLocalHost();
            confHost = HostUtil.getConfHost();*/
            //��ȡToken�������ļ��������ȣ�û�����ȡRedis

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
            //��������ڱ�����һ�����У���ֱ�ӻ�ȡһ����Token
            TokenUtil.genNewToken();
        }*/
        TokenUtil tokenUtil = new TokenUtil();
        token = tokenUtil.getToken();
        //ConfUtil.setLocalProperty("host_name",localHost);
//        token = TokenUtil.getToken();
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
                logger.error("���㣬�����˳���1���Ӻ��ٴγ���");
                Thread.sleep(60000);
                return;
            }
            LoanListResult loanListResult;
            if(bidByTime==1){
                loanListResult = BidService.loanListServiceByTime(indexNum,startDateTime);
            }else if(bidByTime==0){
                loanListResult = BidService.loanListService(indexNum);
            }else{
                logger.error("�Ƿ���bid_by_time����");
                return;
            }
            if(indexNum==1){
                SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                loanListFetchTime = sdf.format(new Date());
            }
            loanIdCount = loanListResult.getLoanIdCount();
            System.out.println("loanIdCount��" + loanIdCount);
            //��������ȡListingIds
            listingIds = BidDataParser.getListingIds(loanListResult.getLoanList());
/*    		listingIds = new ArrayList<Integer>();
    		listingIds.add(116172058);
            System.out.println(listingIds);*/

            //��ListingIds�зֳ�10��һ�飬��ƴ�ӳ�һ��Collector
            //Integer[][] listingIdsParted = BidDataParser.getListingIdsParted(listingIds);
            //ArrayList<List<Integer>> listingIdsCollector = BidDataParser.getListingIdsCollector(listingIdsParted);

            ArrayList<List<Integer>> listingIdsCollector = BidDataParser.getListingIdsCollector(listingIds);

            //ѭ��������ÿ�鴫��ӿ� BatchListingInfos,��������ϲ���Collector
            ArrayList<String> batchListInfosCollector = BidService.batchListInfosCollectorService(
                    token, listingIdsCollector);

            //���ϲ����Collector�����JSONArray���飬���ٺϲ�Ϊ�µ�Collector
            ArrayList<JSONArray> loanInfosCollector = BidDataParser.getLoanInfosCollector(batchListInfosCollector);

            //ѭ�������ó�JSONArray����ÿ��JSONArray�ٷֲ�Ϊ������JSONObject

            for (JSONArray loanInfosArray : loanInfosCollector) {
                for (int i = 0; i < loanInfosArray.size(); i++) {
                    JSONObject loanInfos = loanInfosArray.getJSONObject(i);

                    //System.out.println(loanInfos);

                    //���õ���ÿ�����loanInfo����Ϊÿ���ֶΣ�������ϲ�ΪHashMap
//                    HashMap<String, Object> loanInfoMap = BidDataParser.getLoanInfoMap(loanInfoObj);

                    int listingId = loanInfos.getInt("ListingId");
                    //���г�ʼ�����ж�
                    int basicCriteriaLevel = basicCriteria.getLevel(loanInfos, confBean);
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
                        if (!bidDetermine.determineDuplicateId(listingId)) {
                            System.out.println("====== listingId: " + listingId + ", Start bidding ====== ");
                            //logger.info("listingId: " + listingId + ", JSON is: " + loanInfoMap);
                            //bidAmount = new BidDetermine().determineCriteriaGroup(criteriaGroup, loanInfoMap);
                            //basicCriteria.printCriteria(loanInfos,confBean);              //������ڣ�Ͷ���ı�鿴��ϸ����
                            assert criteriaGroup != null;
                            bidAmount = bidDetermine.determineCriteriaGroup(Objects.requireNonNull(criteriaGroup), confBean, loanInfos);
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
        System.out.println("*~~~~~~~~~~~~~~~~~~~~���ִ����ϣ�Ͷ�������£�~~~~~~~~~~~~~~~~~~~*");

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
 