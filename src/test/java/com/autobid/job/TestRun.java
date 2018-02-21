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
        final Scheduler sched = sf.getScheduler();

        // ��ʼ������ʵ��
        JobDetail job = JobBuilder
                .newJob(TestJob.class)
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
        // ���ö�ʱ����
        sched.scheduleJob(job, trigger);
        // ������ʱ����
        sched.start();
    }
}
