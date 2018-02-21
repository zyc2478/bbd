package com.autobid.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job{

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Thread threadOne = new Thread(new Runnable() {
            public void run() {
                Job1.runThread();
            }
        });
        final Thread threadTwo = new Thread(new Runnable() {
            public void run() {
                Job2.runThread();
            }
        });
        // Ö´ÐÐÏß³Ì
        threadOne.start();
        threadTwo.start();
    }

}
