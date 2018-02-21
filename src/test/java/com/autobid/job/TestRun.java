package com.autobid.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class TestRun {
    public static void main(String[] args) throws Exception {
        //BasicConfigurator.configure();
        //System.out.println("test");
        TestRun example = new TestRun();
        example.run();
    }

    private void run() throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        //SchedulerFactory sf2 = new StdSchedulerFactory();
        int runInterval = 1;
        // 从工厂中，获取一个任务调度实体
        final Scheduler sched = sf.getScheduler();

        // 初始化任务实体
        JobDetail job = JobBuilder
                .newJob(TestJob.class)
                .withIdentity("job", "group")
                .build();
        // 初始化触发器
/*        Trigger trigger = TriggerBuilder
                            .newTrigger()
                            .withIdentity("trigger1", "group1")
                            .startAt(runTime)
                            .build();  */
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simple", "group")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/" + runInterval + " * * * * ?"))
                .startNow()
                .build();

        //logger.info("设置定时任务");
        // 设置定时任务
        sched.scheduleJob(job, trigger);
        // 启动定时任务
        sched.start();
    }
}
