package com.autobid.job;

import com.autobid.bbd.BidManager;
import com.autobid.dbd.DebtManager;
import com.autobid.util.ConfUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Richard Zeng
 * @version V1.0
 * @description 定时处理的任务，任务需要实现Job接口
 * @time 2017-9-20
 */
public class AutoBidJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {

        int bidMode;
        BidManager bid = BidManager.getInstance();
        DebtManager debt = DebtManager.getInstance();
        try {
            bidMode = Integer.parseInt(ConfUtil.getProperty("bid_mode"));
            if (bidMode == 1) {
                bid.bidExcecute();
            } else if (bidMode == 2) {
                bid.bidExcecute();
                debt.debtExcecute();
            } else if (bidMode == 3) {
                debt.debtExcecute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }/*finally {
 			bid = null;
 			debt = null;
 		}*/
    	
/*    	BidManager bid = new BidManager();

		try {
			bidMode = Integer.parseInt(ConfUtil.getProperty("bid_mode"));
			if(bidMode==1) {
				bid.bidExcecute();
			}else if(bidMode==2) {
				bid.bidExcecute();
				//debt.debtExcecute();
			}else if(bidMode==3) {
		    	DebtManager debt = DebtManager.getInstance();
				debt.debtExcecute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/


    }


}  