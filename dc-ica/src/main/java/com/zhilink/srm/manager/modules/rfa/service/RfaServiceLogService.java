/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.rfa.entity.RfaServiceLog;
import com.zhilink.srm.manager.modules.rfa.dao.RfaServiceLogDao;

/**
 * 接口适配器调用日志Service
 * @author chrisye
 * @version 2017-11-17
 */
@Service
@Transactional(readOnly = true)
public class RfaServiceLogService extends CrudService<RfaServiceLogDao, RfaServiceLog> {

	public RfaServiceLog get(String id) {
		return super.get(id);
	}
	
	public List<RfaServiceLog> findList(RfaServiceLog rfaServiceLog) {
		return super.findList(rfaServiceLog);
	}
	
	public Page<RfaServiceLog> findPage(Page<RfaServiceLog> page, RfaServiceLog rfaServiceLog) {
		return super.findPage(page, rfaServiceLog);
	}
	
	@Transactional(readOnly = false)
	public void save(RfaServiceLog rfaServiceLog) {
		super.save(rfaServiceLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(RfaServiceLog rfaServiceLog) {
		super.delete(rfaServiceLog);
	}
	
}