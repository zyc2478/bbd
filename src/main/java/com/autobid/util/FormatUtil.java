package com.autobid.util;

public class FormatUtil {

    public static String filterStrToJSON(String str){
        String resultStr = str;
        if(str.contains("{")){
            resultStr = str.substring(str.indexOf("{"));
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
