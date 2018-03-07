/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaDb;


/**
 * 集成管理DAO接口
 * @author chrisye
 * @version 2017-09-08
 */
@MyBatisDao
public interface IccnIcaDbDao extends CrudDao<IccnIcaDb> {
	public IccnIcaDb getByDbId(IccnIcaDb iccnIcaDb);
	public IccnIcaDb getByDbId(String id);
}