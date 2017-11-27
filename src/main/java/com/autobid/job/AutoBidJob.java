package com.autobid.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.autobid.bbd.BidManager;
  
/** 
 *  
 * @author Richard Zeng 
 * @description 定时处理的任务，任务需要实现Job接口 
 * @time 2017-9-20 
 * @version V1.0 
 * 
 */  
public class AutoBidJob implements Job{  
  
    public void execute(JobExecutionContext context) throws JobExecutionException {  
		// 这里简单输出一句话，和当前的系统时间
		BidManager bid = BidManager.getInstance();
		try {
			bid.bidExcecute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			bid = null;
		}
    }  
}  