package com.autobid.dbd;

import com.autobid.entity.Constants;
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 * @author Richard Zeng
 * @ClassName: BidDetermine
 * @Description: 判断标的金额的程序
 * @date 2017年10月13日 下午5:10:43
 */
public class DebtDetermine implements Constants {

    private static Logger logger = Logger.getLogger(DebtDetermine.class);

    static {
        try {
            ConfBean cb = ConfUtil.readAllToBean();
            int DEBT_MIN_PRICE = Integer.parseInt(cb.getDebtMinPrice());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //private static Logger logger = Logger.getLogger(BidDetermine.class);

    public static boolean determineDuplicateId(int debtId, Jedis jedis) {
        return jedis.exists(String.valueOf(debtId));
    }

}
