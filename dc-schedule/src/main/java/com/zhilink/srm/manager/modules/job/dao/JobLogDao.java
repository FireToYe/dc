/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.job.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.job.entity.JobLog;


/**
 * 调度日志DAO接口
 * @author chrisye
 * @version 2017-11-07
 */
@MyBatisDao
public interface JobLogDao extends CrudDao<JobLog> {
	JobLog getLastEnyity(String jobId);
}