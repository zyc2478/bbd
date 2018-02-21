package com.autobid.job;

import com.autobid.bbd.BidManager;
import com.autobid.dbd.DebtManager;
import com.autobid.util.ConfUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.IOException;

/**
 * @Author Richard Zeng
 * @Version V1.0
 * @Description: 定时处理的任务，任务需要实现Job接口
 * @Date 2017-9-20
 */
@SuppressWarnings("deprecation")
public class AutoBidJob implements Job {

    public void execute(JobExecutionContext context) {

        BidManager bid = BidManager.getInstance();
        DebtManager debt;
        debt = DebtManager.getInstance();
/*        try {
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
        }finally {
 			bid = null;
 			debt = null;
 		}*/

        int bidMode = 0;
        try {
            bidMode = Integer.parseInt(ConfUtil.getProperty("bid_mode"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bidMode==3){
            final BidManager finalBid = bid;
            Thread threadOne = new Thread(new Runnable() {
                public void run() {
                    try {
                        finalBid.bidExecute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            final DebtManager finalDebt = debt;
            final Thread threadTwo = new Thread(new Runnable() {
                public void run() {
                    try {
                        finalDebt.debtExcecute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            // 执行线程
            threadOne.start();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadTwo.start();
        }else if(bidMode==1){
            final BidManager finalBid = bid;
            Thread threadOne = new Thread(new Runnable() {
                public void run() {
                    try {
                        finalBid.bidExecute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threadOne.start();
        }else if(bidMode==2){
            final DebtManager finalDebt = debt;
            final Thread threadTwo = new Thread(new Runnable() {
                public void run() {
                    try {
                        finalDebt.debtExcecute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threadTwo.start();
        }
    }
}