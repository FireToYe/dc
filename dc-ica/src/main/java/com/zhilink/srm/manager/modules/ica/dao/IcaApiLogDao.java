/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.dao;

import java.util.Date;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.ica.entity.IcaApiLog;

/**
 * ica请求日志DAO接口
 * @author chrisye
 * @version 2017-09-15
 */
@MyBatisDao
public interface IcaApiLogDao extends CrudDao<IcaApiLog> {
	void deleteByTask(Date createTime);
	void deleteRfaByTask(Date createTime);
	void deleteByTask(IcaApiLog icaApiLog);
}