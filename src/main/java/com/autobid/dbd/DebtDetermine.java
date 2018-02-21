package com.autobid.dbd;

import com.autobid.entity.Constants;
import com.autobid.util.ConfUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

/**
 * @author Richard Zeng
 * @ClassName: BidDetermine
 * @Description: �жϱ�Ľ��ĳ���
 * @Date 2017��10��13�� ����5:10:43
 */
public class DebtDetermine implements Constants {
    private String host;
    {
        try {
            host = ConfUtil.getProperty("redis_host");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    JedisPool pool = new JedisPool(new JedisPoolConfig(), host);
    public boolean determineDuplicateDebtId(int debtId) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(String.valueOf(debtId));
        }
    }
    public boolean determineDuplicateListingId(int listingId) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(String.valueOf(listingId));
        }
    }
}
