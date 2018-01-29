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
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;
import com.autobid.util.TokenInit;
import com.autobid.util.TokenUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author Richard Zeng
 * @Version 1.0
 * @ClassName: DebtManager
 * @Description: �Զ�Ͷ���������
 * @Date 2017��1��3�� ����5:14:02
 */

@SuppressWarnings("deprecation")
public class DebtManager implements Constants {

    private static String token = "";
    private static String openId;
    private static Jedis jedis;
    private static ConfBean confBean;
    private static Logger logger = Logger.getLogger(DebtManager.class);
    //����
    private volatile static DebtManager instance;

    //static double balance;
    static {
        try {
            AuthInit.init();
            confBean = ConfUtil.readAllToBean();
            openId = confBean.getOpenId();
            String redisHost = confBean.getRedisHost();
            int redisPort = Integer.parseInt(confBean.getRedisPort());

            jedis = new Jedis(redisHost, redisPort);

            //���TokenInit��������ڣ����ʼ��Token���洢��Redis��
            if (TokenUtil.determineTokenInitExists()) {
                TokenInit.initToken();
            }
            //���Token�쵽�ڣ����ȡһ����Token
            if (TokenUtil.determineRefreshDate()) {
                TokenUtil.genNewToken();
            }
            //��ȡToken�������ļ��������ȣ�û�����ȡRedis
            token = TokenUtil.getToken();
            //logger.info("token:" + token);
            //String balanceJson = BidService.queryBalanceService(token);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public void debtExcecute() throws Exception {
        logger.info("debtExcecute");
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
        int debtGroups;

        do {
            balance = BidDataParser.getBalance(BidService.queryBalanceService(token));
            if(BidDetermine.determineBalance(balance)) {
                logger.error("���㣬�����˳���1���Ӻ��ٴγ���");
                Thread.sleep(60000);
                return;
            }
            JSONArray debtListArray = DebtService.debtListService(indexNum);
            debtCount = debtListArray.size();
            totalDebtCount += debtCount;
            //����ȡ��debtList����DebtListFilter�ж���Ĺ������
            //logger.info("��"+indexNum+"�� �����±�ģ�");
/*			for(int i=0;i<debtListArray.size();i++) {
				JSONObject debtListObject = debtListArray.getJSONObject(i);
				//logger.info(debtListObject);	
			}
			*/
            JSONArray dlFiltered = dlf.filter(debtListArray, confBean);

            logger.info("��" + indexNum + "�� �������˺�dlFiltered������" + dlFiltered.size());
            //debtFCount += dlFiltered.size();

            //System.out.println("debtFCount "+ indexNum + " is: "+ debtFCount);

            //��debtList�з�Ϊ10��һ��,��ƴ�ӳ�һ��Collector
            ArrayList<JSONArray> daList = DebtDataParser.getDebtsCollector(dlFiltered);

            for (JSONArray aDaList : daList) {
                //��ȡծȨ�����ϸ
                //logger.info(daList.get(i));

                JSONArray debtInfosList = DebtService.batchDebtInfosService(aDaList);
				
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
                    if (!(DebtDetermine.determineDuplicateDebtId(di.getInt("DebtId"), jedis)) &&
                            !(DebtDetermine.determineDuplicateDebtId(di.getInt("ListingId"), jedis))) {
                        debtResult = DebtService.buyDebtService(token, openId, di);
                    }
                    if (debtResult != null) {
                        if (!successDebtList.contains(debtResult)) {
                            successDebtList.add(debtResult);
                        }
                        jedis.setex(String.valueOf(di.getInt("DebtId")), 172800, String.valueOf(di.getInt("ListingId")));
                        jedis.setex(String.valueOf(di.getInt("ListingId")), 172800, String.valueOf(di.getInt("PriceforSale")));
                        logger.info(indexNum + ": " + di);
                        //System.out.println("��ͶծȨ�� DebtId:"+ di.getInt("DebtId") + ", ListingId:" + di.getInt("ListingId") + ", Price:" + di.getDouble("PriceForSale"));
                    }
                }
                Thread.sleep(200);
            }
            if (indexNum == 1) {
                debtGroups = Integer.parseInt(confBean.getDebtMaxGroups());
            } else {
                debtGroups = Integer.parseInt(confBean.getDebtMinGroups());
            }
            indexNum++;
            //logger.info(indexNum);
        } while (debtCount == 50 && indexNum <= debtGroups); //ÿҳ50��Ԫ��

        logger.info("Total Debt Count is :" + totalDebtCount);

        debtResultsPrint(successDebtList, totalDebtCount);
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
 