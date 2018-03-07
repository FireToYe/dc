/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.job.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.job.dao.JobLogDao;
import com.zhilink.srm.manager.modules.job.entity.JobLog;


/**
 * 调度日志Service
 * @author chrisye
 * @version 2017-11-07
 */
@Service
@Transactional(readOnly = true)
public class JobLogService extends CrudService<JobLogDao, JobLog> {

	public JobLog get(String id) {
		return super.get(id);
	}
	
	public List<JobLog> findList(JobLog jobLog) {
		return super.findList(jobLog);
	}
	
	public Page<JobLog> findPage(Page<JobLog> page, JobLog jobLog) {
		return super.findPage(page, jobLog);
	}
	
	@Transactional(readOnly = false)
	public void save(JobLog jobLog) {
		super.save(jobLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(JobLog jobLog) {
		super.delete(jobLog);
	}
	
	public JobLog getLastEnyity(String jobId) {
		return dao.getLastEnyity(jobId);
	}
}