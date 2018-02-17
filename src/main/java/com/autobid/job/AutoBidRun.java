package com.autobid.job;

//import org.apache.log4j.Logger;

import com.autobid.util.ConfUtil;
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
        AutoBidRun example = new AutoBidRun();
        example.run();
    }

    public void run() throws Exception {

        // ���ȹ���
        SchedulerFactory sf = new StdSchedulerFactory();

        int runInterval = Integer.parseInt(ConfUtil.getProperty("run_interval"));

        // �ӹ����У���ȡһ���������ʵ��
        Scheduler sched1 = sf.getScheduler();
        Scheduler sched2 = sf.getScheduler();
/*
        // ������������ʱ�䣬����Ļ�������Ҫ�ĳ�����Ҫ������ʲôʱ��ִ��
        Date runTime = DateUtil.buildDate(18, 30, 30);
        System.out.println("�����ڣ�" + DateUtil.fromDate2String(runTime) + "ִ��");  */

        // ��ʼ������ʵ��
        JobDetail job1 = JobBuilder
                .newJob(BidJob.class)
                .withIdentity("job1", "group1")
                .build();

        JobDetail job2 = JobBuilder
                .newJob(DebtJob.class)
                .withIdentity("job2", "group2")
                .build();
        // ��ʼ��������
/*        Trigger trigger = TriggerBuilder
                            .newTrigger()
                            .withIdentity("trigger1", "group1")
                            .startAt(runTime)
                            .build();  */
        Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("simple1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/" + runInterval + " * * * * ?"))
                .startNow()
                .build();

        Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("simple1", "group2")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/" + runInterval + " * * * * ?"))
                .startNow()
                .build();
        //logger.info("���ö�ʱ����");

        int bidMode = Integer.parseInt(ConfUtil.getProperty("bid_mode"));
        if(bidMode==3){
            sched1.scheduleJob(job1, trigger1);
            sched1.start();
            sched2.scheduleJob(job2, trigger2);
            sched2.start();
        }else if(bidMode==1){
            // ���ö�ʱ����
            sched1.scheduleJob(job1, trigger1);
            // ������ʱ����
            sched1.start();
        }else if(bidMode==2){
            sched2.scheduleJob(job2, trigger2);
            sched2.start();
        }
/*
        try {
            Thread.sleep(300000L);
        } catch (Exception e) {
        }

        // ֹͣ
        sched.shutdown(true);  */
    }
} 