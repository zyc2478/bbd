package com.autobid.util;

import com.autobid.bbd.AuthInit;
import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.OpenApiClient;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.apache.log4j.Logger;
//import org.junit.Test;

public class TokenUtil {

    private final static String openId = "0d2283b0fa334d14894180520c26a865";
    static AuthInfo authInfo;
    private static String token;
    private static String refreshToken;
    private static int expireDays;
    private static int refreshTokenExpired;
    private static String initDate;
    private static String redisHost;
    private static int redisPort;
    private static Jedis jedis;

    static {
        try {
            expireDays = Integer.parseInt(ConfUtil.getProperty("expire_days"));
            refreshTokenExpired = Integer.parseInt(ConfUtil.getProperty("refresh_token_expired"));
            initDate = ConfUtil.getProperty("init_date");
            redisHost = ConfUtil.getProperty("redis_host");
            System.out.println("redisHost:"+ redisHost);
            redisPort = Integer.parseInt(ConfUtil.getProperty("redis_port"));
            jedis = new Jedis(redisHost, redisPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //private static Logger logger = Logger.getLogger(TokenUtil.class);

    public static void genNewToken() throws Exception {

        AuthInit.init();
        //logger.info(determineRefreshDate());

        String tokenFromConf = ConfUtil.getProperty("refresh_token");

        if (tokenFromConf.equals("")) {
            refreshToken = jedis.get("refreshToken");
        } else {
            refreshToken = tokenFromConf;
        }

        authInfo = OpenApiClient.refreshToken(openId, refreshToken);
        //String newToken = authInfo.getAccessToken();			//47401bc7-1639-4cb6-a168-01c30a628599
        //String newRefreshToken = authInfo.getRefreshToken();	//fc7d2e81-6191-41b0-84fb-c95c61c1d119
/*		setToken(newToken);
		setRefreshToken(newRefreshToken);*/
    }

    public static boolean determineTokenInitExsits() throws IOException {
        if (ConfUtil.getProperty("token_init").equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean determineRefreshDate() throws ParseException {

        int diffDate = getDiffDate();
        int leftDays = expireDays - diffDate % expireDays;
        if (leftDays <= 1) {
            return true;
        }
        return false;
    }

    public static int getDiffDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginDate = df.parse(initDate);
        Date today = new Date();
        //Date today = df.parse("2017-10-23 12:10");
        //logger.info(today);
        long diffTime = today.getTime() - beginDate.getTime();
        long diffDate = (int) (diffTime / (1000 * 60 * 60 * 24));
        return (int) diffDate;
    }

    public static boolean determineRefreshTokenExpired() throws ParseException {

        int diffDate = getDiffDate();
        int leftDays = refreshTokenExpired - diffDate % refreshTokenExpired;
        if (leftDays <= 1) {
            return true;
        }
        return false;
    }

    public static String getToken() {
        token = jedis.get("token");
        return token;
    }

    public static void setToken(String newToken) {
        TokenUtil.token = newToken;
        jedis.setex("token", 691200, token);
    }

    public static String getRefreshToken() {
        refreshToken = jedis.get("refreshToken");
        return refreshToken;
    }

    public static void setRefreshToken(String newRefreshToken) {
        TokenUtil.refreshToken = newRefreshToken;
        jedis.setex("refreshToken", 8640000, refreshToken);
    }

    public static void setRefreshTokenInit(String refreshTokenInit) throws IOException {
        //String initRefreshToken = ConfUtil.getProperty("refresh_token_init");
        jedis.setex("refreshToken", 8640000, refreshTokenInit); //100天后过期
    }

    public static void setTokenInit(String tokenInit) throws IOException {
        //String initToken =  ConfUtil.getProperty("token_init");
        jedis.setex("token", 691200, tokenInit); //8天后过期
    }
}