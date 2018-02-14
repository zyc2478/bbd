package com.autobid.util;

public class StringUtil {

    public static String filterStrToJSON(String str){
        String resultStr = str.substring(str.indexOf("{"));
        return resultStr.intern();
    }

}
