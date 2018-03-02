package com.autobid.util;

import com.autobid.bbd.AuthInit;
import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.OpenApiClient;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.apache.log4j.Logger;
//import org.junit.Test;
@SuppressWarnings("deprecation")
public class TokenUtil {

    private static Logger logger = Logger.getLogger("TokenUtil.class");
    private static int expireDays;
    private static String initDate;
//    private static Jedis jedis;
    private static String localHost,confHost;
    private String host = ConfUtil.getProperty("redis_host");
//    JedisPool pool = new JedisPool(RedisUtil.getPoolConfig(), host);

    static {
        //openId = "7344c77f9a7f4f249bd9df04115171e6";
        try {
            expireDays = Integer.parseInt(ConfUtil.getProperty("expire_days"));
//            int refreshTokenExpired = Integer.parseInt(ConfUtil.getProperty("refresh_token_expired"));
            initDate = ConfUtil.getProperty("init_date");
            String redisHost = ConfUtil.getProperty("redis_host");
            int redisPort = Integer.parseInt(ConfUtil.getProperty("redis_port"));
//            jedis = new Jedis(redisHost, redisPort);
//            jedis = RedisUtil.getJedis();
            String localHost = HostUtil.getLocalHost();
            String confHost = HostUtil.getConfHost();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TokenUtil() throws IOException {
    }

    //private static Logger logger = Logger.getLogger(TokenUtil.class);


    public void genNewToken() throws Exception {
        String refreshToken = getRefreshToken();
        String openId = getOpenId();
        System.out.println("refreshToken:"+refreshToken+",openId:"+openId);
        AuthInfo authInfo = OpenApiClient.refreshToken(openId,refreshToken);
        String token = authInfo.getAccessToken();
        refreshToken = authInfo.getRefreshToken();
        System.out.println("authInfo.getAccessToken():"+token);
        setToken(token);
        setOpenId(openId);
        setRefreshToken(refreshToken);
        logger.info("After refresh, token is:" + this.getToken());
        logger.info("After refresh, refresh token is:" + this.getRefreshToken() );
        logger.info("After refresh, openId is:" + this.getOpenId() );
    }

    public static boolean determineRefreshDate() throws ParseException {

        int diffDate = getDiffDate();
        int leftDays = expireDays - diffDate % expireDays;
        return leftDays <= 1;
    }

    private static int getDiffDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginDate = df.parse(initDate);
        Date today = new Date();
        //Date today = df.parse("2017-10-23 12:10");
        //logger.info(today);
        long diffTime = today.getTime() - beginDate.getTime();
        long diffDate = (int) (diffTime / (1000 * 60 * 60 * 24));
        return (int) diffDate;
    }

/*    public static boolean determineRefreshTokenExpired() throws ParseException {

        int diffDate = getDiffDate();
        int leftDays = refreshTokenExpired - diffDate % refreshTokenExpired;
        return leftDays <= 1;
    }*/

    public String getToken() throws IOException {
        /// Jedis implements Closeable. Hence, the jedis instance will be auto-closed after the last statement.

        Jedis jedis = RedisUtil.getJedis();
        try {
            String token = jedis.get("token");
            String tokenConf = ConfUtil.getProperty("token");
            if(localHost!=confHost){
                return tokenConf;
            }else{
                setToken(token);
                return jedis.get("token");
            }
        }finally {
            jedis.close();
        }
    }

    private String getRefreshToken() throws IOException {

        Jedis jedis = RedisUtil.getJedis();
        try {
            String refreshToken = jedis.get("refreshToken");
            String refreshTokenConf = ConfUtil.getProperty("refresh_token");
            if(localHost!=confHost){
                System.out.println("refreshTokenConf:" + refreshTokenConf);
                return refreshTokenConf;
            }else{
                setRefreshToken(refreshToken);
                System.out.println("jedis.get(\"refreshToken\"):" + jedis.get("refreshToken"));
                return jedis.get("refreshToken");
            }
        }finally {
            jedis.close();
        }

    }

    private String getOpenId() throws IOException {
        Jedis jedis = RedisUtil.getJedis();
        try {
            String openId = jedis.get("openId");
            String openIdConf = ConfUtil.getProperty("open_id");
            if(localHost!=confHost){
                return openIdConf;
            }else{
                setOpenId(openId);
                return jedis.get("openId");
            }
        }finally {
            jedis.close();
        }
    }
    public void setRefreshToken(String refreshToken) throws IOException {

        Jedis jedis = RedisUtil.getJedis();
        try{
            //String initRefreshToken = ConfUtil.getProperty("refresh_token_init");
            jedis.setex("refreshToken", 7776000, refreshToken); //90天后过期
            ConfUtil.setProperty("refresh_token",refreshToken);
        }finally {
            jedis.close();
        }
    }

    public void setToken(String token) throws IOException {

        Jedis jedis = RedisUtil.getJedis();
        try {
            System.out.println("setToken token:"+token);
            jedis.setex("token", 604800, token); //7天后过期
            ConfUtil.setProperty("token",token);
        }finally {
            jedis.close();
        }
    }

    public void setOpenId(String openId) throws IOException {
        Jedis jedis = RedisUtil.getJedis();
        try{
            jedis.setex("openId",691200,openId);
            ConfUtil.setProperty("open_id",openId);
        }finally {
            jedis.close();
        }

    }
}