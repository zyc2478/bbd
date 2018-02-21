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
 * @Description: 判断标的金额的程序
 * @Date 2017年10月13日 下午5:10:43
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
