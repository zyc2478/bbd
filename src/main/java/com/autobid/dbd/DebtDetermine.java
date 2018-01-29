package com.autobid.dbd;

import com.autobid.entity.Constants;
import redis.clients.jedis.Jedis;

/**
 * @author Richard Zeng
 * @ClassName: BidDetermine
 * @Description: 判断标的金额的程序
 * @Date 2017年10月13日 下午5:10:43
 */
public class DebtDetermine implements Constants {

    public static boolean determineDuplicateDebtId(int debtId, Jedis jedis) {
        return jedis.exists(String.valueOf(debtId));
    }
    public static boolean determineDuplicateListingId(int listingId, Jedis jedis) {
        return jedis.exists(String.valueOf(listingId));
    }
}
