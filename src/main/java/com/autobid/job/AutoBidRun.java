package com.autobid.job;

//import org.apache.log4j.Logger;

import com.autobid.util.ConfUtil;
import com.autobid.util.RedisUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


/**
 * @Author Richard Zeng
 * @ClassName: AutoBidRun
 * @Description: ��ʱ���з������������ö�ʱ����
 * @Date 2018��2��17�� ����10:04:40
 */

@SuppressWarnings("deprecation")
public class AutoBidRun {

    //private static Logger logger = Logger.getLogger(AutoBidRun.class);

    public static void main(String[] args) throws Exception {
        //BasicConfigurator.configure();
        //System.out.println("test");
        RedisUtil.poolInit();
        AutoBidRun example = new AutoBidRun();
        example.run();
    }

    public void run() throws Exception {

        // ���ȹ���
        SchedulerFactory sf = new StdSchedulerFactory();

        int runInterval = Integer.parseInt(ConfUtil.getProperty("run_interval"));

        // �ӹ����У���ȡһ���������ʵ��
        Scheduler sched = sf.getScheduler();
/*
        // ������������ʱ�䣬����Ļ�������Ҫ�ĳ�����Ҫ������ʲôʱ��ִ��
        Date runTime = DateUtil.buildDate(18, 30, 30);
        System.out.println("�����ڣ�" + DateUtil.fromDate2String(runTime) + "ִ��");  */

        // ��ʼ������ʵ��
        JobDetail job = JobBuilder
                .newJob(AutoBidJob.class)
                .withIdentity("job", "group")
                .build();

        // ��ʼ��������
/*        Trigger trigger = TriggerBuilder
                            .newTrigger()
                            .withIdentity("trigger1", "group1")
                            .startAt(runTime)
                            .build();  */
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simple", "group")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/" + runInterval + " * * * * ?"))
                .startNow()
                .build();

        //logger.info("���ö�ʱ����");
        sched.scheduleJob(job, trigger);
        sched.start();
/*
        try {
            Thread.sleep(300000L);
        } catch (Exception e) {
        }

        // ֹͣ
        sched.shutdown(true);  */
    }
} 