/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.ConstraintViolationException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhilink.manager.common.config.PropertiesHolder;
import com.zhilink.manager.common.utils.HttpUtil;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.beanvalidator.BeanValidators;
import com.zhilink.manager.framework.common.exception.CommonException;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService; 
import com.zhilink.srm.common.utils.excel.ImportExcel;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaDb;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaService;
import com.zhilink.srm.manager.modules.ica.utils.DbConnTest;
import com.zhilink.srm.manager.modules.ica.utils.HttpClientUtils;
import com.zhilink.srm.manager.modules.ica.utils.IcaSpringContextHolder;
import com.zhilink.srm.manager.modules.ica.dao.IccnIcaServiceDao;

/**
 * ica系统服务管理Service
 * @author chrisyeye
 * @version 2017-09-11
 */
@Service
@Transactional(readOnly = true)
public class IccnIcaServiceService extends CrudService<IccnIcaServiceDao, IccnIcaService> implements DisposableBean{
	private ConcurrentHashMap<String, IccnIcaService> cacheMap = new ConcurrentHashMap<String, IccnIcaService>();
	@Value("${oss_url:http://oss.zhilink.com}")
	private String ossUrl;
	public IccnIcaService get(String id) {
		return super.get(id);
	}
	
	public List<IccnIcaService> findList(IccnIcaService iccnIcaService) {
		return super.findList(iccnIcaService);
	}
	
	public Page<IccnIcaService> findPage(Page<IccnIcaService> page, IccnIcaService iccnIcaService) {
		return super.findPage(page, iccnIcaService);
	}
	public IccnIcaService checkOnlyByServiceCode(IccnIcaService iccnIcaService){
		return dao.checkOnlyByServiceCode(iccnIcaService);
	}
	@Transactional(readOnly = false)
	public void save(IccnIcaService iccnIcaService) {
		cacheMap.remove(iccnIcaService.getServiceCode());
		super.save(iccnIcaService);
	}
	@Transactional(readOnly = false)
	public void updateByServiceCodeAndErpType(IccnIcaService iccnIcaService) {
		cacheMap.remove(iccnIcaService.getServiceCode());
		dao.updateByServiceCodeAndErpType(iccnIcaService);
	}
	@Transactional(readOnly = false)
	public void delete(IccnIcaService iccnIcaService) {
		super.delete(iccnIcaService);
		cacheMap.remove(iccnIcaService.getServiceCode());
	}
	public List<IccnIcaService> exportList(List<String> dataArray) {
		return dao.exportList(dataArray);
	}

	/**
	 * 执行sql
	 * @param sysDbBean dbBean
	 * @param dbsqls sql数组
	 * @param context 模板上下文环境
	 * @throws Exception
	 */
	public void executeSql(final IccnIcaDb sysDbBean, String[] dbsqls, VelocityContext context) throws Exception {
		DataSourceTransactionManager transactionManager_erp = null;
		try{
			transactionManager_erp = (DataSourceTransactionManager) IcaSpringContextHolder.getBean("transactionManager_"+sysDbBean.getMd5BeanName());
		}catch(Exception e){
			if(sysDbBean==null){
				throw new Exception("该服务数据库连接不存在，请联系管理员进行处理！");
			}
			if("0".equals(sysDbBean.getStatus().trim())){
				throw new Exception("数据库连接ID："+sysDbBean.getDbId()+"未启动，请联系管理员进行处理！");
			}
			if (!DbConnTest.connTestByDriver(sysDbBean.getJdbcDriverclassname(), sysDbBean.getJdbcUrl(), sysDbBean.getJdbcUsername(), sysDbBean.getJdbcPassword(),3000)) {
				throw new Exception("数据库连接ID："+sysDbBean.getDbId()+"连接失败，请联系管理员进行处理！");
			}else{
				new Thread(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						IcaSpringContextHolder.updateDataSource(sysDbBean);
					}
				}).start();
				throw new Exception("数据库连接ID："+sysDbBean.getDbId()+"已重启，请重试！");
			}
		}
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager_erp.getTransaction(def);
		try {
			JdbcTemplate jdbcTemplate_erp = IcaSpringContextHolder.getJdbcTemplate("jdbcTemplate_" + sysDbBean.getMd5BeanName());
			Object[] sqlData = new Object[dbsqls.length];
			int totolCount = 0;
			for (int i = 0; i < dbsqls.length; i++) {
					String sql = dbsqls[i].trim();
					if (StringUtils.isNotBlank(sql)) {
						logger.info(sql);
						if (sql.toUpperCase().indexOf("MERGE") != -1 || sql.toUpperCase().indexOf("INSERT") != -1 || sql.toUpperCase().indexOf("UPDATE") != -1 || sql.toUpperCase().indexOf("DELETE") != -1) {
							jdbcTemplate_erp.execute(sql);
							totolCount++;
						} else {
							List<Map<String, Object>> data = jdbcTemplate_erp.queryForList(sql);
							sqlData[i] = data;
						}
					}
			}
			context.put("totolCount", totolCount);
			context.put("status", "0");
			context.put("sqlData", sqlData);
		} catch (Exception e) {
			transactionManager_erp.rollback(status); // 也可以執行status.setRollbackOnly();
			throw e;
		}
		transactionManager_erp.commit(status);
	}
	
	public void executeSql(IccnIcaDb sysDbBean, String[] dbsqls, VelocityContext context, String serviceCode, VelocityEngine ve) throws Exception {
		DataSourceTransactionManager transactionManager_erp = (DataSourceTransactionManager) IcaSpringContextHolder.getApplicationContext().getBean("transactionManager_"+sysDbBean.getMd5BeanName());
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager_erp.getTransaction(def);
		try {
			JdbcTemplate jdbcTemplate_erp = IcaSpringContextHolder.getJdbcTemplate("jdbcTemplate_" + sysDbBean.getMd5BeanName());
			Object[] sqlData = new Object[dbsqls.length];
			int totolCount = 0;
			String sql;
			StringWriter dbSqlWriter ;
			for (int i = 0; i < dbsqls.length; i++) {
					sql = dbsqls[i].trim();
					
					if(sql.contains("sqlData")){
						dbSqlWriter = new StringWriter();
						ve.evaluate(context, dbSqlWriter, serviceCode, sql.replace("sqlData", "$sqlData"));
						sql=dbSqlWriter.toString();	
					}
					
					if (StringUtils.isNotBlank(sql)) {
						logger.info(sql);
						if (sql.toUpperCase().indexOf("MERGE") != -1 || sql.toUpperCase().indexOf("INSERT") != -1 || sql.toUpperCase().indexOf("UPDATE") != -1 || sql.toUpperCase().indexOf("DELETE") != -1) {
							jdbcTemplate_erp.execute(sql);
							totolCount++;
						} else {
							List<Map<String, Object>> data = jdbcTemplate_erp.queryForList(sql);
							sqlData[i] = data;
							context.put("sqlData", sqlData);
						}
					}
			}
			context.put("totolCount", totolCount);
			context.put("status", "0");
			context.put("sqlData", sqlData);
		} catch (Exception e) {
			transactionManager_erp.rollback(status); // 也可以執行status.setRollbackOnly();
			throw e;
		}
		transactionManager_erp.commit(status);
	}
	
	
	public IccnIcaService getByServiceCode(String serviceCode) {
		IccnIcaService iccnIcaService =null;
		if(cacheMap.containsKey(serviceCode)){
			iccnIcaService = cacheMap.get(serviceCode);
		}else{
			iccnIcaService = dao.getByServiceCode(serviceCode);
			if(iccnIcaService!=null){
				cacheMap.put(serviceCode, iccnIcaService);
			}
		}
		return iccnIcaService;
	}
	
	public IccnIcaService getByServiceCode(IccnIcaService iccnIcaService) {
		String serviceCode= iccnIcaService.getServiceCode();
		IccnIcaService result = new IccnIcaService();
		if(cacheMap.containsKey(serviceCode)){
			result = cacheMap.get(serviceCode);
		}else{
			result = dao.getByServiceCode(iccnIcaService);
			if(result!=null){
				cacheMap.put(serviceCode, result);
			}
		}
		return result;
	}

	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		cacheMap=null;
	}

	/**
	 * 从oss获取版本列表
	 * @param erpType
	 * @param fileType
	 * @return
	 */
	public JSONArray getVersionList(String erpType,String fileType){
		String userinfoUrl=String.format(ossUrl+"/api/version/versionList?erpType=%s&fileType=%s", erpType,fileType);
		String strUserInfo=HttpUtil.sendGet(userinfoUrl, "utf-8");
		JSONObject objUserInfo=JSONObject.parseObject(strUserInfo);
		JSONObject objHead=objUserInfo.getJSONObject("head");
		if(objHead.getInteger("status") !=1){
			throw new CommonException(objHead.getString("errorCode"), objHead.getString("errorMsg"));
		}
		JSONArray objBody=objUserInfo.getJSONArray("body");
		return objBody;
	}

	/**
	 * 从oss下载对应版本文件
	 * @param customVersion
	 * @param selectVersion
	 * @param erpType
	 * @param fileType
	 * @return
	 * @throws IOException
	 */
	public String saveVersion(String customVersion,String selectVersion,String erpType,String fileType) throws IOException{
		String filePath = "";
		if("ica".equals(fileType)){
			filePath = PropertiesHolder.getADPHome()+"/conf/icaFile/";
		}
		if("rfa".equals(fileType)){
			filePath = PropertiesHolder.getADPHome()+"/conf/rfaFile/";
		}
		String erpFilePath = filePath+erpType;
		File erpFile = new File(erpFilePath);
		if(!erpFile.exists()){
			erpFile.mkdirs();
		}
		String fileName =erpFilePath+"/"+customVersion+"_"+selectVersion+"_"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".xlsx";
		try {
			if(selectVersion==null){
				selectVersion="";
			}
			if(erpType==null){
				erpType="";
			}
			String url =ossUrl+  "/api/version/download?selectVersion="+selectVersion+"&erpType="+erpType+"&customVersion="+customVersion+"&fileType="+fileType;
			HttpClientUtils.getInstance().httpGet(
					url, fileName);
			return fileName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		
	}
	
}