package com.autobid.dbd;

import com.autobid.entity.Constants;
import redis.clients.jedis.Jedis;

/**
 * @author Richard Zeng
 * @ClassName: BidDetermine
 * @Description: �жϱ�Ľ��ĳ���
 * @Date 2017��10��13�� ����5:10:43
 */
public class DebtDetermine implements Constants {

    public static boolean determineDuplicateDebtId(int debtId, Jedis jedis) {
        return jedis.exists(String.valueOf(debtId));
    }
    public static boolean determineDuplicateListingId(int listingId, Jedis jedis) {
        return jedis.exists(String.valueOf(listingId));
    }
}
