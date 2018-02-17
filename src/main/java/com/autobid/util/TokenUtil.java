package com.autobid.util;

import com.autobid.bbd.AuthInit;
import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.OpenApiClient;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

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
    private static Jedis jedis;

    static {
        //openId = "7344c77f9a7f4f249bd9df04115171e6";
        try {
            expireDays = Integer.parseInt(ConfUtil.getProperty("expire_days"));
//            int refreshTokenExpired = Integer.parseInt(ConfUtil.getProperty("refresh_token_expired"));
            initDate = ConfUtil.getProperty("init_date");
            String redisHost = ConfUtil.getProperty("redis_host");
            int redisPort = Integer.parseInt(ConfUtil.getProperty("redis_port"));
            jedis = new Jedis(redisHost, redisPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //private static Logger logger = Logger.getLogger(TokenUtil.class);

    public static void genNewToken() throws Exception {

        String refreshToken = getRefreshToken();
        String openId = getOpenId();
        AuthInfo authInfo = OpenApiClient.refreshToken(openId,refreshToken);
        String token = authInfo.getAccessToken();
        refreshToken = authInfo.getRefreshToken();
        setToken(token);
        setOpenId(openId);
        setRefreshToken(refreshToken);
        logger.info("After refresh, token is:" + getToken());
        logger.info("After refresh, refresh token is:" + getRefreshToken() );
        logger.info("After refresh, openId is:" + getOpenId() );
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

    public static String getToken() throws IOException {
        String token = jedis.get("token");
        String tokenConf = ConfUtil.getProperty("token");
        if(token.equals("") && !tokenConf.equals("")){
            setToken(token);
        }else if(!token.equals("") && tokenConf.equals("")){
            System.out.println("Try to modify the token value in conf file!");
            ConfUtil.setProperty("token",token);
        }
        return jedis.get("token");
    }

    private static String getRefreshToken() throws IOException {
        String refreshToken = jedis.get("refreshToken");
        String refreshTokenConf = ConfUtil.getProperty("refresh_token");
        int init_flag = Integer.parseInt(ConfUtil.getProperty("init_flag"));
        if(!refreshTokenConf.equals("") &&  init_flag==1){
            setRefreshToken(refreshTokenConf);
        }else if(!refreshToken.equals("") && refreshTokenConf.equals("")){
            ConfUtil.setProperty("refresh_token",refreshToken);
        }

/*        if(refreshToken.equals("") && !refreshTokenConf.equals("")){
            setRefreshToken(refreshTokenConf);
        }else if(!refreshToken.equals("") && refreshTokenConf.equals("")){
            ConfUtil.setProperty("refresh_token",refreshToken);
        }*/
        return jedis.get("refreshToken");
    }

    private static String getOpenId() throws IOException {
        String openId = jedis.get("openId");
        String openIdConf = ConfUtil.getProperty("open_id");
        if(openId.equals("") && !openIdConf.equals("")){
            setOpenId(openId);
        }else if(!openId.equals("") && openIdConf.equals("")){
            ConfUtil.setProperty("open_id",openId);
        }
        return jedis.get("openId");
    }



/*
    public static void setToken(String newToken) {
        TokenUtil.token = newToken;
        jedis.setex("token", 691200, token);
    }
*/

/*    public static String getRefreshToken() {
        refreshToken = jedis.get("refreshToken");
        return refreshToken;
    }*/

/*    public static void setRefreshToken(String newRefreshToken) {
        TokenUtil.refreshToken = newRefreshToken;
        jedis.setex("refreshToken", 8640000, refreshToken);
    }*/

    public static void setRefreshToken(String refreshToken) throws IOException {
        //String initRefreshToken = ConfUtil.getProperty("refresh_token_init");
        jedis.setex("refreshToken", 7776000, refreshToken); //90天后过期
        ConfUtil.setProperty("refresh_token",refreshToken);
    }

    public static void setToken(String token) throws IOException {
        //String initToken =  ConfUtil.getProperty("token_init");
        jedis.setex("token", 604800, token); //7天后过期
        ConfUtil.setProperty("token",token);
    }

    public static void setOpenId(String openId) throws IOException {
        jedis.setex("openId",691200,openId);
        ConfUtil.setProperty("open_id",openId);
    }
}