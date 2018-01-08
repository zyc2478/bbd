package com.autobid.job;

import com.autobid.bbd.BidManager;
import com.autobid.dbd.DebtManager;
import com.autobid.util.ConfUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * @Author Richard Zeng
 * @Version V1.0
 * @Description: 定时处理的任务，任务需要实现Job接口
 * @Date 2017-9-20
 */
@SuppressWarnings("deprecation")
public class AutoBidJob implements Job {

    public void execute(JobExecutionContext context) {

        int bidMode;
        BidManager bid = BidManager.getInstance();
        DebtManager debt;
        debt = DebtManager.getInstance();
        try {
            bidMode = Integer.parseInt(ConfUtil.getProperty("bid_mode"));
            if (bidMode == 1) {
                bid.bidExecute();
            } else if (bidMode == 2) {
                bid.bidExecute();
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