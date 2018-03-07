package com.zhilink.srm.manager.modules.ica.web.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.activiti.engine.impl.util.json.JSONArray;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhilink.manager.common.utils.HttpUtil;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.beanvalidator.BeanValidators;
import com.zhilink.manager.framework.common.exception.CommonException;
import com.zhilink.manager.framework.common.utils.DateUtils;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.utils.excel.ExportExcel;
import com.zhilink.srm.common.utils.excel.ImportExcel;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.ica.dao.IcaApiLogDao;
import com.zhilink.srm.manager.modules.ica.entity.IcaApiLog;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaDb;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaService;
import com.zhilink.srm.manager.modules.ica.service.IcaApiLogService;
import com.zhilink.srm.manager.modules.ica.service.IccnIcaDbService;
import com.zhilink.srm.manager.modules.ica.service.IccnIcaServiceService;
import com.zhilink.srm.manager.modules.ica.thread.LogInsertThread;
import com.zhilink.srm.manager.modules.ica.utils.VEUtils;

/**
 * ica系统服务管理Controller
 * @author chrisyeye
 * @version 2017-09-11
 */
@Controller
@RequestMapping(value = "${adminPath}/ica/api")
public class IcaApiServiceController extends BaseController{
	@Value("${ica.erpType}")
	private String erpType;
	@Autowired
	private IccnIcaServiceService iccnIcaServiceService;
	
	@Autowired
	private IccnIcaDbService iccnIcaDbService;
	
	@Autowired
	private LogInsertThread logInsertThread;
	
//	@Autowired
//	private IcaApiLogDao icaApiLogDao;
//	private static VelocityEngine ve = new VelocityEngine();
//	
//	static{
//		ve.setProperty("velocimacro.permissions.allow.inline.local.scope", true);
//	}
	
	@RequestMapping("/service/{serviceCode}") 
	@ResponseBody
	public String service(@PathVariable("serviceCode")String serviceCode,@RequestHeader HttpHeaders headers,@RequestBody String bodys,HttpServletRequest request) throws Exception{
		long oldTime =System.currentTimeMillis();
		StringWriter dbSqlWriter = null;
		String sqlTemplate="";
		String respone="";
		IccnIcaService bean = null;
		String dbId = "";
		IccnIcaDb sysDbBean =null;
		try{
			Map<String, Object> headerMap = transtojsonmap(headers.toSingleValueMap());
			Map<String, Object> bodyMap = (Map<String, Object>) JSON.parse(bodys);
			String logTag = UUID.randomUUID().toString().replace("-", "");
			//logger.info("{} 请求:API{},body参数{},header参数{}", logTag, serviceCode, bodyMap, headerMap);
			try{
				//如果header存在 ica-db-id则使用指定的dbId进行连接
				dbId=headers.toSingleValueMap().get("ica-db-id");
				if(!StringUtils.isEmpty(dbId)){
					sysDbBean = iccnIcaDbService.getByDbId(dbId);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			long dbTime = System.currentTimeMillis();
			IccnIcaService iccnIcaService = new IccnIcaService();
			iccnIcaService.setServiceCode(serviceCode);
			iccnIcaService.setErpType(erpType);
			bean = iccnIcaServiceService.getByServiceCode(iccnIcaService);
			if(bean==null){
				throw new NullPointerException("服务编码："+serviceCode+"当前系统不存在，请联系管理员进行处理");
			}
			//logger.info("db第一次执行时间:"+(System.currentTimeMillis()-dbTime));
			//如果指定的dbId无法查找到，则使用默认的dbId
			if(sysDbBean==null){
				dbId = bean.getDbId();
				sysDbBean = iccnIcaDbService.getByDbId(dbId);
			}
			if(sysDbBean==null){
				throw new NullPointerException("数据库连接Id："+dbId+"当前系统不存在，请联系管理员进行处理");
			}
			//logger.info("db执行时间:"+(System.currentTimeMillis()-dbTime));
			sqlTemplate = bean.getDbSql().replace("isNOTEmpty", "isNotBlank");
			// 1.模板上下文环境
			VelocityEngine ve = new VelocityEngine();
			VelocityContext context = new VelocityContext();
			context.put("StringUtils", StringUtils.class);
			context.put("DateUtils", DateUtils.class);
			context.put("JSON", JSON.class);
			context.put("String", String.class);
			context.put("Integer", Integer.class);
			context.put("VEUtils", VEUtils.class);
			context.put("headerMap", headerMap);
			context.put("bodyMap", bodyMap);
			Map<String, String> execution = new HashMap<String, String>();
			execution.put("code", "0");
			execution.put("sqlcode", "0");
			execution.put("description", "执行成功！");
			context.put("execution", execution);
			String isSuccess = "1";
			// 2.解析SQL，并执行
			try {
				dbSqlWriter = new StringWriter();
				ve.evaluate(context, dbSqlWriter, serviceCode, sqlTemplate);
				String[] dbsqls = dbSqlWriter.toString().split(";(\\r\\n|\\n)");
				long sqlStartTime = System.currentTimeMillis();
				iccnIcaServiceService.executeSql(sysDbBean,dbsqls, context);			
				//logger.info("sql执行时间:"+(System.currentTimeMillis()-sqlStartTime));
				//sysServiceService.executeSql(sysDbBean,dbsqls, context, serviceCode, ve);
			} catch (Exception e) {
				logger.error(serviceCode + "执行错误", e);
				execution.put("code", "-1");
				execution.put("sqlcode", e.getCause()!=null?e.getCause().getMessage():e.getMessage()==null?e.toString():e.getMessage());
				execution.put("description", e.getMessage()==null?e.toString():e.getMessage());
				context.put("status", "-1");
				context.put("totolCount", 0);
				isSuccess="0";
			}
			// 3.解析 响应并执行
			String responeTemplat = bean.getResponeTemplate();
			StringWriter responeWriter = new StringWriter();
			ve.evaluate(context, responeWriter, serviceCode, responeTemplat);
			respone = responeWriter.toString();
			//logger.info("{} 响应:{}", logTag, respone);
			String result = respone;
			insertLog(serviceCode, bean.getServiceName(), bodys, dbSqlWriter.toString(), bean.getResponeTemplate(),respone,String.valueOf(System.currentTimeMillis()-oldTime), isSuccess, request,dbId);
			logger.info("总耗时:"+(System.currentTimeMillis()-oldTime));
			return result;
		}catch(Exception e){
			e.printStackTrace();
			 Writer writer = new StringWriter();
	         e.printStackTrace(new PrintWriter(writer));  
	         String res =writer.toString();
			 if(bean!=null){
				insertLog(serviceCode, bean.getServiceName(), bodys, sqlTemplate, bean.getResponeTemplate(),res,String.valueOf(System.currentTimeMillis()-oldTime), "0", request,dbId);
			 }else{
				insertLog(serviceCode, "", bodys, sqlTemplate, "",res,String.valueOf(System.currentTimeMillis()-oldTime), "0", request,dbId);
			 }
			 Map<String,Object> map = new HashMap<String, Object>();
			 Map<String,Object> std_data = new HashMap<String, Object>();
			 Map<String,String> execution = new HashMap<String, String>();
			 execution.put("code", "-1");
			 execution.put("sqlcode", e.getMessage());
			 execution.put("description", res);
			 std_data.put("execution", execution);
			 map.put("std_data",std_data);
			 return JSON.toJSONString(map,true).replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n")
					 .replaceAll("\\\\t", "\t");
		}
	}

	
	
	
	private Map<String, Object> transtojsonmap(Map<String, String> singleValueMap) {
		Map<String, Object> headerMap = new HashMap<String, Object>();
		for (Map.Entry<String, String> entry : singleValueMap.entrySet()) {
			try {
				Object json = JSON.parse(entry.getValue());
				headerMap.put(entry.getKey(), json);
			} catch (Exception e) {
				headerMap.put(entry.getKey(), entry.getValue());
			}
		}
		return headerMap;
	}
	
	@RequestMapping("/testService") 
	@ResponseBody
	public String service(@RequestBody IccnIcaService iccnIcaService,@RequestHeader HttpHeaders headers,HttpServletRequest request) throws Exception{
		long oldTime =System.currentTimeMillis();
		String serviceCode ="";
		StringWriter dbSqlWriter = null;
		String sqlTemplate="";
		String respone="";
		try{
			Map<String, Object> headerMap = transtojsonmap(headers.toSingleValueMap());
			Map<String, Object> bodyMap = (Map<String, Object>) JSON.parse(iccnIcaService.getServiceParams());
			serviceCode = iccnIcaService.getServiceCode();
			String logTag = UUID.randomUUID().toString().replace("-", "");
			//logger.info("{} 请求:API{},body参数{},header参数{}", logTag, iccnIcaService, bodyMap, headerMap);
			String dbId = iccnIcaService.getDbId();
			IccnIcaDb sysDbBean = iccnIcaDbService.getByDbId(dbId);
			sqlTemplate = iccnIcaService.getDbSql().replace("isNOTEmpty", "isNotBlank");
			// 1.模板上下文环境
			VelocityEngine ve = new VelocityEngine();
			VelocityContext context = new VelocityContext();
			context.put("StringUtils", StringUtils.class);
			context.put("DateUtils", DateUtils.class);
			context.put("JSON", JSON.class);
			context.put("String", String.class);
			context.put("Integer", Integer.class);
			context.put("VEUtils", VEUtils.class);
			context.put("headerMap", headerMap);
			context.put("bodyMap", bodyMap);
			Map<String, String> execution = new HashMap<String, String>();
			execution.put("code", "0");
			execution.put("sqlcode", "0");
			execution.put("description", "执行成功！");
			context.put("execution", execution);
			String isSuccess = "1";
			// 2.解析SQL，并执行
			try {
				dbSqlWriter = new StringWriter();
				ve.evaluate(context, dbSqlWriter, serviceCode, sqlTemplate);
				String[] dbsqls = dbSqlWriter.toString().split(";(\\r\\n|\\n)");
				long sqlStartTime = System.currentTimeMillis();
				iccnIcaServiceService.executeSql(sysDbBean,dbsqls, context);			
				//logger.info("sql执行时间:"+(System.currentTimeMillis()-sqlStartTime));
				//sysServiceService.executeSql(sysDbBean,dbsqls, context, serviceCode, ve);
			} catch (Exception e) {
				logger.error(serviceCode + "执行错误", e);
				execution.put("code", "-1");
				execution.put("sqlcode", e.getCause()!=null?e.getCause().getMessage():e.getMessage()==null?e.toString():e.getMessage());
				execution.put("description", e.getMessage()==null?e.toString():e.getMessage());
				context.put("status", "-1");
				context.put("totolCount", 0);
				isSuccess = "0";
			}
			// 3.解析 响应并执行
			String responeTemplat = iccnIcaService.getResponeTemplate();
			StringWriter responeWriter = new StringWriter();
			ve.evaluate(context, responeWriter, serviceCode, responeTemplat);
			respone = responeWriter.toString();
			//logger.info("{} 响应:{}", logTag, respone);
			String result = "";
			try{
				JSONObject json = JSONObject.parseObject(respone);//json格式化显示
				result = JSON.toJSONString(json,true);//json格式化会把字符串中的换行字符格式化为\\n
			}catch(Exception e){
				result = respone;
			}
			insertLog(serviceCode, iccnIcaService.getServiceName(), iccnIcaService.getServiceParams(), dbSqlWriter.toString(), iccnIcaService.getResponeTemplate(),respone,String.valueOf(System.currentTimeMillis()-oldTime), isSuccess, request,dbId);
			//logger.info("总耗时:"+(System.currentTimeMillis()-oldTime));
			return result;
		}catch(Exception e){
			e.printStackTrace();
			 Writer writer = new StringWriter();
	         e.printStackTrace(new PrintWriter(writer));  
	         String res =writer.toString();
			 if(iccnIcaService!=null){
				 insertLog(serviceCode, iccnIcaService.getServiceName(), iccnIcaService.getServiceParams(), sqlTemplate, iccnIcaService.getResponeTemplate(),res,String.valueOf(System.currentTimeMillis()-oldTime), "0", request,iccnIcaService.getDbId());
			 }else{
				 insertLog(serviceCode, "", "", sqlTemplate, "",respone,String.valueOf(System.currentTimeMillis()-oldTime), "0", request,"");

			 }
			 Map<String,Object> map = new HashMap<String, Object>();
			 Map<String,Object> std_data = new HashMap<String, Object>();
			 Map<String,String> execution = new HashMap<String, String>();
			 execution.put("code", "-1");
			 execution.put("sqlcode", e.getMessage());
			 execution.put("description",res );
			 std_data.put("execution", execution);
			 map.put("std_data",std_data);
			 return JSON.toJSONString(map,true).replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n")
					 .replaceAll("\\\\t", "\t");
		}
	}




	
	/**
	 * 插入日志
	 * @param serviceCode
	 * @param serviceName
	 * @param serviceParams
	 * @param dbSql
	 * @param responeTemplate
	 * @param result
	 * @param resDuration
	 * @param isSuccess
	 * @param request
	 */
	@SuppressWarnings("unused")
	private void insertLog(String serviceCode,String serviceName,String serviceParams,
			String dbSql,String responeTemplate,String result,String resDuration,String isSuccess,HttpServletRequest request,String dbId){
		IcaApiLog icaApiLog = new IcaApiLog();
		icaApiLog.setServiceCode(serviceCode);
		icaApiLog.setServiceName(serviceName);
		icaApiLog.setServiceParams(serviceParams);
		icaApiLog.setDbSql(dbSql);
		icaApiLog.setResponeTemplate(responeTemplate);
		icaApiLog.setResult(result);
		icaApiLog.setResDuration(resDuration);
		icaApiLog.setIsSuccess(isSuccess);
		icaApiLog.setDbId(dbId);
		//获取客户端ip，如被转发，虚获取请求头 x-forwarded-for的信息
		if (request.getHeader("x-forwarded-for")== null){
			icaApiLog.setIp(request.getRemoteAddr());
		}else{
			icaApiLog.setIp(request.getHeader("x-forwarded-for"));
		}
//		icaApiLogDao.insert(icaApiLog);
		logInsertThread.insert(icaApiLog);
	}
	

	public String export(IccnIcaService iccnIcaService, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			String fileName = I18n.i18nMessage("ica.service_data")+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";		
			iccnIcaService.setErpType(erpType);
			List<IccnIcaService> result = iccnIcaServiceService.findList(iccnIcaService);
			new ExportExcel(I18n.i18nMessage("ica.service_data"), IccnIcaService.class,true).setDataList(result).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes,  I18n.i18nMessage("ica.service_export_error_controller_msg")+"： "+ e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaService/?repage";
	}

	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<IccnIcaService> list = ei.getDataList(IccnIcaService.class);
			for (IccnIcaService iccnIcaService : list) {
				try {
					if(StringUtils.isEmpty(iccnIcaService.getErpType())){
						iccnIcaService.setErpType(erpType);
					}
					if ("true".equals(checkDbId("", iccnIcaService.getServiceCode(),iccnIcaService.getErpType()))) {
						BeanValidators.validateWithException(validator, iccnIcaService);
						iccnIcaServiceService.save(iccnIcaService);
						successNum++;
					} else {
						iccnIcaService.setIsNewRecord(true);
						BeanValidators.validateWithException(validator, iccnIcaService);
						if("Y".equals(iccnIcaService.getForceUpdate())){
							iccnIcaServiceService.updateByServiceCodeAndErpType(iccnIcaService);
							successNum++;
						}else if(iccnIcaService.getCustomVersion()!=null){
							if(!iccnIcaService.getCustomVersion().startsWith("CV_")){
								iccnIcaServiceService.updateByServiceCodeAndErpType(iccnIcaService);
								successNum++;
							}else{
								failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + iccnIcaService.getServiceCode() +" "+ I18n.i18nMessage("ica.import_exist")+"; ");
								failureNum++;
							}
						}else{
							iccnIcaServiceService.updateByServiceCodeAndErpType(iccnIcaService);
							successNum++;
						}
						/*failureMsg.append("<br/>数据库连接ID" + iccnIcaService.getDbId() + " 已存在; ");
						failureNum++;*/
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + iccnIcaService.getServiceCode() + " "+I18n.i18nMessage("ica.import_failure")+"：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + iccnIcaService.getServiceCode() +  " "+I18n.i18nMessage("ica.import_failure")+"："+ ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，"+I18n.i18nMessage("ica.failure") + failureNum + " "+I18n.i18nMessage("ica.service_import_error_controller_msg")+"：");
			}
			return  "import_success:"+I18n.i18nMessage("ica.impory_success_al")+ " "+ successNum + " "+I18n.i18nMessage("ica.service_import_number_error_msg")+" " + failureMsg;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e.getMessage());
		}
	}
	
	public String checkDbId(String oldServiceCode, String serviceCode,String erpTypeNew) {
		IccnIcaService iccnIcaService = new IccnIcaService();
		iccnIcaService.setErpType(erpTypeNew);
		iccnIcaService.setServiceCode(serviceCode);
		if (oldServiceCode != null && serviceCode.equals(oldServiceCode)) {
			return "true";
		} else if (serviceCode != null && iccnIcaServiceService.checkOnlyByServiceCode(iccnIcaService) == null) {
			return "true";
		}
		return "false";
	}
	
	public static void main(String[] args) {
		String json = HttpUtil.sendGet("http://localhost:8080/dc-manager/a/ica/api/export2json", "UTF-8");
		Map<String,String> map = new HashMap<String, String>();
		map.put("json", json);
		System.out.println(HttpUtil.sendPost("http://localhost:8080/dc-manager/a/ica/api/import2json", map,"UTF-8"));
	}
}