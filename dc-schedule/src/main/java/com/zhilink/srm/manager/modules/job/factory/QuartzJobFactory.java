package com.zhilink.srm.manager.modules.job.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zhilink.srm.manager.modules.job.entity.JobEntity;
import com.zhilink.srm.manager.modules.job.util.TaskUtils;


/**
 * @Description: 计划任务执行处 无状态
 * @author chrisye
 * @version 2017-09-26
 */
public class QuartzJobFactory implements Job{
//	private Lock lock = new ReentrantLock();
//	private static ConcurrentHashMap<String, String> concurrentMap = new ConcurrentHashMap<String, String>();
	private static ConcurrentHashMap<String,  Lock> lockMap = new ConcurrentHashMap<String, Lock>();
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobEntity jobEntity = (JobEntity) context.getMergedJobDataMap().get("jobEntity");
		String key = jobEntity.getJobName()+"_"+jobEntity.getJobGroup();
		if(!lockMap.containsKey(key)){
			synchronized (lockMap) {
				if(!lockMap.containsKey(key)){
					lockMap.put(key, new ReentrantLock());
				}
			}
		}
		Lock lock = lockMap.get(key);
		if(lock.tryLock()){
			try{
				TaskUtils.invokeMethod(jobEntity);
			}finally{
				lock.unlock();
			}
		}
	}


}
