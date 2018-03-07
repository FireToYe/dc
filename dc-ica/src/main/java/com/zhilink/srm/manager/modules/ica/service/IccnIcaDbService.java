/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.ica.dao.IccnIcaDbDao;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaDb;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaService;


/**
 * 集成管理Service
 * @author chrisye
 * @version 2017-09-08
 */
@Service
@Transactional(readOnly = true)
public class IccnIcaDbService extends CrudService<IccnIcaDbDao, IccnIcaDb> implements DisposableBean{
	/**
	 * 缓冲集合，以serviceCode为key值，减少从db获取,提升效率
	 */
	private ConcurrentHashMap<String, IccnIcaDb> cacheMap = new ConcurrentHashMap<String, IccnIcaDb>();
	@Autowired
	IccnIcaDbDao iccnIcaDbDao;
	public IccnIcaDb get(String id) {
		return super.get(id);
	}
	
	public List<IccnIcaDb> findList(IccnIcaDb iccnIcaDb) {
		return super.findList(iccnIcaDb);
	}
	
	public Page<IccnIcaDb> findPage(Page<IccnIcaDb> page, IccnIcaDb iccnIcaDb) {
		return super.findPage(page, iccnIcaDb);
	}
	
	@Transactional(readOnly = false)
	public void save(IccnIcaDb iccnIcaDb) {
		cacheMap.remove(iccnIcaDb.getDbId());
		super.save(iccnIcaDb);
	}
	
	@Transactional(readOnly = false)
	public void delete(IccnIcaDb iccnIcaDb) {
		super.delete(iccnIcaDb);
		cacheMap.remove(iccnIcaDb.getDbId());
	}
	
	public IccnIcaDb getByDbId(String id){
		IccnIcaDb iccnIcaDb =null;
		if(cacheMap.containsKey(id)){
			iccnIcaDb=cacheMap.get(id);
		}else{
			iccnIcaDb = iccnIcaDbDao.getByDbId(id);
			if(iccnIcaDb!=null){
				cacheMap.put(id, iccnIcaDb);
			}
		}
		return iccnIcaDb;
	}

	public IccnIcaDb checkOnlyByDbId(String id){
		IccnIcaDb iccnIcaDb =dao.getByDbId(id);
		return iccnIcaDb;
	}
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		cacheMap =null;
	}
}