/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.ica.entity.IcaApiLog;
import com.zhilink.srm.manager.modules.ica.dao.IcaApiLogDao;

/**
 * ica请求日志Service
 * @author chrisye
 * @version 2017-09-15
 */
@Service
@Transactional(readOnly = true)
public class IcaApiLogService extends CrudService<IcaApiLogDao, IcaApiLog> {
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(IcaApiLogService.class);
	@Autowired
	private IcaApiLogDao icaApiLogDao;

	public IcaApiLog get(String id) {
		return super.get(id);
	}
	
	public List<IcaApiLog> findList(IcaApiLog icaApiLog) {
		return super.findList(icaApiLog);
	}
	
	public Page<IcaApiLog> findPage(Page<IcaApiLog> page, IcaApiLog icaApiLog) {
		return super.findPage(page, icaApiLog);
	}
	
	@Transactional(readOnly = false)
	public void save(IcaApiLog icaApiLog) {
		try{
			super.save(icaApiLog);
		}catch(Exception e){
			logger.error(e.getMessage());
			logger.info(icaApiLog.toString());//存入日志异常时。将实体存入到日志文件中
			e.printStackTrace();
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(IcaApiLog icaApiLog) {
		super.delete(icaApiLog);
	}
	
	@Transactional(readOnly = false)
	public void deleteByTask(Date createTime) {
		icaApiLogDao.deleteByTask(createTime);
	}
	@Transactional(readOnly = false)
	public void deleteRfaByTask(Date createTime) {
		icaApiLogDao.deleteRfaByTask(createTime);
	}
	
	@Transactional(readOnly = false)
	public void deleteByTask(IcaApiLog IcaApiLog) {
		icaApiLogDao.deleteByTask(IcaApiLog);
	}
}