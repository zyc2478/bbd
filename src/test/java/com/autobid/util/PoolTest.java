package com.autobid.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by RichardZeng on 17/10/02.
 */
public class PoolTest {
    private static JedisPool pool = null;

    private static JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(500);
            config.setMaxIdle(5);
            config.setMaxWaitMillis(1000 * 10);
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            //new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
            pool = new JedisPool(config, "localhost");

        }
        return pool;
    }

    private synchronized static Jedis getResource() {
        if (pool == null) {
            pool = getPool();
        }
        return pool.getResource();
    }

    // 返还到连接池
    // Deprecated
    // 换成用完之后, redis.close()
    /*
    public static void returnResource(Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }
    */

    public static void main(String[] args) {
        Jedis redis = null;
        int loop = 1;
        while (loop < 20) {
            try {
                long start = System.currentTimeMillis();
                redis = getResource();
                redis.set("k1", "v1");
                String ret = redis.get("k1");
                long end = System.currentTimeMillis();
                System.out.printf("Get " + loop + " ret from redis: %s with %d millis\n", ret, end - start);
            } finally {
                if (redis != null) {
                    redis.close();
                }
            }
            loop++;
        }
    }

}
