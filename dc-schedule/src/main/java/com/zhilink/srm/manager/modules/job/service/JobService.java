/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.job.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.job.dao.JobDao;
import com.zhilink.srm.manager.modules.job.entity.JobEntity;
import com.zhilink.srm.manager.modules.job.factory.QuartzJobFactory;
import com.zhilink.srm.manager.modules.job.factory.QuartzJobFactoryDisallowConcurrentExecution;


/**
 * 定时任务调度Service
 * @author chrisye
 * @version 2017-10-11
 */
@Service
@Transactional(readOnly = true)
public class JobService extends CrudService<JobDao, JobEntity> {
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private JobDao jobDao;
	public JobEntity get(String id) {
		return super.get(id);
	}
	
	public List<JobEntity> findList(JobEntity jobEntity) {
		return super.findList(jobEntity);
	}
	
	public Page<JobEntity> findPage(Page<JobEntity> page, JobEntity jobEntity) {
		return super.findPage(page, jobEntity);
	}
	
	@Transactional(readOnly = false)
	public void save(JobEntity jobEntity) {
		super.save(jobEntity);
	}
	
	@Transactional(readOnly = false)
	public void delete(JobEntity jobEntity) {
		super.delete(jobEntity);
	}
	
	@Transactional(readOnly = false)
	public void updateState(JobEntity jobEntity) {
		jobDao.updateState(jobEntity);
	}
	/**
	 * 添加一个Job
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	@Transactional(readOnly = false)
	public void addJob(JobEntity jobEntity) throws SchedulerException{
		if(jobEntity==null||!JobEntity.STATUS_RUNNING.equals(jobEntity.getJobState())){//如果状态不为启动状态，不添加，直接return；
			Scheduler scheduler = schedulerFactoryBean.getScheduler();//从单例调度器工厂中获取
			TriggerKey triggerKey = TriggerKey.triggerKey(jobEntity.getJobName(), jobEntity.getJobGroup());//根据名字和组名确定唯一key
			Trigger trigger = scheduler.getTrigger(triggerKey);//根据key获取触发器
			if(trigger!=null){
				deleteJob(jobEntity);
			}
			return;
		}
		Scheduler scheduler = schedulerFactoryBean.getScheduler();//从单例调度器工厂中获取
		TriggerKey triggerKey = TriggerKey.triggerKey(jobEntity.getJobName(), jobEntity.getJobGroup());//根据名字和组名确定唯一key
		Trigger trigger = scheduler.getTrigger(triggerKey);//根据key获取触发器
		//如果触发器为空。则代表当前的任务不存在，则新增。否则更新
		if(trigger==null){
			//采用调度器
			Class clazz = JobEntity.CONCURRENT_IS .equals(jobEntity.getIsConcurrent())?QuartzJobFactoryDisallowConcurrentExecution.class:QuartzJobFactory.class;
			//step1:创建jobDetail实例，并应用job
			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobEntity.getJobName(), jobEntity.getJobGroup()).build();
			//将调用信息放入jobDataMap，以后取出使用
			jobDetail.getJobDataMap().put("jobEntity", jobEntity);
			//生成cron调用器构造器。
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobEntity.getCronExpression());
			trigger = TriggerBuilder.newTrigger().withIdentity(jobEntity.getJobName(), jobEntity.getJobGroup()).withSchedule(cronScheduleBuilder).build();
			((JobDetailImpl)jobDetail).setDescription(jobEntity.getDetail());
			//触发器加入调度器统一调度
			scheduler.scheduleJob(jobDetail, trigger);
		}else{
			// Trigger已存在，那么更新相应的定时设置
			JobKey jobKey = JobKey.jobKey(jobEntity.getJobName(),jobEntity.getJobGroup());
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			//更新jobDetail中dataMap数据
			//jobDetail.getJobDataMap().put("jobEntity", jobEntity);
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobEntity.getCronExpression());
			//trigger存在直接使用triggerKey并按新的cronExpression表达式重新构建trigger
			trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
			
			scheduler.rescheduleJob(triggerKey,trigger);
		}
	}
	@PostConstruct
	public void init() throws Exception{
		List<JobEntity> list = findList(new JobEntity());
		for(JobEntity jobEntity:list){
			addJob(jobEntity);
		}
	}
	/**
	 * 暂停job
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	@Transactional(readOnly = false)
	public void pauseJob(JobEntity jobEntity) throws SchedulerException{
		if(jobEntity==null){
			return;
		}
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobkey = JobKey.jobKey(jobEntity.getJobName(),jobEntity.getJobGroup());
		scheduler.pauseJob(jobkey);
	}
	
	/**
	 * 恢复job
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	@Transactional(readOnly = false)
	public void resumeJob(JobEntity jobEntity) throws SchedulerException{
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobkey = JobKey.jobKey(jobEntity.getJobName(),jobEntity.getJobGroup());
		scheduler.resumeJob(jobkey);
	}
	
	
	/**
	 * 删除job
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	@Transactional(readOnly = false)
	public void deleteJob(JobEntity jobEntity) throws SchedulerException{
		if(jobEntity==null){
			return;
		}
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobkey = JobKey.jobKey(jobEntity.getJobName(),jobEntity.getJobGroup());
		scheduler.deleteJob(jobkey);
	}
	/**
	 * 立马执行一次jobEntity
	 * @param jobEntity
	 * @throws SchedulerException
	 */
	public void runAJobNow(JobEntity jobEntity) throws SchedulerException {
		if(jobEntity==null){
			return;
		}
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobEntity.getJobName(), jobEntity.getJobGroup());
		scheduler.triggerJob(jobKey);
	}
	
	/**
	 * 获取计划中所有任务
	 * @return
	 * @throws SchedulerException
	 */
	public List<JobEntity> getAllJob() throws SchedulerException{
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		GroupMatcher<JobKey> groupMatcher = GroupMatcher.anyJobGroup();
		List<JobEntity> jobList = new ArrayList<JobEntity>();
		Set<JobKey> jobSet = scheduler.getJobKeys(groupMatcher);
		for(JobKey jobKey:jobSet){
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			JobEntity jobBean = (JobEntity) jobDetail.getJobDataMap().get("jobEntity");
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for(Trigger trigger:triggers){
				JobEntity jobEntity = new JobEntity();
				jobEntity.setJobName(jobKey.getName());
				jobEntity.setJobGroup(jobKey.getGroup());
				jobEntity.setDetail("触发器："+trigger.getKey());
				jobEntity.setId(jobBean.getId());
				jobEntity.setIsConcurrent(jobBean.getIsConcurrent());
				jobEntity.setExecuteType(jobBean.getExecuteType());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				jobEntity.setJobState(triggerState.name());
				if(trigger instanceof CronTrigger){
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					jobEntity.setCronExpression(cronExpression);
				}
				jobList.add(jobEntity);
			}
		}
		return jobList;
	}
	
	/**
	 * 获取所有运行的任务
	 * @return
	 * @throws SchedulerException
	 */
	public List<JobEntity> getAllExcuteJob() throws SchedulerException{
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		List<JobExecutionContext> contexts = scheduler.getCurrentlyExecutingJobs();
		List<JobEntity> jobList = new ArrayList<JobEntity>();
		for(JobExecutionContext jobExecutionContext:contexts){
			JobDetail jobDetail = jobExecutionContext.getJobDetail();
			JobEntity jobEntity = (JobEntity) jobDetail.getJobDataMap().get("jobEntity");
			JobKey jobKey = jobDetail.getKey();
			Trigger trigger = jobExecutionContext.getTrigger();
			jobEntity.setJobName(jobKey.getName());
			jobEntity.setJobGroup(jobKey.getGroup());
			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			jobEntity.setJobState(triggerState.name());
			jobEntity.setDetail("触发器："+trigger.getKey());
			if(trigger instanceof CronTrigger){
				CronTrigger cronTrigger = (CronTrigger) trigger;
				String cronExpression = cronTrigger.getCronExpression();
				jobEntity.setCronExpression(cronExpression);
			}
			jobList.add(jobEntity);
		}
		return jobList;
	}
	public void test(String param){
		logger.info("第一次执行任务"+param);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test1(String param){
		logger.info("第二次执行任务:参数"+param);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JobEntity getByNameAndGroup(JobEntity jobEntity){
		return dao.getByNameAndGroup(jobEntity);
	}

}