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
        // �ӹ����У���ȡһ���������ʵ��
        Scheduler sched1 = sf.getScheduler();
        Scheduler sched2 = sf.getScheduler();

        // ��ʼ������ʵ��
        JobDetail job1 = JobBuilder
                .newJob(TestJob1.class)
                .withIdentity("job1", "group1")
                .build();
        // ��ʼ������ʵ��
        JobDetail job2 = JobBuilder
                .newJob(TestJob2.class)
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

        Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("simple2", "group2")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/" + runInterval + " * * * * ?"))
                .startNow()
                .build();

        //logger.info("���ö�ʱ����");
        // ���ö�ʱ����
        sched1.scheduleJob(job1, trigger1);
        // ������ʱ����
        sched1.start();

        sched2.scheduleJob(job2, trigger2);
        // ������ʱ����
        sched2.start();

/*
        try {
            Thread.sleep(300000L);
        } catch (Exception e) {
        }

        // ֹͣ
        sched.shutdown(true);  */

    }
}
