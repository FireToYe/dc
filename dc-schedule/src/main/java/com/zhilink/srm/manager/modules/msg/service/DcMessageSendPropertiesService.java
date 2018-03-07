/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.msg.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.zhilink.manager.framework.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.msg.entity.DcMessageSendProperties;
import com.zhilink.srm.manager.modules.msg.dao.DcMessageSendPropertiesDao;

/**
 * 信息发送配置Service
 * @author chrisye
 * @version 2018-02-06
 */
@Service
@Transactional(readOnly = true)
public class DcMessageSendPropertiesService extends CrudService<DcMessageSendPropertiesDao, DcMessageSendProperties> {
	private ConcurrentHashMap<String, DcMessageSendProperties> cacheMap = new ConcurrentHashMap<String, DcMessageSendProperties>();
	private DcMessageSendProperties msgDefaultProperties;
	private DcMessageSendProperties emailDefaultProperties;
	public DcMessageSendProperties get(String id) {
		DcMessageSendProperties cache = super.get(id);
		if(cache!=null){
			cacheMap.put(cache.getId(),cache);
		}
		return cache;
	}
	
	public List<DcMessageSendProperties> findList(DcMessageSendProperties dcMessageSendProperties) {
		return super.findList(dcMessageSendProperties);
	}
	
	public Page<DcMessageSendProperties> findPage(Page<DcMessageSendProperties> page, DcMessageSendProperties dcMessageSendProperties) {
		return super.findPage(page, dcMessageSendProperties);
	}
	
	@Transactional(readOnly = false)
	public void save(DcMessageSendProperties dcMessageSendProperties) {
		if(msgDefaultProperties.getId().equals(dcMessageSendProperties.getId())){
			msgDefaultProperties =null;
		}
		if(emailDefaultProperties.getId().equals(dcMessageSendProperties.getId())){
			emailDefaultProperties =null;
		}
		if(!StringUtils.isEmpty(dcMessageSendProperties.getId())){
			cacheMap.remove(dcMessageSendProperties.getId());
		}
		super.save(dcMessageSendProperties);
	}
	
	@Transactional(readOnly = false)
	public void delete(DcMessageSendProperties dcMessageSendProperties) {
		if(msgDefaultProperties.getId().equals(dcMessageSendProperties.getId())){
			msgDefaultProperties =null;
		}
		if(emailDefaultProperties.getId().equals(dcMessageSendProperties.getId())){
			emailDefaultProperties =null;
		}
		if(!StringUtils.isEmpty(dcMessageSendProperties.getId())){
			cacheMap.remove(dcMessageSendProperties.getId());
		}
		super.delete(dcMessageSendProperties);
	}

	public DcMessageSendProperties getByCache(String id,String type){
		if(StringUtils.isEmpty(id)){
			if("1".equals(type)){
				if(msgDefaultProperties==null){
					msgDefaultProperties = dao.getDefault(type);
				}
				return msgDefaultProperties;
			}
			if("2".equals(type)){
				if(emailDefaultProperties==null){
					emailDefaultProperties = dao.getDefault(type);
				}
				return emailDefaultProperties;
			}
		}else{
			return get(id);
		}
		return null;
	}


}