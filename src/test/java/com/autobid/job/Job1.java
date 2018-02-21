package com.autobid.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

public class Job1{

    public static void runThread(){
        Date now = new Date();
        for(int i=0;i<20;i++){
            System.out.println("##1## Job1 Running times "+i+"! "+ now);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
