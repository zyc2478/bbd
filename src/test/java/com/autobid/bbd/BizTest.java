package com.autobid.bbd;

import com.ppdai.open.core.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Richard Zeng on 2017/10/16
 */

@SuppressWarnings("deprecation,unused")
public class BizTest {

    /************ 应用ＩＤ **************/
    private static String appid = "7344c77f9a7f4f249bd9df04115171e6";

    /***************** 客户端私钥 **************/
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
    /***************** 服务端公钥 ***************/
    private static String serverPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1hTOFi/JjU"
            + "D7gqTP7EqHiLmsgLaI3FsVApbIpDHhXTIhPcC3OVQyysbqvTJUOhbb8JMj4SSninBWkvd0PYJK+e7P6"
            + "+6qJcbo6+pnUJUkRQZ+qE0i6dRwXtmUd3yIPISIM0JP99ALxhl81Uz6Z68GRxnBiySbel84pwYPIJV1"
            + "sCwIDAQAB";
    private String token = "";
    /*********** 授权信息 ***************/
    private AuthInfo authInfo = null;

    @Test
    public void AuthTest() throws Exception {

        /*
          跳转到AC的oauth2.0联合登录
          https://ac.ppdai.com/oauth2/login?AppID=8cf65377538741c2ba8add2615a22299&ReturnUrl=http://mysite.com/auth/gettoken

         */

        /*
          登录成功后 oauth2.0 跳转到http://mysite.com/auth/gettoken?code=XXXXXXXXXXXXXXXXXXXXXXXXXXX
          添加WebApi接口gettoken
         */

        System.out.println("Get token is :" + gettoken());

        /*
          刷新Token
          用于AccessToken失效后刷新一个新的AccessToken，AccessToken有效期七天
         */
        System.out.println("After refresh, token is :" + refreshToken());
    }

    /**
     * 根据授权码获取授权信息
     */
    private String gettoken() throws Exception {
        OpenApiClient.Init(appid, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);
        authInfo = OpenApiClient.authorize("3084cb609b724c66b4ea97e9180c4262");
        token = authInfo.getAccessToken();
        System.out.println("OpenID:" + authInfo.getOpenID());
        System.out.println("AccessToken:" + authInfo.getAccessToken());
        System.out.println("RefreshToken:" + authInfo.getRefreshToken());
        System.out.println("ExpiresIn:" + authInfo.getExpiresIn());
        return token;
    }

    /**
     * 刷新令牌
     *
     * @Throws IOException
     */
    private String refreshToken() throws Exception {
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
                new PropertyObject("SByteProperty", Byte.parseByte("3"), ValueTypeEnum.SByte),
                new PropertyObject("SingleProperty", 25.5687F, ValueTypeEnum.Single),
                new PropertyObject("UInt16Property", 15, ValueTypeEnum.UInt16),
                new PropertyObject("UInt32Property", 256, ValueTypeEnum.UInt32),
                new PropertyObject("UInt64Property", 198745874512L, ValueTypeEnum.UInt64)
        );

        System.out.println(str2);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void InterfaceTest() throws Exception {
        String gwurl = "http://gw.open.ppdai.com";
        String token = "c6f91679-3477-4671-814e-2f53d0cc4641";

        Result result;

        OpenApiClient.Init(appid, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);
        result = OpenApiClient.send("http://gw.open.ppdai.com/invest/BidService/BidList", token,
                new PropertyObject("ListingId", 0, ValueTypeEnum.Int32),
                new PropertyObject("StartTime", "2017-06-15", ValueTypeEnum.String),
                new PropertyObject("EndTime", "2017-06-16", ValueTypeEnum.String),
                new PropertyObject("PageIndex", 1, ValueTypeEnum.Int32),
                new PropertyObject("PageSize", 20, ValueTypeEnum.Int32));
        System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));


        System.out.println("测试 AuthService.SendSMSAuthCode");
        result = OpenApiClient.send(gwurl + "/auth/authservice/sendsmsauthcode"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("DeviceFP", "asdfasdf4asdf546asf", ValueTypeEnum.String));
        System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("测试 AuthService.SMSAuthCodeLogin");
        result = OpenApiClient.send(gwurl + "/open/oauthservice/smsauthcodelogin"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("DeviceFP", "asdfasdf4asdf546asf", ValueTypeEnum.String)
                , new PropertyObject("SMSAuthCode", "111111", ValueTypeEnum.String));
        System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("测试 RegisterService.Register");
        result = OpenApiClient.send(gwurl + "/auth/registerservice/register"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("Email", "xxxxxx@ppdai.com", ValueTypeEnum.String)
                , new PropertyObject("Role", 12, ValueTypeEnum.Int32));
        System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("测试 RegisterService.Register 2");
        result = OpenApiClient.send(gwurl + "/open/registerservice/register"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("Email", "xxxxxx@ppdai.com", ValueTypeEnum.String)
                , new PropertyObject("Role", 12, ValueTypeEnum.Int32));
        System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("测试 RegisterService.SendSMSRegisterCode");
        result = OpenApiClient.send(gwurl + "/auth/registerservice/sendsmsregistercode"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("DeviceFP", "asdfasdf4asdf546asf", ValueTypeEnum.String));
        System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("测试 RegisterService.AccountExist");
        result = OpenApiClient.send(gwurl + "/auth/registerservice/AccountExist"
                , new PropertyObject("AccountName", "15200000001", ValueTypeEnum.String));
        System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("测试 RegisterService.SMSCodeRegister");
        result = OpenApiClient.send(gwurl + "/open/registerservice/smscoderegister"
                , new PropertyObject("Mobile", "15200000001", ValueTypeEnum.String)
                , new PropertyObject("DeviceFP", "asdfasdf4asdf546asf", ValueTypeEnum.String)
                , new PropertyObject("Code", "111111", ValueTypeEnum.String));
        System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("测试 AutoLogin.AutoLogin");
        result = OpenApiClient.send(gwurl + "/auth/LoginService/AutoLogin", token
                , new PropertyObject("Timestamp", new Date(), ValueTypeEnum.DateTime));
        System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();

        System.out.println("测试 AutoLogin.QueryUserInfo");
        result = OpenApiClient.send(gwurl + "/auth/LoginService/QueryUserInfo", token);
        System.out.println(String.format("返回结果:%s", result.isSucess() ? result.getContext() : result.getErrorMessage()));
        System.out.println();
        System.out.println("-----------------------------------------------");
        System.out.println();
    }


    public void pkcs1Test() {
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

}