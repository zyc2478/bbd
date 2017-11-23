package com.autobid.bbd;

import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.RsaCryptoHelper;

/** 
* @ClassName: AuthInit 
* @Description: ��֤��ʼ������tokenÿ7��ͻ�ʧЧ����Ҫ��ʱˢ��
* @author Richard Zeng 
* @date 2017��10��13�� ����5:11:30 
* 
*/
public class AuthInit {

    /************ Ӧ�ãɣ� **************/
    private static String appId = "7344c77f9a7f4f249bd9df04115171e6";
    
	//private static Logger logger = Logger.getLogger(AuthInit.class); 

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
    
    /**
     * ��ת��AC��oauth2.0���ϵ�¼
     * https://ac.ppdai.com/oauth2/login?AppID=7344c77f9a7f4f249bd9df04115171e6&ReturnUrl=http://bidbydebt.com/auth/gettoken
     * 
     * ��¼�ɹ��� oauth2.0 ��ת��http://bidbydebt.com/auth/gettoken?code=6c6efc46793745f6b86a372b5f45dd9b
     * 
     */
	public static void init() throws Exception{
        OpenApiClient.Init(appId, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);
    }
}
