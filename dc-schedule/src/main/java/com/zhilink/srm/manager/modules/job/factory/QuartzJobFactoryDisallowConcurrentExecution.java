package com.zhilink.srm.manager.modules.job.factory;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zhilink.srm.manager.modules.job.entity.JobEntity;
import com.zhilink.srm.manager.modules.job.util.TaskUtils;



/**
 * @Description: 若一个方法一次执行不完下次轮转时则等待改方法执行完后才执行下一次操作
 * @author chrisye
 * @version 2017-09-26
 */
@DisallowConcurrentExecution
public class QuartzJobFactoryDisallowConcurrentExecution implements Job{
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobEntity jobEntity = (JobEntity) context.getMergedJobDataMap().get("jobEntity");
		TaskUtils.invokeMethod(jobEntity);
	}


}
