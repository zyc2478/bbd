package com.autobid.util;

import org.apache.log4j.Logger;

public class FormatUtil {

    static Logger logger = Logger.getLogger(FormatUtil.class);

    public static String filterStrToJSON(String str){
        String resultStr = str;
        if(str.contains("{")){
            resultStr = str.substring(str.indexOf("{"));
        }else{
            logger.error("不合法的JSON：" + resultStr);
            return  null;
        }
        return resultStr.intern();
    }

    public static String nullToStr(Object obj){
        if(obj.equals(null)){
            return "0";
        }else{
            return obj.toString();
        }
    }
}
