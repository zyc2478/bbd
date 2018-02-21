package com.autobid.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;


public class Job1{

    public static synchronized void runThread(){
        Date now = new Date();
        for(int i=0;i<40;i++){
            System.out.println("##1## Job1 Running times "+i+"! "+ now);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
