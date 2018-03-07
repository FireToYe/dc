/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.rfa.entity.RfaServiceLog;

/**
 * 接口适配器调用日志DAO接口
 * @author chrisye
 * @version 2017-11-17
 */
@MyBatisDao
public interface RfaServiceLogDao extends CrudDao<RfaServiceLog> {
	
}