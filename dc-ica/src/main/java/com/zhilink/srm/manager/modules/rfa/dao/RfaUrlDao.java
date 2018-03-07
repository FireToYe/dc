/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.rfa.entity.RfaUrl;

/**
 * rfa连接管理DAO接口
 * @author chrisye
 * @version 2017-11-16
 */
@MyBatisDao
public interface RfaUrlDao extends CrudDao<RfaUrl> {
	RfaUrl getByUrlId(String urlId);
}