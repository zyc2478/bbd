package com.autobid.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SuppressWarnings("deprecation,unused")
public final class RedisUtil {

    //Redis�Ķ˿ں�
    private static int PORT = 6379;

    //��������
    //private static String AUTH = "admin";

    //private static int TIMEOUT = 10000;

    private static JedisPool jedisPool = null;

    /*
      ��ʼ��Redis���ӳ�
     */
/*    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
            int MAX_TOTAL = 1024;
            config.setMaxTotal(MAX_TOTAL);
            int MAX_IDLE = 200;
            config.setMaxIdle(MAX_IDLE);
            int MAX_WAIT = 10000;
            config.setMaxWaitMillis(MAX_WAIT);
//            boolean TEST_ON_BORROW = true;
            config.setTestOnBorrow(true);
            //jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
            String ADDR = "localhost";
//            jedisPool = new JedisPool(config, ADDR);
//            jedisPool = new JedisPool(config,ADDR,PORT,100000);
            jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static JedisPoolConfig getPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
        int MAX_TOTAL = 1024;
        config.setMaxTotal(MAX_TOTAL);
        int MAX_IDLE = 200;
        config.setMaxIdle(MAX_IDLE);
        int MAX_WAIT = 10000;
        config.setMaxWaitMillis(MAX_WAIT);
        config.setTestOnBorrow(true);
        return config;
    }


    /**
     * ��ȡJedisʵ��
     *
     */
    public static synchronized Jedis getJedis() {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            /// ... do stuff here ... for example
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return jedis;
    }
    /*
     *
      �ͷ�jedis��Դ
      @param jedis
    */

	public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
}