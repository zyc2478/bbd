package com.autobid.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowDate = sdf.format(new Date());
        System.out.println(nowDate);
    }
}
