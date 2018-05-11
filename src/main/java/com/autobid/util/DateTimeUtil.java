package com.autobid.util;

import com.autobid.bbd.BidService;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    private static Logger logger = Logger.getLogger(DateTimeUtil.class);
    public static String calcStartDateTime(String timeInterval) throws IOException, ParseException {
        String startDateTime = "";
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date today = new Date();

        if(timeInterval.equals("h")){

            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(today);//把当前时间赋给日历
            int year = calendar.get(Calendar.YEAR);//获取年份
            int month=calendar.get(Calendar.MONTH);//获取月份
            int date=calendar.get(Calendar.DATE);//获取日
            int hour=calendar.get(Calendar.HOUR);//小时
            calendar.set(year, month, date,hour,0,0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startTime = calendar.getTime();
            startDateTime = sdf.format(startTime);

        }else if(timeInterval.equals("d")){
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(today);//把当前时间赋给日历
            int year = calendar.get(Calendar.YEAR);//获取年份
            int month=calendar.get(Calendar.MONTH);//获取月份
            int date=calendar.get(Calendar.DATE);//获取日
            calendar.set(year, month, date,0,0,0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startTime = calendar.getTime();
            startDateTime = sdf.format(startTime);

        }else if(timeInterval.equals("m")){
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(today);//把当前时间赋给日历
            int year = calendar.get(Calendar.YEAR);//获取年份
            int month=calendar.get(Calendar.MONTH);//获取月份
            int date=calendar.get(Calendar.DATE);//获取日
            int hour=calendar.get(Calendar.HOUR);//小时
            int minute=calendar.get(Calendar.MINUTE);//分钟
            calendar.set(year, month, date,hour,minute,0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startTime = calendar.getTime();
            startDateTime = sdf.format(startTime);

        }else if(timeInterval.equals("p")) {
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(today);//把当前时间赋给日历
            int year = calendar.get(Calendar.YEAR)-1;//获取年份
            calendar.set(year, 0, 1,0,0,0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date defaultDate = calendar.getTime();
            String defaultDateTime = sdf.format(defaultDate);   //取前年第一天为默认值

            Jedis jedis = RedisUtil.getJedis();
            jedis.set("startDateTime","");
            String lastRunTime = jedis.get("startDateTime");
            if(lastRunTime.equals(null) || lastRunTime.equals("")){
                lastRunTime = defaultDateTime;
            }
            //System.out.println("lastRunTime:"+lastRunTime);
            Date lastRunDate = sdf.parse(lastRunTime);
            calendar.setTime(lastRunDate);//把当前时间赋给日历
            year = calendar.get(Calendar.YEAR);//获取年份
            int month=calendar.get(Calendar.MONTH);//获取月份
            int date=calendar.get(Calendar.DATE);//获取日
            int hour=calendar.get(Calendar.HOUR);//小时
            int minute=calendar.get(Calendar.MINUTE);//分钟
            int second=calendar.get(Calendar.SECOND)+1; //取上次运行的下一秒
            calendar.set(year, month, date,hour,minute,second);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startTime = calendar.getTime();
            startDateTime = sdf.format(startTime);
        }else{
            logger.error("非法的time_interval参数");
        }

        System.out.println("DateTimeUtil startDateTime："+ startDateTime);
        return startDateTime;
    }

}
