/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.rfa.entity.RfaService;

/**
 * rfa系统服务管理DAO接口
 * @author chrisye
 * @version 2017-11-14
 */
@MyBatisDao
public interface RfaServiceDao extends CrudDao<RfaService> {
	RfaService getByServiceCode(String serviceCode);
	RfaService getByServiceCode(RfaService rfaService);
	RfaService checkOnlyServiceCode(String serviceCode);
	void updateByServiceCode(RfaService rfaService);
}