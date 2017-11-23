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
* @Description: ��ʱ���з������������ö�ʱ����
* @author Richard Zeng 
* @date 2017��10��13�� ����5:04:40 
*  
*/
public class AutoBidRun {  
	
	//private static Logger logger = Logger.getLogger(AutoBidRun.class); 
	
    public void run() throws Exception {  
  
        // ���ȹ���  
        SchedulerFactory sf = new StdSchedulerFactory();  
        
        int runInterval = Integer.parseInt(ConfUtil.getProperty("run_interval"));
          
        // �ӹ����У���ȡһ���������ʵ��  
        Scheduler sched = sf.getScheduler();  
/*  
        // ������������ʱ�䣬����Ļ�������Ҫ�ĳ�����Ҫ������ʲôʱ��ִ��  
        Date runTime = DateUtil.buildDate(18, 30, 30);  
        System.out.println("�����ڣ�" + DateUtil.fromDate2String(runTime) + "ִ��");  */
  
        // ��ʼ������ʵ��  
        JobDetail job = JobBuilder  
                            .newJob(AutoBidJob.class)  
                            .withIdentity("job1", "group1")  
                            .build();  
        
        // ��ʼ��������  
/*        Trigger trigger = TriggerBuilder  
                            .newTrigger()  
                            .withIdentity("trigger1", "group1")  
                            .startAt(runTime)  
                            .build();  */
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simple","group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/" + runInterval + " * * * * ?"))    
                .startNow()
                .build();
  
        //logger.info("���ö�ʱ����");
        // ���ö�ʱ����  
        sched.scheduleJob(job, trigger);  
  
        // ������ʱ����  
        sched.start();  
/*  
        try {  
            Thread.sleep(300000L);  
        } catch (Exception e) {  
        }  
  
        // ֹͣ  
        sched.shutdown(true);  */

    }  
  
    public static void main(String[] args) throws Exception {  
		//BasicConfigurator.configure();
    	//System.out.println("test");
    	AutoBidRun example = new AutoBidRun();  
        example.run();  
    }  
} 