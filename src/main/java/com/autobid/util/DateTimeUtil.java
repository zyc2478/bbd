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

            Calendar calendar = Calendar.getInstance(); //�õ�����
            calendar.setTime(today);//�ѵ�ǰʱ�丳������
            int year = calendar.get(Calendar.YEAR);//��ȡ���
            int month=calendar.get(Calendar.MONTH);//��ȡ�·�
            int date=calendar.get(Calendar.DATE);//��ȡ��
            int hour=calendar.get(Calendar.HOUR);//Сʱ
            calendar.set(year, month, date,hour,0,0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startTime = calendar.getTime();
            startDateTime = sdf.format(startTime);

        }else if(timeInterval.equals("d")){
            Calendar calendar = Calendar.getInstance(); //�õ�����
            calendar.setTime(today);//�ѵ�ǰʱ�丳������
            int year = calendar.get(Calendar.YEAR);//��ȡ���
            int month=calendar.get(Calendar.MONTH);//��ȡ�·�
            int date=calendar.get(Calendar.DATE);//��ȡ��
            calendar.set(year, month, date,0,0,0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startTime = calendar.getTime();
            startDateTime = sdf.format(startTime);

        }else if(timeInterval.equals("m")){
            Calendar calendar = Calendar.getInstance(); //�õ�����
            calendar.setTime(today);//�ѵ�ǰʱ�丳������
            int year = calendar.get(Calendar.YEAR);//��ȡ���
            int month=calendar.get(Calendar.MONTH);//��ȡ�·�
            int date=calendar.get(Calendar.DATE);//��ȡ��
            int hour=calendar.get(Calendar.HOUR);//Сʱ
            int minute=calendar.get(Calendar.MINUTE);//����
            calendar.set(year, month, date,hour,minute,0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startTime = calendar.getTime();
            startDateTime = sdf.format(startTime);

        }else if(timeInterval.equals("p")) {
            Calendar calendar = Calendar.getInstance(); //�õ�����
            calendar.setTime(today);//�ѵ�ǰʱ�丳������
            int year = calendar.get(Calendar.YEAR)-1;//��ȡ���
            calendar.set(year, 0, 1,0,0,0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date defaultDate = calendar.getTime();
            String defaultDateTime = sdf.format(defaultDate);   //ȡǰ���һ��ΪĬ��ֵ

            Jedis jedis = RedisUtil.getJedis();
            jedis.set("startDateTime","");
            String lastRunTime = jedis.get("startDateTime");
            if(lastRunTime.equals(null) || lastRunTime.equals("")){
                lastRunTime = defaultDateTime;
            }
            //System.out.println("lastRunTime:"+lastRunTime);
            Date lastRunDate = sdf.parse(lastRunTime);
            calendar.setTime(lastRunDate);//�ѵ�ǰʱ�丳������
            year = calendar.get(Calendar.YEAR);//��ȡ���
            int month=calendar.get(Calendar.MONTH);//��ȡ�·�
            int date=calendar.get(Calendar.DATE);//��ȡ��
            int hour=calendar.get(Calendar.HOUR);//Сʱ
            int minute=calendar.get(Calendar.MINUTE);//����
            int second=calendar.get(Calendar.SECOND)+1; //ȡ�ϴ����е���һ��
            calendar.set(year, month, date,hour,minute,second);
            calendar.set(Calendar.MILLISECOND, 0);
            Date startTime = calendar.getTime();
            startDateTime = sdf.format(startTime);
        }else{
            logger.error("�Ƿ���time_interval����");
        }

        System.out.println("DateTimeUtil startDateTime��"+ startDateTime);
        return startDateTime;
    }

}
