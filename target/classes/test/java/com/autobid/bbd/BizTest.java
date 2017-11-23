package com.autobid.bbd;

import com.ppdai.open.core.AuthInfo;
import com.ppdai.open.core.ObjectDigitalSignHelper;
import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.PropertyObject;
import com.ppdai.open.core.Result;
import com.ppdai.open.core.RsaCryptoHelper;
import com.ppdai.open.core.ValueTypeEnum;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by xuzhishen on 2016/3/16.
 */
public class BizTest {

    /************ Ӧ�ãɣ� **************/
    private static String appid = "7344c77f9a7f4f249bd9df04115171e6";

    /***************** �ͻ���˽Կ **************/
    private static String clientPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGB"
    		+ "AM7lQkzlTjz0HOt/rMqYJcUb4NmTY+7zOGOVG4fSgWdUI4m5Q1IgU5ZuQoyavS6LtX1oDOdu+ul8t+x"
    		+ "Z02XL7nPqjNqiM5vMT1xmS/CK7afG42nGmAxSJSnatsVmQTN7OtiBaHOP1yt6Z4t8I/g9vmtOdqbgMq"
    		+ "tl51H6CmXQzmUfAgMBAAECgYEAxCPP8PpKa7q5WAEYHJCU7gJ57YqvaK6nvLB1AxRrnTvE3SIHUmpXE"
    		+ "/u/l3By/RdxnUvxP86UjJmx+51ErfLhQMj40e0A3gm5GdXRsHgVhHxyV3cpK5TU366y4opnIyaKm9cH"
    		+ "bOZIK/t823D5NovXWj1C/6J/GTAVjfo1Dcp1PKECQQD7mEBZSCwegGsPjtjPKx2iHVJ+QydUdnIDQ+H"
    		+ "nI7yexVb3IyNj3M0sTQiD1EJvPxMangr3W2gRChXA7gOaz585AkEA0oSl3SbSw4KJNnQFPfbh+3nObY"
    		+ "UIuzGnYvJLWZArbSS6b51mM1t6CWeocyM4XG3MsRWg2yh8SXS1eILf0ajPFwJBANqVWkK3S21skJdBu"
    		+ "eezQ9mWtBbybPcauM9RaLCSAcvHE1k/c/3M1YyJmL4/6UARgp17dXeWOIGlS2UE5KjZfTECQFuuxYXB"
    		+ "KL1ZFmUOtlG8OcMJ02tDKwBLqbigCUziSudnvYJqrF3lkwqRiH1Mc3ldoG3nG30W7roXCAmKBewLdtU"
    		+ "CQHNroh7XYmA80gp3iIFcZ/ucyZLhkg5ThC/VMyoGwOgmuMVRCbO4WlzPAQvnv4GSNx6BSD2ARoaqkb"
    		+ "KI/Op36RE=";
    /***************** ����˹�Կ ***************/
    private static String serverPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1hTOFi/JjU"
    		+ "D7gqTP7EqHiLmsgLaI3FsVApbIpDHhXTIhPcC3OVQyysbqvTJUOhbb8JMj4SSninBWkvd0PYJK+e7P6"
    		+ "+6qJcbo6+pnUJUkRQZ+qE0i6dRwXtmUd3yIPISIM0JP99ALxhl81Uz6Z68GRxnBiySbel84pwYPIJV1"
    		+ "sCwIDAQAB";

    /*********** ��Ȩ��Ϣ ***************/
    private AuthInfo authInfo = null;


    
    String token = "";
     @Test
    public void AuthTest() throws Exception {

        /**
         * ��ת��AC��oauth2.0���ϵ�¼
         * https://ac.ppdai.com/oauth2/login?AppID=8cf65377538741c2ba8add2615a22299&ReturnUrl=http://mysite.com/auth/gettoken
         *
         */

        /**
         * ��¼�ɹ��� oauth2.0 ��ת��http://mysite.com/auth/gettoken?code=XXXXXXXXXXXXXXXXXXXXXXXXXXX
         * ���WebApi�ӿ�gettoken
         */

    	System.out.println("Get token is :" + gettoken("3084cb609b724c66b4ea97e9180c4262"));

        /**
         * ˢ��Token
         * ����AccessTokenʧЧ��ˢ��һ���µ�AccessToken��AccessToken��Ч������
         */
    	System.out.println("After refresh, token is :" + refreshToken());
    }

    /**
     * ������Ȩ���ȡ��Ȩ��Ϣ
     *
     * @param code
     * @throws IOException
     */
    public String gettoken(String code) throws Exception {
        OpenApiClient.Init(appid, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);
        authInfo = OpenApiClient.authorize(code);
        token = authInfo.getAccessToken();
        System.out.println("OpenID:" + authInfo.getOpenID());
        System.out.println("AccessToken:" + authInfo.getAccessToken());
        System.out.println("RefreshToken:" + authInfo.getRefreshToken());
        System.out.println("ExpiresIn:" + authInfo.getExpiresIn());
        return token;
    }

    /**
     * ˢ������
     *
     * @throws IOException
     */
    public String refreshToken() throws Exception {
        OpenApiClient.Init(appid, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);
        authInfo = OpenApiClient.refreshToken(authInfo.getOpenID(), authInfo.getRefreshToken());
        token = authInfo.getAccessToken();
        System.out.println("OpenID:" + authInfo.getOpenID());
        System.out.println("AccessToken:" + authInfo.getAccessToken());
        System.out.println("RefreshToken:" + authInfo.getRefreshToken());
        System.out.println("ExpiresIn:" + authInfo.getExpiresIn());
        return token;
    }

    //@Test
    public void getSignString() throws ParseException {

        final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String str2 = ObjectDigitalSignHelper.getObjectHashString(
                new PropertyObject("Age", 18, ValueTypeEnum.Int32),
                new PropertyObject("Amount", BigDecimal.valueOf(154.254), ValueTypeEnum.Decimal),
                new PropertyObject("ByteProperty", Byte.parseByte("5"), ValueTypeEnum.Byte),
                new PropertyObject("CharProperty", 'C', ValueTypeEnum.Char),
                new PropertyObject("CreateDate", dateformat.parse("2016-03-14 19:15:22"), ValueTypeEnum.DateTime),
                new PropertyObject("Data", new ArrayList<String>(), ValueTypeEnum.Other),
                new PropertyObject("DoubleProperty", 3.14159265358979, ValueTypeEnum.Double),
                new PropertyObject("GuidProperty", UUID.fromString("f1f55e34-1a12-41c1-bd51-00341f3eacb8"), ValueTypeEnum.Guid),
                new PropertyObject("ID", 55, ValueTypeEnum.Int16),
                new PropertyObject("Int64Property", 12365478958745487L, ValueTypeEnum.Int64),
                new PropertyObject("IsVIP", false, ValueTypeEnum.Boolean),
                new PropertyObject("Message", "hello world", ValueTypeEnum.String),
                new PropertyObject("SByetProperty", Byte.parseByte("3"), ValueTypeEnum.SByte),
                new PropertyObject("SingleProperty", 25.5687F, ValueTypeEnum.Single),
                new PropertyObject("UInt16Property", 15, ValueTypeEnum.UInt16),
                new PropertyObject("UInt32Property", 256, ValueTypeEnum.UInt32),
                new PropertyObject("UInt64Property", 198745874512L, ValueTypeEnum.UInt64)
        );

        System.out.println(str2);

    }

    @Test
    public void InterfaceTest() throws Exception {
        String gwurl = "http://gw.open.ppdai.com";
        String token = "c6f91679-3477-4671-814e-2f53d0cc4641";

        Result result;

        OpenApiClient.Init(appid, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);
        result = OpenApiClient.send("http://gw.open.ppdai.com/invest/BidService/BidList",token,
                new PropertyObject("ListingId", 0, ValueTypeEnum.Int32),
                new PropertyObject("StartTime","2017-06-15", ValueTypeEnum.String),
                new PropertyObject("EndTime","2017-06-16", ValueTypeEnum.String),
                new PropertyObject("PageIndex",1, ValueTypeEnum.Int32),
                new PropertyObject("PageSize",20, ValueTypeEnum.Int32));
        System.out.println(String.format("���ؽ��:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));


        System.out.println("���� AuthService.SendSMSAuthCode");
        result = OpenApiClient.send(gwurl + "/auth/authservice/sendsmsauthcode"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("DeviceFP", "asdfasdf4asdf546asf", ValueTypeEnum.String));
        System.out.println(String.format("���ؽ��:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("���� AuthService.SMSAuthCodeLogin");
        result = OpenApiClient.send(gwurl + "/open/oauthservice/smsauthcodelogin"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("DeviceFP", "asdfasdf4asdf546asf", ValueTypeEnum.String)
                , new PropertyObject("SMSAuthCode", "111111", ValueTypeEnum.String));
        System.out.println(String.format("���ؽ��:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("���� RegisterService.Register");
        result = OpenApiClient.send(gwurl + "/auth/registerservice/register"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("Email", "xxxxxx@ppdai.com", ValueTypeEnum.String)
                , new PropertyObject("Role", 12, ValueTypeEnum.Int32));
        System.out.println(String.format("���ؽ��:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("���� RegisterService.Register 2");
        result = OpenApiClient.send(gwurl + "/open/registerservice/register"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("Email", "xxxxxx@ppdai.com", ValueTypeEnum.String)
                , new PropertyObject("Role", 12, ValueTypeEnum.Int32));
        System.out.println(String.format("���ؽ��:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("���� RegisterService.SendSMSRegisterCode");
        result = OpenApiClient.send(gwurl + "/auth/registerservice/sendsmsregistercode"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("DeviceFP", "asdfasdf4asdf546asf", ValueTypeEnum.String));
        System.out.println(String.format("���ؽ��:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("���� RegisterService.AccountExist");
        result = OpenApiClient.send(gwurl + "/auth/registerservice/AccountExist"
                , new PropertyObject("AccountName", "15200000001", ValueTypeEnum.String));
        System.out.println(String.format("���ؽ��:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("���� RegisterService.SMSCodeRegister");
        result = OpenApiClient.send(gwurl + "/open/registerservice/smscoderegister"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("DeviceFP", "asdfasdf4asdf546asf", ValueTypeEnum.String)
                , new PropertyObject("Code", "111111", ValueTypeEnum.String));
        System.out.println(String.format("���ؽ��:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("���� AutoLogin.AutoLogin");
        result = OpenApiClient.send(gwurl + "/auth/LoginService/AutoLogin", token
                , new PropertyObject("Timestamp", new Date(), ValueTypeEnum.DateTime));
        System.out.println(String.format("���ؽ��:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("���� AutoLogin.QueryUserInfo");
        result = OpenApiClient.send(gwurl + "/auth/LoginService/QueryUserInfo", token);
        System.out.println(String.format("���ؽ��:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();
    }


    public void pkcs1Test() throws Exception {
        /*String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8iMpEG3mnFlMfufO95DfAfor80RL3I/IzF828aoDDw/Xy86jPiihJyGyG2ZmbqsAw+8nj8eGc+U9LmKASgQhS9e0R/MmYDa9R/O2f4tQZUQr3nE3uUTES0tqCLoE3TVSd59lnVExeDL5IW+F/Yc9mz1v+xSDFcSKyfHEo0FDnnwIDAQAB";
        String privKey = "MIICWwIBAAKBgQC8iMpEG3mnFlMfufO95DfAfor80RL3I/IzF828aoDDw/Xy86jPiihJyGyG2ZmbqsAw+8nj8eGc+U9LmKASgQhS9e0R/MmYDa9R/O2f4tQZUQr3nE3uUTES0tqCLoE3TVSd59lnVExeDL5IW+F/Yc9mz1v+xSDFcSKyfHEo0FDnnwIDAQABAoGAJ5wxqrd/CpzFIBBIZmfxUq8DcnRWoLfbpeJlZiWWIgskvEN2/wuOxVmne3lyLWNld6Ue2JY0CW/TuhU55ElZvv91NiTreBqr5WfZ8EYI+/lwEUKC4GzogVwrmpL1PpSaNJymvTujiShmP/+hia2mav9fhMOYm8MaMRwPELwASiECQQD0nW8xWF9IRT90v89y+P/htW+g3E4HZVAYPXyhfAnFJsGC06XAXwO0hDS8Sao7Nktj2sNSacNFjZvndGrQPOePAkEAxU8o7+QHqm/HYsO0XN49xn6zWQRvAOonhl5/+NKm7NfGEVTGwhP5KbNsJPv3TTtCPrS2V6MlIScg1yLXkFF28QJAGoEYdDNMF6uRJZhG5QE/0Hf1QWu9dKWwmP/IikLDWD5Lx14hXoetAhk1EZW1wTav0oD4muxkwRuH4ftGO4vt1wJAKkjdsBOBZRBRfaQNWj2ypYBvtSsTEvIbiFtmN5AFgAp6AyrU8bDQHBS8n2x0QlPpzYBy93MaOPGmwxRPeDlNMQJAKubPrAE9Qe++95xvvfpZgj6wOZoKGa4Yj3dd1PYcO2fU9eVSW1W6IrvJc36NIGz4Egyw2EiqFBBIJL92ZhjQ2Q==";
        */
    	//String txt = "abc";

        //RsaCryptoHelper rsaCryptoHelper = new RsaCryptoHelper(RsaCryptoHelper.PKCSType.PKCS1, pubKey, privKey);


        //String txt2 = rsaCryptoHelper.encryptByPublicKey(txt);
        //String txt3 = rsaCryptoHelper.decryptByPrivateKey(txt2);

        //String sign = rsaCryptoHelper.sign(txt);
        //boolean isSign = rsaCryptoHelper.verify(txt, sign);
    }

    @Test
    public void pkcs8Test() throws Exception {

/*        String pubKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDk/B01fncBoYj83jQnR3kAAftP\n" +
                "7U7hVX62K+pKZY8sx6nGyskjoxJXT/kFaCmB/gQKi0n9/F5NFNbuY73gMGTlOU4B\n" +
                "jAuhT0n/WiiSi8UA1FqQ5b6Sz7Mr2qhlg83qTyedUzyk+y+SyiRuewWTQ2FHw9v9\n" +
                "A/XF+mstJbLgqRL+ywIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";
        String privKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOT8HTV+dwGhiPze\n" +
                "NCdHeQAB+0/tTuFVfrYr6kpljyzHqcbKySOjEldP+QVoKYH+BAqLSf38Xk0U1u5j\n" +
                "veAwZOU5TgGMC6FPSf9aKJKLxQDUWpDlvpLPsyvaqGWDzepPJ51TPKT7L5LKJG57\n" +
                "BZNDYUfD2/0D9cX6ay0lsuCpEv7LAgMBAAECgYA9AiLyHrisWZJ69OTmVjeZ1e1U\n" +
                "VUC/7pxtAvRQUBC+eI/2ZA8FDKyVULxjQWZVuQzwlj3nirbBSL0fFLoBIkOvAcfi\n" +
                "nd7DcdO51lKC6k4V64nl9YvYXFgEEOtUfrnq9VzehJ6QQTvWQRfURFuXppx5+x9X\n" +
                "c+oFCil3Mr+PjTHGiQJBAP/no5063IqFKlgK1c14pDB4vtRZeqN1x0zY9PpMtTP3\n" +
                "fif66sCyPgWansHIvyMXE8UsAIqZllL9M/PlHnrLzecCQQDlEemONP0AITetgEHi\n" +
                "gvUBXWhW1Vtgj25c/0mvGtFkFhIKPQmmwvNBvn+n4Xlp/XUcgPHMA2X4pT20Eotw\n" +
                "dEN9AkBo35tT0k2TjyNdVYNtY2WWX8WE7O6vkpMM0VUERu9zzpeq9s/CDMoSLd2l\n" +
                "+Qkr7kcx5OiL5ImQlSf3agxlsqQ9AkEAy2d2bnIa3gyg9g1Xc505lXat+b0GoN17\n" +
                "8FQ3x6cWm7sFVdYRReUCQDS6Agay2yzW2vKcwr2ZxIpmGgoFi1uRuQJBAMWgRVMa\n" +
                "aaE2JBriRkxkOUiEYieE7U3bDNZP4BpFmUD6iqkQUeNl9YvIUxE/2tKsMMsR4crI\n" +
                "G/7xG0sUQFNKD9o=\n" +
                "-----END PRIVATE KEY-----";*/
        //String txt = "abc";

        //RsaCryptoHelper rsaCryptoHelper = new RsaCryptoHelper(RsaCryptoHelper.PKCSType.PKCS8, pubKey, privKey);


        //String txt2 = rsaCryptoHelper.encryptByPublicKey(txt);
        //String txt3 = rsaCryptoHelper.decryptByPrivateKey(txt2);

        //String sign = rsaCryptoHelper.sign(txt);
        //boolean isSign = rsaCryptoHelper.verify(txt, sign);
    }
}