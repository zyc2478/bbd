package com.autobid.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SuppressWarnings("deprecation,unused")
public final class RedisUtil {

    //Redis的端口号
    private static int PORT = 6379;

    //访问密码
    //private static String AUTH = "admin";

    //private static int TIMEOUT = 10000;

    private static JedisPool jedisPool = null;

    /*
      初始化Redis连接池
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
        config.setMaxTotal(200);
        config.setMaxIdle(50);
        config.setMinIdle(8);//设置最小空闲数
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        //Idle时进行连接扫描
        config.setTestWhileIdle(true);
        //表示idle object evitor两次扫描之间要sleep的毫秒数
        config.setTimeBetweenEvictionRunsMillis(30000);
        //表示idle object evitor每次扫描的最多的对象数
        config.setNumTestsPerEvictionRun(10);
        //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        config.setMinEvictableIdleTimeMillis(60000);
        return config;
    }


    /**
     * 获取Jedis实例
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
      释放jedis资源
      @param jedis
    */

	public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
}