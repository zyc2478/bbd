package com.autobid.dbd;

import com.autobid.bbd.AuthInit;
import com.autobid.bbd.BidDataParser;
import com.autobid.bbd.BidDetermine;
import com.autobid.bbd.BidService;
import com.autobid.entity.Constants;
import com.autobid.entity.DebtResult;
import com.autobid.filter.BidInfosFilter;
import com.autobid.filter.DebtInfosListFilter;
import com.autobid.filter.DebtListFilter;
import com.autobid.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author Richard Zeng
 * @Version 2.4
 * @ClassName: DebtManager
 * @Description: �Զ�ͶծȨ���������
 * @Date 2018��2��14�� ����0:37:15
 */

@SuppressWarnings("deprecation")
public class DebtManager implements Constants {

    private static String token = "";
    private static String openId;
    private static ConfBean confBean;
    private static String localHost,confHost;
    private static Logger logger = Logger.getLogger(DebtManager.class);
    private DebtDetermine debtDetermine = new DebtDetermine();
    //����
    private volatile static DebtManager instance;

    //static double balance;
    static {
        try {
            AuthInit.init();
            confBean = ConfUtil.readAllToBean();
            openId = confBean.getOpenId();

            //���init_flag��������ڣ����ʼ��Token���洢��Redis��
            if (ConfUtil.getProperty("init_flag").equals("0")) {
                TokenInit.initToken();
            }
            //���Token�쵽�ڣ����ȡһ����Token
            TokenUtil tokenUtil = new TokenUtil();
            if (tokenUtil.determineRefreshDate()) {
                tokenUtil.genNewToken();
            }
            localHost = HostUtil.getLocalHost();
            confHost = HostUtil.getConfHost();
/*            TokenUtil.genNewToken();
            //��ȡToken�������ļ��������ȣ�û�����ȡRedis
            token = TokenUtil.getToken();*/
            //logger.info("token:" + token);
            //String balanceJson = BidService.queryBalanceService(token);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String host;
    {
        try {
            host = ConfUtil.getProperty("redis_host");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    JedisPool pool = new JedisPool(RedisUtil.getPoolConfig(), host);
    private ArrayList<DebtResult> successDebtList = new ArrayList<>();
    public DebtManager() {
    }
    //HashMap<Integer,String> bidResultMap = new HashMap<Integer,String>();
    //int bidAmount = 0;

    public static DebtManager getInstance() {
        if (instance == null) {
            synchronized (DebtManager.class) {
                if (instance == null) {
                    instance = new DebtManager();
                }
            }
        }
        return instance;
    }

    @Test
    public synchronized void debtExcecute() throws Exception {
/*        if(localHost==confHost && ConfUtil.getProperty("init_flag").equals("1")) {
            //��������ڱ�����һ�����У���ֱ�ӻ�ȡһ����Token
            TokenUtil.genNewToken();
        }*/
        TokenUtil tokenUtil = new TokenUtil();
        token = tokenUtil.getToken();
        //ConfUtil.setProperty("host_name",localHost);
/*        TokenUtil.genNewToken();
        //��ȡToken�������ļ��������ȣ�û�����ȡRedis
        token = TokenUtil.getToken();*/
//        logger.info("debtExcecute");
        int overdueSwitch = Integer.parseInt(confBean.getDebtOverdueSwitch());
        int debtMix = Integer.parseInt(confBean.getDebtMix());

        if (overdueSwitch == 0) {
            debtNoOverdueExecute();
        } else {
            if (debtMix == 1) {
                debtOverdueExecute();
            } else {
                debtNoOverdueExecute();
                debtOverdueExecute();
            }
        }
/*		logger = null;
		instance = null;*/
    }

    private void debtNoOverdueExecute() throws Exception {
        logger.info("debtNoOverdueExecute");
        execute();
    }

    private void debtOverdueExecute() throws Exception {
        logger.info("debtOverdueExcecute");
        execute();
    }

    private void execute() throws Exception {
        double balance = BidDataParser.getBalance(BidService.queryBalanceService(token));
        if (BidDetermine.determineBalance(balance)) {
            logger.error("���㣬�����˳���1���Ӻ��ٴγ���");
            Thread.sleep(60000);
            return;
        }
        int indexNum = 1;
        int debtCount;
        //int debtFCount = 0;
        int totalDebtCount = 0;
        DebtListFilter dlf = new DebtListFilter();
        DebtInfosListFilter dilf = new DebtInfosListFilter();
        BidInfosFilter bif = new BidInfosFilter();
        int debtGroups = Integer.parseInt(confBean.getDebtGroups());
        do {
            System.out.println("debtOverdueExecute do cycle");
            balance = BidDataParser.getBalance(BidService.queryBalanceService(token));
            if(BidDetermine.determineBalance(balance)) {
                logger.error("���㣬�����˳���1���Ӻ��ٴγ���");
                Thread.sleep(60000);
                return;
            }
            JSONArray debtListArray = DebtService.debtListService(indexNum);
            if(debtListArray.equals(null)){
                debtCount = 0;
            }else{
                debtCount = debtListArray.size();
            }
            System.out.println("debtCount��" + debtCount);
            totalDebtCount += debtCount;
            //����ȡ��debtList����DebtListFilter�ж���Ĺ������
            //logger.info("��"+indexNum+"�� �����±�ģ�");
/*			for(int i=0;i<debtListArray.size();i++) {
				JSONObject debtListObject = debtListArray.getJSONObject(i);
				//logger.info(debtListObject);	
			}
			*/
            JSONArray dlFiltered = dlf.filter(debtListArray, confBean);

//            logger.info("��" + indexNum + "�� �������˺�dlFiltered������" + dlFiltered.size());
            //debtFCount += dlFiltered.size();

            //System.out.println("debtFCount "+ indexNum + " is: "+ debtFCount);

            //��debtList�з�Ϊ10��һ��,��ƴ�ӳ�һ��Collector
            ArrayList<JSONArray> daList = DebtDataParser.getDebtsCollector(dlFiltered);

            for (JSONArray aDaList : daList) {
                //��ȡծȨ�����ϸ
                //logger.info(daList.get(i));

                JSONArray debtInfosList = DebtService.batchDebtInfosService(aDaList);

                if(debtInfosList.equals(null)){
                    continue;
                }
				
/*				System.out.println(debtInfosList.size());
				for(int m=0;m<debtInfosList.size();m++) {
					logger.info(debtInfosList.getJSONObject(m));
				}
				*/
                //ͨ����ծȨ��ϸ���ݷ�����ѡ�����Ͷ��ծȨ��
                JSONArray dFiltered = dilf.filter(debtInfosList, confBean);

                //System.out.println("��"+indexNum+"�� �� "+i+"�� dFiltered.size =  ��" + dFiltered.size());

                //��ծȨ������ɸѡ����ListingId��List,�ٵ��÷����ѯ��Щ�����ϸ��Ϣ
                List<Integer> listingIds = DebtDataParser.getListingIds(dFiltered);
				
/*				listingIds = new ArrayList<Integer>();
				listingIds.add(55476937);*/

                JSONArray batchBidInfos = BidService.batchListingInfosService(token, listingIds);

                //ͨ����ծȨ��Ӧ������ݷ�������ѡ����Ͷ�ı�
                JSONArray bidFiltered = bif.filter(batchBidInfos, confBean);
                //System.out.println("��"+indexNum+"�� �� "+i+"�� bidFiltered.size = ��" + bidFiltered.size());

                //��֮ת��Ϊ��Ͷ��ծȨ��
                JSONArray dbFiltered = DebtDataParser.parseDebtInfoFromBids(dFiltered, bidFiltered);

                //System.out.println("��"+indexNum+"�� �� "+i+"���ͶծȨ��������" + dbFiltered.size());

                //�������飬��ÿ����ͶծȨ�곢��Ͷ��
                for (int j = 0; j < dbFiltered.size(); j++) {
                    JSONObject di = dbFiltered.getJSONObject(j);
                    DebtResult debtResult = null;
                    if (!(debtDetermine.determineDuplicateDebtId(di.getInt("DebtId"))) &&
                            !(debtDetermine.determineDuplicateDebtId(di.getInt("ListingId")))) {
                        debtResult = DebtService.buyDebtService(token, openId, di);
                    }
                    if (debtResult != null) {
                        if (!successDebtList.contains(debtResult)) {
                            successDebtList.add(debtResult);
                        }
                        Jedis jedis = RedisUtil.getJedis();
                        try {
                            jedis.setex(String.valueOf(di.getInt("DebtId")), 172800, String.valueOf(di.getInt("ListingId")));
                            jedis.setex(String.valueOf(di.getInt("ListingId")), 172800, String.valueOf(di.getInt("PriceforSale")));
                        }finally {
                            jedis.close();
                        }
                        //logger.info(indexNum + ": " + di);
                        //System.out.println("��ͶծȨ�� DebtId:"+ di.getInt("DebtId") + ", ListingId:" + di.getInt("ListingId") + ", Price:" + di.getDouble("PriceForSale"));
                    }
                }
                Thread.sleep(100);
            }

            indexNum++;
            //logger.info(indexNum);
          Thread.sleep(100);
        } while (debtCount == 50 && indexNum <= debtGroups); //ÿҳ50��Ԫ��

//        logger.info("Total Debt Count is :" + totalDebtCount);

        debtResultsPrint(successDebtList, totalDebtCount);
        //ConfUtil.setProperty("host_name",localHost);
//        pool.close();
    }


    private void debtResultsPrint(ArrayList<DebtResult> successDebtList, int successDebtCount) {
        if (successDebtCount == 0) {
            System.out.println("*~~~~~~~~~~~~~~~~~~~~~�ܱ�Ǹ��û�п�ͶծȨ~~~~~~~~~~~~~~~~~~~~~~~~~*");
        } else if (successDebtList.isEmpty()) {
            System.out.println("*~~~~~~~~~~~~~~~~~~~�ܱ�Ǹ��û���ҵ�����ծȨ~~~~~~~~~~~~~~~~~~~~~~~*");
        } else {
            for (DebtResult dr : successDebtList) {
                logger.info("*~~~~~~~~~~~ DebtId:" + dr.getDebtId() + ", ListingId:" +
                        dr.getListingId() + ", Price:" + dr.getPrice() + "~~~~~~~~~~~*");
            }
        }
    }
}
 