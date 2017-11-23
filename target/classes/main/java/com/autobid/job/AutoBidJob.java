package com.autobid.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.autobid.bbd.BidManager;
  
/** 
 *  
 * @author Richard Zeng 
 * @description ��ʱ���������������Ҫʵ��Job�ӿ� 
 * @time 2017-9-20 
 * @version V1.0 
 * 
 */  
public class AutoBidJob implements Job{  
  
    public void execute(JobExecutionContext context) throws JobExecutionException {  
		// ��������һ�仰���͵�ǰ��ϵͳʱ��
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