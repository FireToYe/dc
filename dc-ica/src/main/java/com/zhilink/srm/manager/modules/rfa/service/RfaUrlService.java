/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaDb;
import com.zhilink.srm.manager.modules.rfa.entity.RfaUrl;
import com.zhilink.srm.manager.modules.rfa.dao.RfaUrlDao;

/**
 * rfa连接管理Service
 * @author chrisye
 * @version 2017-11-16
 */
@Service
@Transactional(readOnly = true)
public class RfaUrlService extends CrudService<RfaUrlDao, RfaUrl> {
	private ConcurrentHashMap<String, RfaUrl> cacheMap = new ConcurrentHashMap<String, RfaUrl>();
	public RfaUrl get(String id) {
		return super.get(id);
	}
	
	public List<RfaUrl> findList(RfaUrl rfaUrl) {
		return super.findList(rfaUrl);
	}
	
	public Page<RfaUrl> findPage(Page<RfaUrl> page, RfaUrl rfaUrl) {
		return super.findPage(page, rfaUrl);
	}
	
	public RfaUrl getByUrlId(String urlId) {
		if(urlId ==null){
			return null;
		}
		RfaUrl rfaUrl =null;
		if(cacheMap.containsKey(urlId)){
			rfaUrl=cacheMap.get(urlId);
		}else{
			rfaUrl = dao.getByUrlId(urlId);
			if(rfaUrl!=null){
				cacheMap.put(urlId, rfaUrl);
			}
		}
		return rfaUrl;
	}
	public RfaUrl checkOnly(String urlId) {
		RfaUrl rfaUrl = dao.getByUrlId(urlId);
		return rfaUrl;
	}
	@Transactional(readOnly = false)
	public void save(RfaUrl rfaUrl) {
		cacheMap.remove(rfaUrl.getUrlId());
		super.save(rfaUrl);
	}
	
	@Transactional(readOnly = false)
	public void delete(RfaUrl rfaUrl) {
		super.delete(rfaUrl);
		cacheMap.remove(rfaUrl.getUrlId());
	}
	
}