/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.job.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.job.entity.JobEntity;


/**
 * 定时任务调度DAO接口
 * @author chrisye
 * @version 2017-10-11
 */
@MyBatisDao
public interface JobDao extends CrudDao<JobEntity> {
	void updateState(JobEntity jobEntity);
	JobEntity getByNameAndGroup(JobEntity jobEntity);
}