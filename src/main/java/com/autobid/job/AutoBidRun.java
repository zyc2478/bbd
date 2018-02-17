package com.autobid.job;

//import org.apache.log4j.Logger;

import com.autobid.util.ConfUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


/**
 * @Author Richard Zeng
 * @ClassName: AutoBidRun
 * @Description: 定时运行服务，在这里配置定时任务
 * @Date 2018年2月17日 下午10:04:40
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

        // 调度工厂
        SchedulerFactory sf = new StdSchedulerFactory();

        int runInterval = Integer.parseInt(ConfUtil.getProperty("run_interval"));

        // 从工厂中，获取一个任务调度实体
        Scheduler sched1 = sf.getScheduler();
        Scheduler sched2 = sf.getScheduler();
/*
        // 定义任务运行时间，这里的话，你需要改成你想要任务在什么时候执行
        Date runTime = DateUtil.buildDate(18, 30, 30);
        System.out.println("任务将在：" + DateUtil.fromDate2String(runTime) + "执行");  */

        // 初始化任务实体
        JobDetail job1 = JobBuilder
                .newJob(BidJob.class)
                .withIdentity("job1", "group1")
                .build();

        JobDetail job2 = JobBuilder
                .newJob(DebtJob.class)
                .withIdentity("job2", "group2")
                .build();
        // 初始化触发器
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
        //logger.info("设置定时任务");

        int bidMode = Integer.parseInt(ConfUtil.getProperty("bid_mode"));
        if(bidMode==3){
            sched1.scheduleJob(job1, trigger1);
            sched1.start();
            sched2.scheduleJob(job2, trigger2);
            sched2.start();
        }else if(bidMode==1){
            // 设置定时任务
            sched1.scheduleJob(job1, trigger1);
            // 启动定时任务
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

        // 停止
        sched.shutdown(true);  */
    }
} 