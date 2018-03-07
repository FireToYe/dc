/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaService;


/**
 * ica系统服务管理DAO接口
 * @author chrisyeye
 * @version 2017-09-11
 */
@MyBatisDao
public interface IccnIcaServiceDao extends CrudDao<IccnIcaService> {
	public IccnIcaService getByServiceCode(IccnIcaService iccnIcaService);
	public IccnIcaService getByServiceCode(String serviceCode);
	public IccnIcaService checkOnlyByServiceCode(IccnIcaService iccnIcaService);
	public void updateByServiceCodeAndErpType(IccnIcaService iccnIcaService);
	List<IccnIcaService> exportList(@Param("ids") List<String> ids);
}