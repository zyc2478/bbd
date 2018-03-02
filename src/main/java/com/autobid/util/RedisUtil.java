package com.autobid.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.List;


public class RedisUtil {
    private static Logger logger = Logger.getLogger(RedisUtil.class);
    private List<JSONObject> resultList;
    private static JedisPool pool;

    /**
     * 初始化Redis连接池
     */
    private static void initializePool() throws IOException {
/*
        //redisURL 与 redisPort 的配置文件
        String configFile = "production.properties";
        if (PropKit.getBoolean("devMode")) {
            configFile = "dev.properties";
        }
*/
        JedisPoolConfig config = new JedisPoolConfig();
        //设置最大连接数（100个足够用了，没必要设置太大）
        config.setMaxTotal(100);
        //最大空闲连接数
        config.setMaxIdle(10);
        //获取Jedis连接的最大等待时间（50秒）
        config.setMaxWaitMillis(50 * 1000);
        //在获取Jedis连接时，自动检验连接是否可用
        config.setTestOnBorrow(true);
        //在将连接放回池中前，自动检验连接是否有效
        config.setTestOnReturn(true);
        //自动测试池中的空闲连接是否都是可用连接
        config.setTestWhileIdle(true);
        //创建连接池
        pool = new JedisPool(config,ConfUtil.getProperty("redis_host"));
        //pool = new JedisPool(config, ConfUtil.getProperty("redis_host"), ConfUtil.getProperty("redis_port"));
    }

    /**
     * 多线程环境同步初始化（保证项目中有且仅有一个连接池）
     */
    public static synchronized void poolInit() throws IOException {
        if (null == pool) {
            initializePool();
        }
    }

    /**
     * 获取Jedis实例
     */
    public static Jedis getJedis() throws IOException {
        if (null == pool) {
            poolInit();
        }

        int timeoutCount = 0;
        while (true) {
            try {
                if (null != pool) {
                    return pool.getResource();
                }
            } catch (Exception e) {
                if (e instanceof JedisConnectionException) {
                    timeoutCount++;
                    logger.warn("getJedis timeoutCount={}:" + timeoutCount);
                    if (timeoutCount > 3) {
                        break;
                    }
                } else {
                    logger.warn("jedisInfo ... NumActive=" + pool.getNumActive()
                            + ", NumIdle=" + pool.getNumIdle()
                            + ", NumWaiters=" + pool.getNumWaiters()
                            + ", isClosed=" + pool.isClosed());
                    logger.error("GetJedis error," ,e);
                    break;
                }
            }
            break;
        }
        return null;
    }

    /**
     * 释放Jedis资源
     *
     * @param jedis
     */
    public static void returnResource(Jedis jedis) {
        if (null != jedis) {
            //pool.getResource();
            jedis.close();
        }
    }

    /**
     * 绝对获取方法（保证一定能够使用可用的连接获取到 目标数据）
     * Jedis连接使用后放回
     * @param key
     * @return
     */
    private String safeGet(String key) throws IOException {
        Jedis jedis = getJedis();
        while (true) {
            if (null != jedis) {
                break;
            } else {
                jedis = getJedis();
            }
        }
        String value = jedis.get(key);
        returnResource(jedis);
        return value;
    }

    /**
     * 绝对设置方法（保证一定能够使用可用的链接设置 数据）
     * Jedis连接使用后返回连接池
     * @param key
     * @param time
     * @param value
     */
    private void safeSet(String key, int time, String value) throws IOException {
        Jedis jedis = getJedis();
        while (true) {
            if (null != jedis) {
                break;
            } else {
                jedis = getJedis();
            }
        }
        jedis.setex(key, time, value);
        returnResource(jedis);
    }

    /**
     * 绝对删除方法（保证删除绝对有效）
     * Jedis连接使用后返回连接池</span>
     * @param key
     */
    private void safeDel(String key) throws IOException {
        Jedis jedis = getJedis();
        while (true) {
            if (null != jedis) {
                break;
            } else {
                jedis = getJedis();
            }
        }
        jedis.del(key);
        returnResource(jedis);
    }

    /**自定义的一些 get set del 方法，方便使用**/
    public JSONObject getJSONObject(String key) throws IOException {
        String result = safeGet(key);
        if (result != null) {
            return JSONObject.fromObject(result);
        }
        return null;

    }

    public String getString(String key) throws IOException {
        String result = safeGet(key);
        if (result != null) {
            return result;
        }
        return null;

    }

    public List<JSONObject> getArrayByCache(String key) throws IOException {
        String result = safeGet(key);

        if (result != null) {
            resultList = (List<JSONObject>) JSONArray.toArray( getJSONArray(key), JSONObject.class);
            return resultList;
        }
        return null;
    }

    public JSONArray getJSONArray(String key) throws IOException {
        String result = safeGet(key);
        if (result != null) {
            return JSONArray.fromObject(result);
        }
        return null;
    }

    public void set(String key, String s) throws IOException {
        safeSet(key, 86400, s);
    }

    public void setByOneHour(String key, String s) throws IOException {
        safeSet(key, 3600, s);
    }

    public void setByOneHour(String key, List<JSONObject> json) throws IOException {
        safeSet(key, 86400, JSONObject.fromObject(json).toString());
        resultList = json;
    }

    public void set(String key, JSONObject json) throws IOException {
        safeSet(key, 86400, json.toString());
    }

    public void set(String key, List<JSONObject> list) throws IOException {
        safeSet(key, 86400, JSONObject.fromObject(list).toString());
        resultList = list;
    }

    public void set(String key, JSONArray array) throws IOException {
        safeSet(key, 86400, array.toString());
    }

    public void setExTime(String key, String s, int time) throws IOException {
        safeSet(key, time, s);
    }


    public void del(String key) throws IOException {
        if (null != safeGet(key)) {
            safeDel(key);
        }
    }
/*
    public JSONObject toJSON(DBObject db) {
        return (JSONObject) JSONObject.toJSON(db);
    }

    public List<JSONObject> toJSON(List<DBObject> list) {
        List<JSONObject> json = new ArrayList<>();
        for (DBObject aList : list) {
            json.add((JSONObject) JSONObject.toJSON(aList));
        }
        return json;
    }*/

    public boolean notNull() {
        return resultList != null && resultList.size() > 0;
    }

    public List<JSONObject> getResult() {
        return resultList;
    }

}
