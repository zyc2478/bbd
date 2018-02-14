package com.autobid.util;

import org.junit.Test;

public class JSONUtilTest {

    @Test
    public void testDecode() {
        System.out.println(JSONUtil.decodeUnicode("\u4e13\u79d1(\u9ad8\u804c)"));
        String str = "\u4e13\u79d1(\u9ad8\u804c)";
        if (str.contains("\u4e13\u79d1")) {
            System.out.println("包含：" + JSONUtil.decodeUnicode("\u4e13\u79d1"));
        }
        if (str.contains(JSONUtil.decodeUnicode("\u4e13\u79d1"))) {
            System.out.println("str包含：" + "专科");
        }
        String strCn = "专科（高职）";
        if (strCn.contains(JSONUtil.decodeUnicode("专科"))) {
            System.out.println("strCn包含：" + "专科");
        }
        System.out.println(JSONUtil.decodeUnicode("\u60a8\u7684\u64cd\u4f5c\u592a\u9891\u7e41\u5566\uff0c\u8bf7\u559d\u676f\u8336\u540e\uff0c\u518d\u6765\u8bd5\u8bd5\u5427"));

    }
}
