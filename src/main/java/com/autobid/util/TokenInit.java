package com.autobid.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.autobid.bbd.AuthInit;
import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.OpenApiClient;

public class TokenInit {

	static Logger logger = Logger.getLogger("TokenInit.class");
	private static String code;
    private static AuthInfo authInfo = null;
    private static String token = "";
    private static String refreshToken = "";
    private static boolean initFlag = false;
    
    static {
        try {
    		code = ConfUtil.getProperty("code") ;
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
/*	private volatile static TokenInit instance;
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
		AuthInit.init();
		initFlag = true;
		//System.out.println("code is:" + code);
		//authInfo = OpenApiClient.authorize("9ca3fb6357b04c5385fd51d1e6db9922");
		authInfo = OpenApiClient.authorize(code);
		token = authInfo.getAccessToken();  //7dcf80d8-e74b-42ad-a439-6dfe567741ec
		refreshToken = authInfo.getRefreshToken(); //fc7d2e81-6191-41b0-84fb-c95c61c1d119
		
		logger.info("token is: " + token);
		logger.info("refresh token is: " + refreshToken);
		ConfUtil.setProperty("token_init", token);
		ConfUtil.setProperty("refresh_token_init", refreshToken);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowDate = sdf.format(new Date());
        ConfUtil.setProperty("init_date", nowDate);
		TokenUtil.setTokenInit(token);
		TokenUtil.setRefreshTokenInit(refreshToken);
		
	}
	
	public static boolean getInitFlag() {
		return initFlag;
	}
	
	@Test
	public void testInitToken() throws Exception {
		TokenInit.initToken();
	}
}
