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
     * ��ʼ��Redis���ӳ�
     */
    private static void initializePool() throws IOException {
/*
        //redisURL �� redisPort �������ļ�
        String configFile = "production.properties";
        if (PropKit.getBoolean("devMode")) {
            configFile = "dev.properties";
        }
*/
        JedisPoolConfig config = new JedisPoolConfig();
        //���������������100���㹻���ˣ�û��Ҫ����̫��
        config.setMaxTotal(100);
        //������������
        config.setMaxIdle(10);
        //��ȡJedis���ӵ����ȴ�ʱ�䣨50�룩
        config.setMaxWaitMillis(50 * 1000);
        //�ڻ�ȡJedis����ʱ���Զ����������Ƿ����
        config.setTestOnBorrow(true);
        //�ڽ����ӷŻس���ǰ���Զ����������Ƿ���Ч
        config.setTestOnReturn(true);
        //�Զ����Գ��еĿ��������Ƿ��ǿ�������
        config.setTestWhileIdle(true);
        //�������ӳ�
        pool = new JedisPool(config,ConfUtil.getProperty("redis_host"));
        //pool = new JedisPool(config, ConfUtil.getProperty("redis_host"), ConfUtil.getProperty("redis_port"));
    }

    /**
     * ���̻߳���ͬ����ʼ������֤��Ŀ�����ҽ���һ�����ӳأ�
     */
    public static synchronized void poolInit() throws IOException {
        if (null == pool) {
            initializePool();
        }
    }

    /**
     * ��ȡJedisʵ��
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
     * �ͷ�Jedis��Դ
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
     * ���Ի�ȡ��������֤һ���ܹ�ʹ�ÿ��õ����ӻ�ȡ�� Ŀ�����ݣ�
     * Jedis����ʹ�ú�Ż�
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
     * �������÷�������֤һ���ܹ�ʹ�ÿ��õ��������� ���ݣ�
     * Jedis����ʹ�ú󷵻����ӳ�
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
     * ����ɾ����������֤ɾ��������Ч��
     * Jedis����ʹ�ú󷵻����ӳ�</span>
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

    /**�Զ����һЩ get set del ����������ʹ��**/
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
