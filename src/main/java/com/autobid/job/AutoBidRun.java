package com.autobid.job;

//import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;  
import org.quartz.JobDetail;  
import org.quartz.Scheduler;  
import org.quartz.SchedulerFactory;  
import org.quartz.Trigger;  
import org.quartz.TriggerBuilder;  
import org.quartz.impl.StdSchedulerFactory;  

import com.autobid.util.ConfUtil;

  
/** 
* @ClassName: AutoBidRun 
* @Description: 定时运行服务，在这里配置定时任务
* @author Richard Zeng 
* @date 2017年10月13日 下午5:04:40 
*  
*/
public class AutoBidRun {  
	
	//private static Logger logger = Logger.getLogger(AutoBidRun.class); 
	
    public void run() throws Exception {  
  
        // 调度工厂  
        SchedulerFactory sf = new StdSchedulerFactory();  
        
        int runInterval = Integer.parseInt(ConfUtil.getProperty("run_interval"));
          
        // 从工厂中，获取一个任务调度实体  
        Scheduler sched = sf.getScheduler();  
/*  
        // 定义任务运行时间，这里的话，你需要改成你想要任务在什么时候执行  
        Date runTime = DateUtil.buildDate(18, 30, 30);  
        System.out.println("任务将在：" + DateUtil.fromDate2String(runTime) + "执行");  */
  
        // 初始化任务实体  
        JobDetail job = JobBuilder  
                            .newJob(AutoBidJob.class)  
                            .withIdentity("job1", "group1")  
                            .build();  
        
        // 初始化触发器  
/*        Trigger trigger = TriggerBuilder  
                            .newTrigger()  
                            .withIdentity("trigger1", "group1")  
                            .startAt(runTime)  
                            .build();  */
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simple","group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/" + runInterval + " * * * * ?"))    
                .startNow()
                .build();
  
        //logger.info("设置定时任务");
        // 设置定时任务  
        sched.scheduleJob(job, trigger);  
  
        // 启动定时任务  
        sched.start();  
/*  
        try {  
            Thread.sleep(300000L);  
        } catch (Exception e) {  
        }  
  
        // 停止  
        sched.shutdown(true);  */

    }  
  
    public static void main(String[] args) throws Exception {  
		//BasicConfigurator.configure();
    	//System.out.println("test");
    	AutoBidRun example = new AutoBidRun();  
        example.run();  
    }  
} 