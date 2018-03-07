/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.msg.service;

import java.util.List;

import com.zhilink.srm.manager.modules.job.dao.JobDao;
import com.zhilink.srm.manager.modules.job.entity.JobEntity;
import com.zhilink.srm.manager.modules.job.service.JobService;
import com.zhilink.srm.manager.modules.msg.util.DcMailSendUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.msg.entity.DcMessageSendLog;
import com.zhilink.srm.manager.modules.msg.dao.DcMessageSendLogDao;

/**
 * 信息发送详情Service
 * @author chrisye
 * @version 2018-02-06
 */
@Service
@Transactional(readOnly = true)
public class DcMessageSendLogService extends CrudService<DcMessageSendLogDao, DcMessageSendLog> implements InitializingBean{

	@Value("${send_msg_status:false}")
	private String sendMsgStatus;
	@Autowired
	private JobService jobService;

	public String getSendMsgStatus() {
		return sendMsgStatus;
	}

	@Autowired
	private JobDao jobDao;
	@Autowired
	private DcMessageSendPropertiesService dcMessageSendPropertiesService;

	public DcMessageSendLog get(String id) {
		return super.get(id);
	}
	
	public List<DcMessageSendLog> findList(DcMessageSendLog dcMessageSendLog) {
		return super.findList(dcMessageSendLog);
	}
	
	public Page<DcMessageSendLog> findPage(Page<DcMessageSendLog> page, DcMessageSendLog dcMessageSendLog) {
		return super.findPage(page, dcMessageSendLog);
	}
	
	@Transactional(readOnly = false)
	public void save(DcMessageSendLog dcMessageSendLog) {
		super.save(dcMessageSendLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(DcMessageSendLog dcMessageSendLog) {
		super.delete(dcMessageSendLog);
	}


	public void afterPropertiesSet() throws Exception {
		JobEntity jobEntity = getSendJob();
		JobEntity oldEntity = jobService.getByNameAndGroup(jobEntity);
		if(oldEntity==null){
			jobDao.insert(jobEntity);
			if("true".equals(sendMsgStatus)) {
				jobDao.updateState(jobEntity);
				jobService.addJob(jobEntity);
			}
		}else{
			jobEntity = oldEntity;
		}

		if(!"1".equals(oldEntity.getJobState())){
			if("true".equals(sendMsgStatus)) {
				jobEntity.setJobState("1");
				jobDao.updateState(jobEntity);
				jobService.addJob(jobEntity);
			}
		}else{
			if(!"true".equals(sendMsgStatus)) {
				jobEntity.setJobState("0");
				jobDao.updateState(jobEntity);
				jobService.deleteJob(jobEntity);
			}
		}
	}
	@Transactional(readOnly = false)
	public void changeState(String state){
		sendMsgStatus = state;
		JobEntity jobEntity = new JobEntity();
		jobEntity.setJobName("信息发送任务");
		jobEntity.setJobGroup("信息发送组");
		JobEntity oldEntity = jobService.getByNameAndGroup(jobEntity);
		if(oldEntity==null){
			oldEntity = getSendJob();
			jobDao.insert(oldEntity);
		}
		try {
			if ("true".equals(sendMsgStatus)) {
				oldEntity.setJobState("1");
				jobDao.updateState(oldEntity);
				jobService.addJob(oldEntity);
			}
			if ("false".equals(sendMsgStatus)) {
				oldEntity.setJobState("0");
				jobDao.updateState(oldEntity);
				jobService.deleteJob(oldEntity);
			}
		}catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	@Transactional(readOnly = false)
	public String executeSendMsg(String parameter){
		DcMessageSendLog dcMessageSendLog = new DcMessageSendLog();
		dcMessageSendLog.setStatus("0");
		List<DcMessageSendLog> list = dao.findList(dcMessageSendLog);
		logger.info("邮件发送任务启动,预计发送："+list.size());
		StringBuilder sb = new StringBuilder();
		for(DcMessageSendLog msg:list){
			sb.append(DcMailSendUtil.send(msg,dcMessageSendPropertiesService.getByCache(msg.getFromAddress(),msg.getType()))).append(System.getProperty("line.separator"));
			dao.update(msg);
		}
		return sb.toString();
	}

	/**
	 *邮件发送插入
	 * @param toMail 收件人地址
	 * @param subject 发件主题
	 * @param content 发送内容
	 * @param fromMail 发件人
	 */
	@Transactional(readOnly = false)
	public void insertMail(String toMail,String subject,String content,String fromMail){
		if("true".equals(sendMsgStatus)){
			DcMessageSendLog dcMessageSendLog = new DcMessageSendLog();
			dcMessageSendLog.setToAddress(toMail);
			dcMessageSendLog.setSubject(subject);
			dcMessageSendLog.setContent(content);
			dcMessageSendLog.setFromAddress(fromMail);
			dcMessageSendLog.setType("2");
			dcMessageSendLog.setStatus("0");
			dcMessageSendLog.preInsert();
			dao.insert(dcMessageSendLog);
		}
	}

	/**
	 *
	 * @param toMail 收件人地址
	 * @param subject 发件主题
	 * @param content 发送内容
	 */
	@Transactional(readOnly = false)
	public void insertMail(String toMail,String subject,String content){
		insertMail(toMail,subject,content,null);
	}
	public JobEntity getSendJob(){
		JobEntity jobEntity = new JobEntity();
		jobEntity.setJobName("信息发送任务");
		jobEntity.setJobGroup("信息发送组");
		jobEntity.setIsConcurrent("0");
		jobEntity.setExecuteType("2");
		jobEntity.setParameter("");
		jobEntity.setSpringId("dcMessageSendLogService");
		jobEntity.setMethodName("executeSendMsg");
		jobEntity.setDependLastTime("N");
		jobEntity.setCronExpression("0 0/1 * * * ? ");
		return jobEntity;
	}
}