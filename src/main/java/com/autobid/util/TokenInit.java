package com.autobid.util;

import com.autobid.bbd.AuthInit;
import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.OpenApiClient;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TokenInit {

    private static Logger logger = Logger.getLogger("TokenInit.class");
    private static String code;
    private static boolean initFlag = false;

    static {
        try {
            AuthInit.init();
            code = ConfUtil.getProperty("code");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*   private volatile static TokenInit instance;
        public static TokenInit getInstance(){
            if(instance == null){
                synchronized (TokenInit.class){
                    if(instance == null){
                        instance = new TokenInit();
                    }
                }
            }
            return instance;
        }
        */
    public static void initToken() throws Exception {
        TokenUtil tokenUtil = new TokenUtil();
        System.out.println("code is:" + code);
        //authInfo = OpenApiClient.authorize("9ca3fb6357b04c5385fd51d1e6db9922");
        AuthInfo authInfo = OpenApiClient.authorize(code);
        String token = authInfo.getAccessToken();
        String refreshToken = authInfo.getRefreshToken();
        String openId = authInfo.getOpenID();

        logger.info("token is: " + token);
        logger.info("refresh token is: " + refreshToken);
        logger.info("openId is:" + openId );
/*        ConfUtil.setProperty("token", token);
        ConfUtil.setProperty("refresh_token", refreshToken);
        ConfUtil.setProperty("open_id",openId);*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowDate = sdf.format(new Date());
        ConfUtil.setProperty("init_date", nowDate);
        tokenUtil.setToken(token);
        tokenUtil.setRefreshToken(refreshToken);
        tokenUtil.setOpenId(openId);
        initFlag = true;
        ConfUtil.setProperty("init_flag","1");
    }

    public static boolean getInitFlag() {
        return initFlag;
    }

/*    private static void refreshToken() throws Exception {
        String refreshToken = ConfUtil.getProperty("refresh_token");
        System.out.println("refreshToken is:" + refreshToken);
        String open_id = ConfUtil.getProperty("open_id");
        System.out.println("Before refresh, openId is :" + open_id);
        AuthInfo authInfo = OpenApiClient.refreshToken(open_id,refreshToken);
        String token = authInfo.getAccessToken();
        String rt = authInfo.getRefreshToken();
        int expiresIn = authInfo.getExpiresIn();
        System.out.println("After refresh, token is:" + token );
        System.out.println("After refresh, refresh token is:" + rt );
        System.out.println("After refresh, openIdn is:" + authInfo.getOpenID()) ;
        System.out.println("After refresh, expired in:" + expiresIn);
    }*/
    @Test
    public void testInitToken() throws Exception {
        TokenUtil tokenUtil = new TokenUtil();
        //TokenInit.initToken();   // 如果genNewToken(）不能用了就用这个方法，但是要用一个新的code来生成（取自AuthInit)
        Thread.sleep(3000);
        //TokenInit.refreshToken();
        tokenUtil.genNewToken();    //genNewToken()无需重新获取code，可以用refreshToken定期获取最新的token，比较方便，优选方法
        Thread.sleep(3000);
        //TokenInit.refreshToken();
        //TokenUtil.genNewToken();
        //System.out.println("TokenUtil.getToken() is :" + TokenUtil.getToken());
    }
}
