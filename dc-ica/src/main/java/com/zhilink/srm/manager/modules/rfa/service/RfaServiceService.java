/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.manager.framework.common.utils.DateUtils;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.rfa.entity.RfaService;
import com.zhilink.srm.manager.modules.rfa.entity.RfaUrl;
import com.zhilink.srm.manager.modules.rfa.dao.RfaServiceDao;

/**
 * rfa系统服务管理Service
 * @author chrisye
 * @version 2017-11-14
 */
@Service
@Transactional(readOnly = true)
public class RfaServiceService extends CrudService<RfaServiceDao, RfaService> {
	private ConcurrentHashMap<String, RfaService> cacheMap = new ConcurrentHashMap<String, RfaService>();
	public RfaService get(String id) {
		return super.get(id);
	}
	
	public List<RfaService> findList(RfaService rfaService) {
		return super.findList(rfaService);
	}
	
	public Page<RfaService> findPage(Page<RfaService> page, RfaService rfaService) {
		return super.findPage(page, rfaService);
	}
	
	@Transactional(readOnly = false)
	public void save(RfaService rfaService) {
		rfaService.setSystemVersion("v_"+DateUtils.getDate("yyyyMMddHHmmssSSS"));
		super.save(rfaService);
		cacheMap.remove(rfaService.getServiceCode());
	}
	
	@Transactional(readOnly = false)
	public void delete(RfaService rfaService) {
		super.delete(rfaService);
		cacheMap.remove(rfaService.getServiceCode());
	}
	
	public RfaService getByServiceCode(String serviceCode) {
		RfaService rfaService =null;
		if(cacheMap.containsKey(serviceCode)){
			rfaService=cacheMap.get(serviceCode);
		}else{
			rfaService = dao.getByServiceCode(serviceCode);
			if(rfaService!=null){
				cacheMap.put(serviceCode, rfaService);
			}
		}
		return rfaService;
	}
	
	public RfaService getByServiceCode(RfaService rfaService) {
		RfaService result =null;
		if(cacheMap.containsKey(rfaService.getServiceCode())){
			result=cacheMap.get(rfaService.getServiceCode());
		}else{
			result = dao.getByServiceCode(rfaService);
			if(result!=null){
				cacheMap.put(rfaService.getServiceCode(), result);
			}
		}
		return result;
	}
	
	public RfaService checkOnlyServiceCode(String serviceCode) {
		RfaService result =dao.checkOnlyServiceCode(serviceCode);
		return result;
	}
	@Transactional(readOnly = false)
	public void updateByServiceCode(RfaService rfaService){
		dao.updateByServiceCode(rfaService);
	}
}