package com.autobid.bbd;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.entity.CriteriaBid;
import com.autobid.entity.CriteriaGroup;
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Richard Zeng
 * @ClassName: BidDetermine
 * @Description: 判断标的金额的程序
 * @Date 2017年10月13日 下午5:10:43
 */
public class BidDetermine implements Constants {

    private static int MIN_BID_AMOUNT;
    private static int BID_LEVEL_AMOUNT;
    private static int MULTIPLE;
    private static int MORE_MULTIPLE;
    private static float MINI_MULTIPLE, MEDIUM_MULTIPLE;
    private static Logger logger = Logger.getLogger(BidDetermine.class);

    static {
        try {
            ConfBean cb = ConfUtil.readAllToBean();
            MIN_BID_AMOUNT = Integer.parseInt(cb.getMinBidAmount());
            BID_LEVEL_AMOUNT = Integer.parseInt(cb.getBidLevelAmount());
            MULTIPLE = Integer.parseInt(cb.getMultiple());
            MORE_MULTIPLE = Integer.parseInt(cb.getMoreMultiple());
            MINI_MULTIPLE = Float.parseFloat(cb.getMiniMultiple());
            MEDIUM_MULTIPLE = Float.parseFloat(cb.getMediumMultiple());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //private static Logger logger = Logger.getLogger(BidDetermine.class);  

    public static boolean determineBalance(double queryBalance) throws Exception {
        if (queryBalance > MIN_BID_AMOUNT) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean determineDuplicateId(int listingId, Jedis jedis) {
        return jedis.exists(String.valueOf(listingId));
    }

    @SuppressWarnings("unused")
    public static int determineLoanAmountPro(int loanAmountLevelPro) {
        switch (loanAmountLevelPro) {
            case PERFECT:
                return (int) (BID_LEVEL_AMOUNT * MEDIUM_MULTIPLE);
            case GOOD:
                return BID_LEVEL_AMOUNT * MULTIPLE;
            case OK:
                return BID_LEVEL_AMOUNT;
            default:
                return NONE;
        }
    }
    @SuppressWarnings("unused")
    public static int determineCertificate(int certificateLevel) {
        return levelCalcMedium(certificateLevel);
    }

    private static int levelCalcMedium(int criteriaLevel) {
        switch (criteriaLevel) {
            case PERFECT:
                return BID_LEVEL_AMOUNT * MULTIPLE;
            case GOOD:
                return (int) (BID_LEVEL_AMOUNT * MEDIUM_MULTIPLE);
            case OK:
                return BID_LEVEL_AMOUNT;
            default:
                return NONE;
        }
    }
    @SuppressWarnings("unused")
    public static int determineSuccessCount(int successCountLevel) {
        switch (successCountLevel) {
            case GOOD:
                return (int) (BID_LEVEL_AMOUNT * MINI_MULTIPLE);
            case OK:
                return BID_LEVEL_AMOUNT;
            default:
                return NONE;
        }
    }
    @SuppressWarnings("unused")
    public static int determineOverduePro(int overdueLevelPro) {
        switch (overdueLevelPro) {
            case GOOD:
                return BID_LEVEL_AMOUNT;
            default:
                return NONE;
        }
    }
    @SuppressWarnings("unused")
    public static int determineDebtRate(int debtRateLevel) {
        return levelCalcMedium(debtRateLevel);
    }

    @SuppressWarnings("unused")
    public static int determineDebtRatePro(int debtRateProLevel) {
        switch (debtRateProLevel) {
            case PERFECT:
                return (int) (BID_LEVEL_AMOUNT * MEDIUM_MULTIPLE);
            case GOOD:
                return BID_LEVEL_AMOUNT;
            default:
                return NONE;
        }
    }
    @SuppressWarnings("unused")
    public static int determineEducation(int educationLevel) {
        return levelCalcMore(educationLevel);
    }

    private static int levelCalcMore(int criteriaLevel) {
        switch (criteriaLevel) {
            case PERFECT:
                return BID_LEVEL_AMOUNT * MORE_MULTIPLE;
            case GOOD:
                return BID_LEVEL_AMOUNT * MULTIPLE;
            case OK:
                return BID_LEVEL_AMOUNT;
            default:
                return NONE;
        }
    }
    @SuppressWarnings("unused")
    public static int determineEducationPro(int educationProLevel) {
        switch (educationProLevel) {
            case PERFECT:
                return BID_LEVEL_AMOUNT * MULTIPLE;
            case GOOD:
                return BID_LEVEL_AMOUNT;
            default:
                return NONE;
        }
    }
    @SuppressWarnings("unused")
    public static int determineBeginPro(int educationProLevel) {
        return levelCalcMore(educationProLevel);
    }

    @SuppressWarnings("unused")
    public static int determineBasic(int basicLevel) {
        switch (basicLevel) {
            case PERFECT:
                return MIN_BID_AMOUNT + BID_LEVEL_AMOUNT * MULTIPLE;
            case GOOD:
                return MIN_BID_AMOUNT + BID_LEVEL_AMOUNT;
            case OK:
                return MIN_BID_AMOUNT;
            case SOSO:
                return MIN_BID_AMOUNT;
            default:
                return NONE;
        }
    }
    @SuppressWarnings("unused")
    public static int determineEduDebtPro(int eduDebtProLevel) {
        switch (eduDebtProLevel) {
            case PERFECT:
                return BID_LEVEL_AMOUNT * MORE_MULTIPLE;
            case GOOD:
                return BID_LEVEL_AMOUNT * MULTIPLE;
            case OK:
                return (int) (BID_LEVEL_AMOUNT * MEDIUM_MULTIPLE);
            default:
                return NONE;
        }
    }
    @SuppressWarnings("unused")
    public static int determineLastSuccessBorrow(int lastSuccessBorrowLevel) {
        switch (lastSuccessBorrowLevel) {
            case GOOD:
                return BID_LEVEL_AMOUNT;
            case OK:
                return NONE;
            default:
                return NONE;
        }
    }

    public static int determineCriteriaGroup(CriteriaGroup criteriaGroup, ConfBean cb, HashMap<String, Object> loanInfoMap)
            throws Exception {
        //System.out.println("eduCriteriaGroup's size " + eduCriteriaGroup.getCriteriaList().size());
        int totalAmount = 0;
        //System.out.println("criteriaGroup is : " + criteriaGroup.getCriteriaList());
        ArrayList<Criteria> criteriaList = criteriaGroup.getCriteriaList();
        for (Criteria c : criteriaList) {
            String criteriaName = c.getCriteriaName();
            String methodName = "determine" + criteriaName;
            int criteriaLevel = c.getLevel(loanInfoMap, cb);

            //注意，static方法不能使用this.getClass(),getDeclaredMethod(name, parameterTypes)
            //int本身不是对象，只能用如下方法调用 new Class[]{int.class}
            //Method method = this.getClass().getDeclaredMethod(methodName,new Class[]{int.class});

            //Class<?> bdo = Class.forName("com.autobid.bbd.BidDetermine");
            Class<BidDetermine> bdo = BidDetermine.class;
            Method method = bdo.getDeclaredMethod(methodName, int.class);
            //int amount = Integer.parseInt(method.invoke(this.getClass(),criteriaLevel).toString());
            int amount = Integer.parseInt(method.invoke(bdo, criteriaLevel).toString());
            CriteriaBid criteriaBid = new CriteriaBid();
            criteriaBid.setAmount(amount);
            criteriaBid.setCriteriaName(criteriaName);
            criteriaBid.setLevel(criteriaLevel);
            //logger.info(criteriaBid.print());
/*			if(criteriaLevel>0) {
				System.out.println(c.getCriteriaName() + "："+ c.getLevel(loanInfoMap)+
						"; amount="+amount);
			}*/
            totalAmount += amount;

        }
        return totalAmount;
    }
}
