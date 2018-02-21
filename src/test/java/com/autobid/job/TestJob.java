package com.autobid.job;

import com.autobid.util.ConfUtil;
import org.junit.Test;
import org.quartz.*;

import java.io.IOException;


public class TestJob implements Job{

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

/*        Thread threadOne = new Thread(new Runnable() {
            public void run() {
                Job1.runThread();
            }
        });
        final Thread threadTwo = new Thread(new Runnable() {
            public void run() {
                Job2.runThread();
            }
        });
        try {
            ConfUtil.setProperty("is_running","1");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ִ���߳�
        try {
            if(ConfUtil.getProperty("is_running").equals("1")){
                threadOne.start();
                threadTwo.start();
                ConfUtil.setProperty("is_running","0");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
        // ִ���߳�
        threadOne.start();
        threadTwo.start();
    }

}
